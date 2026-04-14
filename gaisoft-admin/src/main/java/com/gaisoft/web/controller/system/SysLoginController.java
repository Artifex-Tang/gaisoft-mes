package com.gaisoft.web.controller.system;

import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginBody;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.framework.web.service.SysLoginService;
import com.gaisoft.framework.web.service.SysPermissionService;
import com.gaisoft.framework.web.service.TokenService;
import com.gaisoft.system.service.ISysMenuService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private TokenService tokenService;

    @PostMapping(value={"/login"})
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String token = this.loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        ajax.put("token", (Object)token);
        return ajax;
    }

    @GetMapping(value={"getInfo"})
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        Set roles = this.permissionService.getRolePermission(user);
        Set permissions = this.permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            this.tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", (Object)user);
        ajax.put("roles", (Object)roles);
        ajax.put("permissions", (Object)permissions);
        return ajax;
    }

    @GetMapping(value={"getRouters"})
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List menus = this.menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success((Object)this.menuService.buildMenus(menus));
    }
}
