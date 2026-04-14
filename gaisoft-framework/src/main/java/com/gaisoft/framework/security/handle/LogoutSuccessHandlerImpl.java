package com.gaisoft.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.utils.MessageUtils;
import com.gaisoft.common.utils.ServletUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.manager.AsyncManager;
import com.gaisoft.framework.manager.factory.AsyncFactory;
import com.gaisoft.framework.web.service.TokenService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class LogoutSuccessHandlerImpl
implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        if (StringUtils.isNotNull((Object)loginUser)) {
            String userName = loginUser.getUsername();
            this.tokenService.delLoginUser(loginUser.getToken());
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, "Logout", MessageUtils.message((String)"user.logout.success", (Object[])new Object[0]), new Object[0]));
        }
        ServletUtils.renderString((HttpServletResponse)response, (String)JSON.toJSONString((Object)AjaxResult.success((String)MessageUtils.message((String)"user.logout.success", (Object[])new Object[0]))));
    }
}
