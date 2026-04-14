package com.gaisoft.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.core.text.Convert;
import com.gaisoft.common.enums.BusinessStatus;
import com.gaisoft.common.enums.HttpMethod;
import com.gaisoft.common.filter.PropertyPreExcludeFilter;
import com.gaisoft.common.utils.ExceptionUtil;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.ServletUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.ip.IpUtils;
import com.gaisoft.framework.manager.AsyncManager;
import com.gaisoft.framework.manager.factory.AsyncFactory;
import com.gaisoft.system.domain.SysOperLog;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    public static final String[] EXCLUDE_PROPERTIES = new String[]{"password", "oldPassword", "newPassword", "confirmPassword"};
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal("Cost Time");

    @Before(value="@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut="@annotation(controllerLog)", returning="jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        this.handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value="@annotation(controllerLog)", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        this.handleLog(joinPoint, controllerLog, e, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(Integer.valueOf(BusinessStatus.SUCCESS.ordinal()));
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring((String)ServletUtils.getRequest().getRequestURI(), (int)0, (int)255));
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUsername());
                SysUser currentUser = loginUser.getUser();
                if (StringUtils.isNotNull((Object)currentUser) && StringUtils.isNotNull((Object)currentUser.getDept())) {
                    operLog.setDeptName(currentUser.getDept().getDeptName());
                }
            }
            if (e != null) {
                operLog.setStatus(Integer.valueOf(BusinessStatus.FAIL.ordinal()));
                operLog.setErrorMsg(StringUtils.substring((String)Convert.toStr((Object)e.getMessage(), (String)ExceptionUtil.getExceptionMessage((Throwable)e)), (int)0, (int)2000));
            }
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            this.getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            operLog.setCostTime(Long.valueOf(System.currentTimeMillis() - TIME_THREADLOCAL.get()));
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp) {
            log.error("异常信息:{}", (Object)exp.getMessage());
            exp.printStackTrace();
        }
        finally {
            TIME_THREADLOCAL.remove();
        }
    }

    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog, Object jsonResult) throws Exception {
        operLog.setBusinessType(Integer.valueOf(log.businessType().ordinal()));
        operLog.setTitle(log.title());
        operLog.setOperatorType(Integer.valueOf(log.operatorType().ordinal()));
        if (log.isSaveRequestData()) {
            this.setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        if (log.isSaveResponseData() && StringUtils.isNotNull((Object)jsonResult)) {
            operLog.setJsonResult(StringUtils.substring((String)JSON.toJSONString((Object)jsonResult), (int)0, (int)2000));
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) throws Exception {
        Map paramsMap = ServletUtils.getParamMap((ServletRequest)ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        if (StringUtils.isEmpty((Map)paramsMap) && StringUtils.equalsAny((CharSequence)requestMethod, (CharSequence[])new CharSequence[]{HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name()})) {
            String params = this.argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring((String)params, (int)0, (int)2000));
        } else {
            operLog.setOperParam(StringUtils.substring((String)JSON.toJSONString((Object)paramsMap, (Filter)this.excludePropertyPreFilter(excludeParamNames), (JSONWriter.Feature[])new JSONWriter.Feature[0]), (int)0, (int)2000));
        }
    }

    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!StringUtils.isNotNull((Object)o) || this.isFilterObject(o)) continue;
                try {
                    String jsonObj = JSON.toJSONString((Object)o, (Filter)this.excludePropertyPreFilter(excludeParamNames), (JSONWriter.Feature[])new JSONWriter.Feature[0]);
                    params = params + jsonObj.toString() + " ";
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return params.trim();
    }

    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes((String[])ArrayUtils.addAll((Object[])EXCLUDE_PROPERTIES, (Object[])excludeParamNames));
    }

    public boolean isFilterObject(Object o) {
        Map map;
        Iterator iterator;
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection)o;
            Iterator iterator2 = collection.iterator();
            if (iterator2.hasNext()) {
                Object value = iterator2.next();
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz) && (iterator = (map = (Map)o).entrySet().iterator()).hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            return entry.getValue() instanceof MultipartFile;
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof BindingResult;
    }
}
