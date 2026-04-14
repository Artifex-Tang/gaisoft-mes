package com.gaisoft.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.gaisoft.common.annotation.RepeatSubmit;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.utils.ServletUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public abstract class RepeatSubmitInterceptor
implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null && this.isRepeatSubmit(request, annotation)) {
                AjaxResult ajaxResult = AjaxResult.error((String)annotation.message());
                ServletUtils.renderString((HttpServletResponse)response, (String)JSON.toJSONString((Object)ajaxResult));
                return false;
            }
            return true;
        }
        return true;
    }

    public abstract boolean isRepeatSubmit(HttpServletRequest var1, RepeatSubmit var2);
}
