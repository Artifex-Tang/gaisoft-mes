package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysRole
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="角色序号", cellType=Excel.ColumnType.NUMERIC)
    private Long roleId;
    @Excel(name="角色名称")
    private String roleName;
    @Excel(name="角色权限")
    private String roleKey;
    @Excel(name="角色排序")
    private Integer roleSort;
    @Excel(name="数据范围", readConverterExp="1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;
    private boolean menuCheckStrictly;
    private boolean deptCheckStrictly;
    @Excel(name="角色状态", readConverterExp="0=正常,1=停用")
    private String status;
    private String delFlag;
    private boolean flag = false;
    private Long[] menuIds;
    private Long[] deptIds;
    private Set<String> permissions;

    public SysRole() {
    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return SysRole.isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message="角色名称不能为空")
    @Size(min=0, max=30, message="角色名称长度不能超过30个字符")
    public @NotBlank(message="角色名称不能为空") @Size(min=0, max=30, message="角色名称长度不能超过30个字符") String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @NotBlank(message="权限字符不能为空")
    @Size(min=0, max=100, message="权限字符长度不能超过100个字符")
    public @NotBlank(message="权限字符不能为空") @Size(min=0, max=100, message="权限字符长度不能超过100个字符") String getRoleKey() {
        return this.roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    @NotNull(message="显示顺序不能为空")
    public @NotNull(message="显示顺序不能为空") Integer getRoleSort() {
        return this.roleSort;
    }

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public String getDataScope() {
        return this.dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public boolean isMenuCheckStrictly() {
        return this.menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly() {
        return this.deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long[] getMenuIds() {
        return this.menuIds;
    }

    public void setMenuIds(Long[] menuIds) {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds() {
        return this.deptIds;
    }

    public void setDeptIds(Long[] deptIds) {
        this.deptIds = deptIds;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("roleId", (Object)this.getRoleId()).append("roleName", (Object)this.getRoleName()).append("roleKey", (Object)this.getRoleKey()).append("roleSort", (Object)this.getRoleSort()).append("dataScope", (Object)this.getDataScope()).append("menuCheckStrictly", this.isMenuCheckStrictly()).append("deptCheckStrictly", this.isDeptCheckStrictly()).append("status", (Object)this.getStatus()).append("delFlag", (Object)this.getDelFlag()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
