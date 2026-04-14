package com.gaisoft.framework.security.filter;

import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.web.service.TokenService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationTokenFilter
extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        if (StringUtils.isNotNull((Object)loginUser) && StringUtils.isNull((Object)SecurityUtils.getAuthentication())) {
            this.tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken((Object)loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails((Object)new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication((Authentication)authenticationToken);
        }
        chain.doFilter((ServletRequest)request, (ServletResponse)response);
    }
}
