package com.gaisoft.framework.web.service;

import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.service.ISysMenuService;
import com.gaisoft.system.service.ISysRoleService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SysPermissionService {
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysMenuService menuService;

    public Set<String> getRolePermission(SysUser user) {
        HashSet<String> roles = new HashSet<String>();
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(this.roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    public Set<String> getMenuPermission(SysUser user) {
        HashSet<String> perms = new HashSet<String>();
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            @SuppressWarnings("unchecked") List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty((Collection)roles)) {
                for (SysRole role : roles) {
                    if (!StringUtils.equals((CharSequence)role.getStatus(), (CharSequence)"0") || role.isAdmin()) continue;
                    Set rolePerms = this.menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(this.menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
