package com.gaisoft.system.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysPost
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="岗位序号", cellType=Excel.ColumnType.NUMERIC)
    private Long postId;
    @Excel(name="岗位编码")
    private String postCode;
    @Excel(name="岗位名称")
    private String postName;
    @Excel(name="岗位排序")
    private Integer postSort;
    @Excel(name="状态", readConverterExp="0=正常,1=停用")
    private String status;
    private boolean flag = false;

    public Long getPostId() {
        return this.postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @NotBlank(message="岗位编码不能为空")
    @Size(min=0, max=64, message="岗位编码长度不能超过64个字符")
    public @NotBlank(message="岗位编码不能为空") @Size(min=0, max=64, message="岗位编码长度不能超过64个字符") String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @NotBlank(message="岗位名称不能为空")
    @Size(min=0, max=50, message="岗位名称长度不能超过50个字符")
    public @NotBlank(message="岗位名称不能为空") @Size(min=0, max=50, message="岗位名称长度不能超过50个字符") String getPostName() {
        return this.postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @NotNull(message="显示顺序不能为空")
    public @NotNull(message="显示顺序不能为空") Integer getPostSort() {
        return this.postSort;
    }

    public void setPostSort(Integer postSort) {
        this.postSort = postSort;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("postId", (Object)this.getPostId()).append("postCode", (Object)this.getPostCode()).append("postName", (Object)this.getPostName()).append("postSort", (Object)this.getPostSort()).append("status", (Object)this.getStatus()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
