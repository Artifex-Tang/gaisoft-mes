package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysDictType
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="字典主键", cellType=Excel.ColumnType.NUMERIC)
    private Long dictId;
    @Excel(name="字典名称")
    private String dictName;
    @Excel(name="字典类型")
    private String dictType;
    @Excel(name="状态", readConverterExp="0=正常,1=停用")
    private String status;

    public Long getDictId() {
        return this.dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    @NotBlank(message="字典名称不能为空")
    @Size(min=0, max=100, message="字典类型名称长度不能超过100个字符")
    public @NotBlank(message="字典名称不能为空") @Size(min=0, max=100, message="字典类型名称长度不能超过100个字符") String getDictName() {
        return this.dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @NotBlank(message="字典类型不能为空")
    @Size(min=0, max=100, message="字典类型类型长度不能超过100个字符")
    @Pattern(regexp="^[a-z][a-z0-9_]*$", message="字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    public @NotBlank(message="字典类型不能为空") @Size(min=0, max=100, message="字典类型类型长度不能超过100个字符") @Pattern(regexp="^[a-z][a-z0-9_]*$", message="字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）") String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("dictId", (Object)this.getDictId()).append("dictName", (Object)this.getDictName()).append("dictType", (Object)this.getDictType()).append("status", (Object)this.getStatus()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
