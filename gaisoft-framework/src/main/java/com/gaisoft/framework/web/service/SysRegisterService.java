package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.RegisterBody;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.exception.user.CaptchaException;
import com.gaisoft.common.exception.user.CaptchaExpireException;
import com.gaisoft.common.utils.MessageUtils;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.manager.AsyncManager;
import com.gaisoft.framework.manager.factory.AsyncFactory;
import com.gaisoft.system.service.ISysConfigService;
import com.gaisoft.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private RedisCache redisCache;

    public String register(RegisterBody registerBody) {
        String msg = "";
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            this.validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        if (StringUtils.isEmpty((String)username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty((String)password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < 2 || username.length() > 20) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < 5 || password.length() > 20) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!this.userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword((String)password));
            boolean regFlag = this.userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Register", MessageUtils.message((String)"user.register.success", (Object[])new Object[0]), new Object[0]));
            }
        }
        return msg;
    }

    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = "captcha_codes:" + (String)StringUtils.nvl((Object)uuid, (Object)"");
        String captcha = (String)this.redisCache.getCacheObject(verifyKey);
        this.redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
