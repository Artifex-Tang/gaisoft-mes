package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysMenu;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.service.ISysMenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/menu"})
public class SysMenuController
extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    @PreAuthorize(value="@ss.hasPermi('system:menu:list')")
    @GetMapping(value={"/list"})
    public AjaxResult list(SysMenu menu) {
        List menus = this.menuService.selectMenuList(menu, this.getUserId());
        return this.success(menus);
    }

    @PreAuthorize(value="@ss.hasPermi('system:menu:query')")
    @GetMapping(value={"/{menuId}"})
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return this.success(this.menuService.selectMenuById(menuId));
    }

    @GetMapping(value={"/treeselect"})
    public AjaxResult treeselect(SysMenu menu) {
        List menus = this.menuService.selectMenuList(menu, this.getUserId());
        return this.success(this.menuService.buildMenuTreeSelect(menus));
    }

    @GetMapping(value={"/roleMenuTreeselect/{roleId}"})
    public AjaxResult roleMenuTreeselect(@PathVariable(value="roleId") Long roleId) {
        List menus = this.menuService.selectMenuList(this.getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", (Object)this.menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", (Object)this.menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    @PreAuthorize(value="@ss.hasPermi('system:menu:add')")
    @Log(title="菜单管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!this.menuService.checkMenuNameUnique(menu)) {
            return this.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if ("0".equals(menu.getIsFrame()) && !StringUtils.ishttp((String)menu.getPath())) {
            return this.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(this.getUsername());
        return this.toAjax(this.menuService.insertMenu(menu));
    }

    @PreAuthorize(value="@ss.hasPermi('system:menu:edit')")
    @Log(title="菜单管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!this.menuService.checkMenuNameUnique(menu)) {
            return this.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if ("0".equals(menu.getIsFrame()) && !StringUtils.ishttp((String)menu.getPath())) {
            return this.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        if (menu.getMenuId().equals(menu.getParentId())) {
            return this.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(this.getUsername());
        return this.toAjax(this.menuService.updateMenu(menu));
    }

    @PreAuthorize(value="@ss.hasPermi('system:menu:remove')")
    @Log(title="菜单管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{menuId}"})
    public AjaxResult remove(@PathVariable(value="menuId") Long menuId) {
        if (this.menuService.hasChildByMenuId(menuId)) {
            return this.warn("存在子菜单,不允许删除");
        }
        if (this.menuService.checkMenuExistRole(menuId)) {
            return this.warn("菜单已分配,不允许删除");
        }
        return this.toAjax(this.menuService.deleteMenuById(menuId));
    }
}
