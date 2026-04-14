package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.exception.user.UserPasswordNotMatchException;
import com.gaisoft.common.exception.user.UserPasswordRetryLimitExceedException;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.framework.security.context.AuthenticationContextHolder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SysPasswordService {
    @Autowired
    private RedisCache redisCache;
    @Value(value="${user.password.maxRetryCount}")
    private int maxRetryCount;
    @Value(value="${user.password.lockTime}")
    private int lockTime;

    private String getCacheKey(String username) {
        return "pwd_err_cnt:" + username;
    }

    public void validate(SysUser user) {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
        Integer retryCount = (Integer)this.redisCache.getCacheObject(this.getCacheKey(username));
        if (retryCount == null) {
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(this.maxRetryCount)) {
            throw new UserPasswordRetryLimitExceedException(this.maxRetryCount, this.lockTime);
        }
        if (!this.matches(user, password)) {
            retryCount = retryCount + 1;
            this.redisCache.setCacheObject(this.getCacheKey(username), (Object)retryCount, Integer.valueOf(this.lockTime), TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        this.clearLoginRecordCache(username);
    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword((String)rawPassword, (String)user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (this.redisCache.hasKey(this.getCacheKey(loginName)).booleanValue()) {
            this.redisCache.deleteObject(this.getCacheKey(loginName));
        }
    }
}
