package com.gaisoft.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.utils.ServletUtils;
import com.gaisoft.common.utils.StringUtils;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointImpl
implements AuthenticationEntryPoint,
Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        int code = 401;
        String msg = StringUtils.format((String)"请求访问：{}，认证失败，无法访问系统资源", (Object[])new Object[]{request.getRequestURI()});
        ServletUtils.renderString((HttpServletResponse)response, (String)JSON.toJSONString((Object)AjaxResult.error((int)code, (String)msg)));
    }
}
