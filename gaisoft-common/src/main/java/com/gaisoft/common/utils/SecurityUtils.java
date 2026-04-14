package com.gaisoft.common.utils;

import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

public class SecurityUtils {
    public static Long getUserId() {
        try {
            return SecurityUtils.getLoginUser().getUserId();
        }
        catch (Exception e) {
            throw new ServiceException("获取用户ID异常", 401);
        }
    }

    public static Long getDeptId() {
        try {
            return SecurityUtils.getLoginUser().getDeptId();
        }
        catch (Exception e) {
            throw new ServiceException("获取部门ID异常", 401);
        }
    }

    public static String getUsername() {
        try {
            return SecurityUtils.getLoginUser().getUsername();
        }
        catch (Exception e) {
            throw new ServiceException("获取用户账户异常", 401);
        }
    }

    public static LoginUser getLoginUser() {
        try {
            return (LoginUser)SecurityUtils.getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new ServiceException("获取用户信息异常", 401);
        }
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode((CharSequence)password);
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches((CharSequence)rawPassword, encodedPassword);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public static boolean hasPermi(String permission) {
        return SecurityUtils.hasPermi(SecurityUtils.getLoginUser().getPermissions(), permission);
    }

    public static boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText).anyMatch(x -> "*:*:*".equals(x) || PatternMatchUtils.simpleMatch((String)x, (String)permission));
    }

    public static boolean hasRole(String role) {
        List<SysRole> roleList = SecurityUtils.getLoginUser().getUser().getRoles();
        Collection roles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        return SecurityUtils.hasRole(roles, role);
    }

    public static boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText).anyMatch(x -> "admin".equals(x) || PatternMatchUtils.simpleMatch((String)x, (String)role));
    }
}
