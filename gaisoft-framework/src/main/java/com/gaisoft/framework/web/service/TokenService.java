package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.core.redis.RedisCache;
import com.gaisoft.common.utils.ServletUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.ip.AddressUtils;
import com.gaisoft.common.utils.ip.IpUtils;
import com.gaisoft.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    @Value(value="${token.header}")
    private String header;
    @Value(value="${token.secret}")
    private String secret;
    @Value(value="${token.expireTime}")
    private int expireTime;
    protected static final long MILLIS_SECOND = 1000L;
    protected static final long MILLIS_MINUTE = 60000L;
    private static final Long MILLIS_MINUTE_TWENTY = 1200000L;
    @Autowired
    private RedisCache redisCache;

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = this.getToken(request);
        if (StringUtils.isNotEmpty((String)token)) {
            try {
                Claims claims = this.parseToken(token);
                String uuid = (String)claims.get((Object)"login_user_key");
                String userKey = this.getTokenKey(uuid);
                LoginUser user = (LoginUser)this.redisCache.getCacheObject(userKey);
                return user;
            }
            catch (Exception e) {
                log.error("获取用户信息异常'{}'", (Object)e.getMessage());
            }
        }
        return null;
    }

    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull((Object)loginUser) && StringUtils.isNotEmpty((String)loginUser.getToken())) {
            this.refreshToken(loginUser);
        }
    }

    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty((String)token)) {
            String userKey = this.getTokenKey(token);
            this.redisCache.deleteObject(userKey);
        }
    }

    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        this.setUserAgent(loginUser);
        this.refreshToken(loginUser);
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("login_user_key", token);
        claims.put("sub", loginUser.getUsername());
        return this.createToken(claims);
    }

    public void verifyToken(LoginUser loginUser) {
        long currentTime;
        long expireTime = loginUser.getExpireTime();
        if (expireTime - (currentTime = System.currentTimeMillis()) <= MILLIS_MINUTE_TWENTY) {
            this.refreshToken(loginUser);
        }
    }

    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(Long.valueOf(System.currentTimeMillis()));
        loginUser.setExpireTime(Long.valueOf(loginUser.getLoginTime() + (long)this.expireTime * 60000L));
        String userKey = this.getTokenKey(loginUser.getToken());
        this.redisCache.setCacheObject(userKey, (Object)loginUser, Integer.valueOf(this.expireTime), TimeUnit.MINUTES);
    }

    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString((String)ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP((String)ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, this.secret).compact();
        return token;
    }

    private Claims parseToken(String token) {
        return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = this.parseToken(token);
        return claims.getSubject();
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(this.header);
        if (StringUtils.isNotEmpty((String)token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return "login_tokens:" + uuid;
    }
}
