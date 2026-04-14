package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.exception.user.BlackListException;
import com.gaisoft.common.exception.user.CaptchaException;
import com.gaisoft.common.exception.user.CaptchaExpireException;
import com.gaisoft.common.exception.user.UserNotExistsException;
import com.gaisoft.common.exception.user.UserPasswordNotMatchException;
import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.common.utils.MessageUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.ip.IpUtils;
import com.gaisoft.framework.manager.AsyncManager;
import com.gaisoft.framework.manager.factory.AsyncFactory;
import com.gaisoft.framework.security.context.AuthenticationContextHolder;
import com.gaisoft.framework.web.service.TokenService;
import com.gaisoft.system.service.ISysConfigService;
import com.gaisoft.system.service.ISysUserService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysConfigService configService;

    public String login(String username, String password, String code, String uuid) {
        this.validateCaptcha(username, code, uuid);
        this.loginPreCheck(username, password);
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken((Object)username, (Object)password);
            AuthenticationContextHolder.setContext((Authentication)authenticationToken);
            authentication = this.authenticationManager.authenticate((Authentication)authenticationToken);
        }
        catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"user.password.not.match", (Object[])new Object[0]), new Object[0]));
                throw new UserPasswordNotMatchException();
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", e.getMessage(), new Object[0]));
            throw new ServiceException(e.getMessage());
        }
        finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Success", MessageUtils.message((String)"user.login.success", (Object[])new Object[0]), new Object[0]));
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        this.recordLoginInfo(loginUser.getUserId());
        return this.tokenService.createToken(loginUser);
    }

    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = "captcha_codes:" + (String)StringUtils.nvl((Object)uuid, (Object)"");
            String captcha = (String)this.redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"user.jcaptcha.expire", (Object[])new Object[0]), new Object[0]));
                throw new CaptchaExpireException();
            }
            this.redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"user.jcaptcha.error", (Object[])new Object[0]), new Object[0]));
                throw new CaptchaException();
            }
        }
    }

    public void loginPreCheck(String username, String password) {
        if (StringUtils.isEmpty((String)username) || StringUtils.isEmpty((String)password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"not.null", (Object[])new Object[0]), new Object[0]));
            throw new UserNotExistsException();
        }
        if (password.length() < 5 || password.length() > 20) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"user.password.not.match", (Object[])new Object[0]), new Object[0]));
            throw new UserPasswordNotMatchException();
        }
        if (username.length() < 2 || username.length() > 20) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"user.password.not.match", (Object[])new Object[0]), new Object[0]));
            throw new UserPasswordNotMatchException();
        }
        String blackStr = this.configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp((String)blackStr, (String)IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message((String)"login.blocked", (Object[])new Object[0]), new Object[0]));
            throw new BlackListException();
        }
    }

    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        this.userService.updateUserProfile(sysUser);
    }
}
