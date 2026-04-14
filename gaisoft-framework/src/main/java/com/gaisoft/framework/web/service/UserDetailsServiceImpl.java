package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.enums.UserStatus;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.MessageUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.web.service.SysPasswordService;
import com.gaisoft.framework.web.service.SysPermissionService;
import com.gaisoft.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl
implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private SysPermissionService permissionService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = this.userService.selectUserByUserName(username);
        if (StringUtils.isNull((Object)user)) {
            log.info("登录用户：{} 不存在.", (Object)username);
            throw new ServiceException(MessageUtils.message((String)"user.not.exists", (Object[])new Object[0]));
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", (Object)username);
            throw new ServiceException(MessageUtils.message((String)"user.password.delete", (Object[])new Object[0]));
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", (Object)username);
            throw new ServiceException(MessageUtils.message((String)"user.blocked", (Object[])new Object[0]));
        }
        this.passwordService.validate(user);
        return this.createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, this.permissionService.getMenuPermission(user));
    }
}
