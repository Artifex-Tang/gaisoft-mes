package com.gaisoft.framework.web.exception;

import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.text.Convert;
import com.gaisoft.common.exception.DemoModeException;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.html.EscapeUtil;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value={AccessDeniedException.class})
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", (Object)requestURI, (Object)e.getMessage());
        return AjaxResult.error((int)403, (String)"没有权限，请联系管理员授权");
    }

    @ExceptionHandler(value={HttpRequestMethodNotSupportedException.class})
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", (Object)requestURI, (Object)e.getMethod());
        return AjaxResult.error((String)e.getMessage());
    }

    @ExceptionHandler(value={ServiceException.class})
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), (Throwable)e);
        Integer code = e.getCode();
        return StringUtils.isNotNull((Object)code) ? AjaxResult.error((int)code, (String)e.getMessage()) : AjaxResult.error((String)e.getMessage());
    }

    @ExceptionHandler(value={MissingPathVariableException.class})
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", (Object)requestURI, (Object)e);
        return AjaxResult.error((String)String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    @ExceptionHandler(value={MethodArgumentTypeMismatchException.class})
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr((Object)e.getValue());
        if (StringUtils.isNotEmpty((String)value)) {
            value = EscapeUtil.clean((String)value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", (Object)requestURI, (Object)e);
        return AjaxResult.error((String)String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    @ExceptionHandler(value={RuntimeException.class})
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", (Object)requestURI, (Object)e);
        return AjaxResult.error((String)e.getMessage());
    }

    @ExceptionHandler(value={Exception.class})
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", (Object)requestURI, (Object)e);
        return AjaxResult.error((String)e.getMessage());
    }

    @ExceptionHandler(value={BindException.class})
    public AjaxResult handleBindException(BindException e) {
        log.error(e.getMessage(), (Throwable)e);
        String message = ((ObjectError)e.getAllErrors().get(0)).getDefaultMessage();
        return AjaxResult.error((String)message);
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), (Throwable)e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error((String)message);
    }

    @ExceptionHandler(value={DemoModeException.class})
    public AjaxResult handleDemoModeException(DemoModeException e) {
        return AjaxResult.error((String)"演示模式，不允许操作");
    }
}
