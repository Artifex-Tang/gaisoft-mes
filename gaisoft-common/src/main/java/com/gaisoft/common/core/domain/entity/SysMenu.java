package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.core.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysMenu
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long menuId;
    private String menuName;
    private String parentName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String query;
    private String routeName;
    private String isFrame;
    private String isCache;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @NotBlank(message="菜单名称不能为空")
    @Size(min=0, max=50, message="菜单名称长度不能超过50个字符")
    public @NotBlank(message="菜单名称不能为空") @Size(min=0, max=50, message="菜单名称长度不能超过50个字符") String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @NotNull(message="显示顺序不能为空")
    public @NotNull(message="显示顺序不能为空") Integer getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Size(min=0, max=200, message="路由地址不能超过200个字符")
    public @Size(min=0, max=200, message="路由地址不能超过200个字符") String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Size(min=0, max=200, message="组件路径不能超过255个字符")
    public @Size(min=0, max=200, message="组件路径不能超过255个字符") String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getIsFrame() {
        return this.isFrame;
    }

    public void setIsFrame(String isFrame) {
        this.isFrame = isFrame;
    }

    public String getIsCache() {
        return this.isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    @NotBlank(message="菜单类型不能为空")
    public @NotBlank(message="菜单类型不能为空") String getMenuType() {
        return this.menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getVisible() {
        return this.visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Size(min=0, max=100, message="权限标识长度不能超过100个字符")
    public @Size(min=0, max=100, message="权限标识长度不能超过100个字符") String getPerms() {
        return this.perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysMenu> getChildren() {
        return this.children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("menuId", (Object)this.getMenuId()).append("menuName", (Object)this.getMenuName()).append("parentId", (Object)this.getParentId()).append("orderNum", (Object)this.getOrderNum()).append("path", (Object)this.getPath()).append("component", (Object)this.getComponent()).append("query", (Object)this.getQuery()).append("routeName", (Object)this.getRouteName()).append("isFrame", (Object)this.getIsFrame()).append("IsCache", (Object)this.getIsCache()).append("menuType", (Object)this.getMenuType()).append("visible", (Object)this.getVisible()).append("status ", (Object)this.getStatus()).append("perms", (Object)this.getPerms()).append("icon", (Object)this.getIcon()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
