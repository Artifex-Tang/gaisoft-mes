package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.core.domain.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysDept
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long deptId;
    private Long parentId;
    private String ancestors;
    private String deptName;
    private Integer orderNum;
    private String leader;
    private String phone;
    private String email;
    private String status;
    private String delFlag;
    private String parentName;
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return this.ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message="部门名称不能为空")
    @Size(min=0, max=30, message="部门名称长度不能超过30个字符")
    public @NotBlank(message="部门名称不能为空") @Size(min=0, max=30, message="部门名称长度不能超过30个字符") String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotNull(message="显示顺序不能为空")
    public @NotNull(message="显示顺序不能为空") Integer getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return this.leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min=0, max=11, message="联系电话长度不能超过11个字符")
    public @Size(min=0, max=11, message="联系电话长度不能超过11个字符") String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message="邮箱格式不正确")
    @Size(min=0, max=50, message="邮箱长度不能超过50个字符")
    public @Email(message="邮箱格式不正确") @Size(min=0, max=50, message="邮箱长度不能超过50个字符") String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren() {
        return this.children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("deptId", (Object)this.getDeptId()).append("parentId", (Object)this.getParentId()).append("ancestors", (Object)this.getAncestors()).append("deptName", (Object)this.getDeptName()).append("orderNum", (Object)this.getOrderNum()).append("leader", (Object)this.getLeader()).append("phone", (Object)this.getPhone()).append("email", (Object)this.getEmail()).append("status", (Object)this.getStatus()).append("delFlag", (Object)this.getDelFlag()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).toString();
    }
}
