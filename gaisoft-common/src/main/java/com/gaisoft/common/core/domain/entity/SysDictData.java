package com.gaisoft.common.core.domain.entity;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysDictData
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="字典编码", cellType=Excel.ColumnType.NUMERIC)
    private Long dictCode;
    @Excel(name="字典排序", cellType=Excel.ColumnType.NUMERIC)
    private Long dictSort;
    @Excel(name="字典标签")
    private String dictLabel;
    @Excel(name="字典键值")
    private String dictValue;
    @Excel(name="字典类型")
    private String dictType;
    private String cssClass;
    private String listClass;
    @Excel(name="是否默认", readConverterExp="Y=是,N=否")
    private String isDefault;
    @Excel(name="状态", readConverterExp="0=正常,1=停用")
    private String status;

    public Long getDictCode() {
        return this.dictCode;
    }

    public void setDictCode(Long dictCode) {
        this.dictCode = dictCode;
    }

    public Long getDictSort() {
        return this.dictSort;
    }

    public void setDictSort(Long dictSort) {
        this.dictSort = dictSort;
    }

    @NotBlank(message="字典标签不能为空")
    @Size(min=0, max=100, message="字典标签长度不能超过100个字符")
    public @NotBlank(message="字典标签不能为空") @Size(min=0, max=100, message="字典标签长度不能超过100个字符") String getDictLabel() {
        return this.dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message="字典键值不能为空")
    @Size(min=0, max=100, message="字典键值长度不能超过100个字符")
    public @NotBlank(message="字典键值不能为空") @Size(min=0, max=100, message="字典键值长度不能超过100个字符") String getDictValue() {
        return this.dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @NotBlank(message="字典类型不能为空")
    @Size(min=0, max=100, message="字典类型长度不能超过100个字符")
    public @NotBlank(message="字典类型不能为空") @Size(min=0, max=100, message="字典类型长度不能超过100个字符") String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    @Size(min=0, max=100, message="样式属性长度不能超过100个字符")
    public @Size(min=0, max=100, message="样式属性长度不能超过100个字符") String getCssClass() {
        return this.cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getListClass() {
        return this.listClass;
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public boolean getDefault() {
        return "Y".equals(this.isDefault);
    }

    public String getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("dictCode", (Object)this.getDictCode()).append("dictSort", (Object)this.getDictSort()).append("dictLabel", (Object)this.getDictLabel()).append("dictValue", (Object)this.getDictValue()).append("dictType", (Object)this.getDictType()).append("cssClass", (Object)this.getCssClass()).append("listClass", (Object)this.getListClass()).append("isDefault", (Object)this.getIsDefault()).append("status", (Object)this.getStatus()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
