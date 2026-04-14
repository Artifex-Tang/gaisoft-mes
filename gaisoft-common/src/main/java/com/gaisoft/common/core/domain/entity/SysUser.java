package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.annotation.Excels;
import com.gaisoft.common.core.domain.BaseEntity;
import com.gaisoft.common.core.domain.entity.SysDept;
import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.xss.Xss;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysUser
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="用户序号", type=Excel.Type.EXPORT, cellType=Excel.ColumnType.NUMERIC, prompt="用户编号")
    private Long userId;
    @Excel(name="部门编号", type=Excel.Type.IMPORT)
    private Long deptId;
    @Excel(name="登录名称")
    private String userName;
    @Excel(name="用户名称")
    private String nickName;
    @Excel(name="用户邮箱")
    private String email;
    @Excel(name="手机号码", cellType=Excel.ColumnType.TEXT)
    private String phonenumber;
    @Excel(name="用户性别", readConverterExp="0=男,1=女,2=未知")
    private String sex;
    private String avatar;
    private String password;
    @Excel(name="帐号状态", readConverterExp="0=正常,1=停用")
    private String status;
    private String delFlag;
    @Excel(name="最后登录IP", type=Excel.Type.EXPORT)
    private String loginIp;
    @Excel(name="最后登录时间", width=30.0, dateFormat="yyyy-MM-dd HH:mm:ss", type=Excel.Type.EXPORT)
    private Date loginDate;
    @Excels(value={@Excel(name="部门名称", targetAttr="deptName", type=Excel.Type.EXPORT), @Excel(name="部门负责人", targetAttr="leader", type=Excel.Type.EXPORT)})
    private SysDept dept;
    private List<SysRole> roles;
    private Long[] roleIds;
    private Long[] postIds;
    private Long roleId;

    public SysUser() {
    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return SysUser.isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Xss(message="用户昵称不能包含脚本字符")
    @Size(min=0, max=30, message="用户昵称长度不能超过30个字符")
    public @Size(min=0, max=30, message="用户昵称长度不能超过30个字符") String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Xss(message="用户账号不能包含脚本字符")
    @NotBlank(message="用户账号不能为空")
    @Size(min=0, max=30, message="用户账号长度不能超过30个字符")
    public @NotBlank(message="用户账号不能为空") @Size(min=0, max=30, message="用户账号长度不能超过30个字符") String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message="邮箱格式不正确")
    @Size(min=0, max=50, message="邮箱长度不能超过50个字符")
    public @Email(message="邮箱格式不正确") @Size(min=0, max=50, message="邮箱长度不能超过50个字符") String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min=0, max=11, message="手机号码长度不能超过11个字符")
    public @Size(min=0, max=11, message="手机号码长度不能超过11个字符") String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public SysDept getDept() {
        return this.dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return this.roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return this.roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return this.postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("userId", (Object)this.getUserId()).append("deptId", (Object)this.getDeptId()).append("userName", (Object)this.getUserName()).append("nickName", (Object)this.getNickName()).append("email", (Object)this.getEmail()).append("phonenumber", (Object)this.getPhonenumber()).append("sex", (Object)this.getSex()).append("avatar", (Object)this.getAvatar()).append("password", (Object)this.getPassword()).append("status", (Object)this.getStatus()).append("delFlag", (Object)this.getDelFlag()).append("loginIp", (Object)this.getLoginIp()).append("loginDate", (Object)this.getLoginDate()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).append("dept", (Object)this.getDept()).toString();
    }
}
