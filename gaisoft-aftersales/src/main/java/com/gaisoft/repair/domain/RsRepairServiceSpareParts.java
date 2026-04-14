package com.gaisoft.repair.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RsRepairServiceSpareParts
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long sparePartId;
    @Excel(name="维修服务商ID")
    private Long providerId;
    @Excel(name="维修服务商编码")
    private String providerCode;
    @Excel(name="维修服务商名称")
    private String providerName;
    @Excel(name="维修服务商仓库编码")
    private String warehouseCode;
    @Excel(name="维修服务商仓库名称")
    private String warehouseName;
    @Excel(name="备件名称")
    private String sparePartName;
    @Excel(name="备件单位")
    private String sparePartUnit;
    @Excel(name="备件型号")
    private String sparePartSpec;
    @Excel(name="备件编码")
    private String sparePartCode;
    @Excel(name="调拨人")
    private String transferBy;
    @Excel(name="调拨人联系方式")
    private String transferPhone;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name="调拨时间", width=30.0, dateFormat="yyyy-MM-dd")
    private Date transferTime;
    @Excel(name="接收人")
    private String receiveBy;
    @Excel(name="接收人联系方式")
    private String receivePhone;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name="接收时间", width=30.0, dateFormat="yyyy-MM-dd")
    private Date receiveTime;
    @Excel(name="备用字段1")
    private String attr1;
    @Excel(name="备用字段2")
    private String attr2;
    @Excel(name="备用字段3")
    private Long attr3;
    @Excel(name="备用字段4")
    private Long attr4;
    @Excel(name="创建人id")
    private Long createId;
    @Excel(name="修改人id")
    private Long updateId;

    public void setSparePartId(Long sparePartId) {
        this.sparePartId = sparePartId;
    }

    public Long getSparePartId() {
        return this.sparePartId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getProviderId() {
        return this.providerId;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderCode() {
        return this.providerCode;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseCode() {
        return this.warehouseCode;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseName() {
        return this.warehouseName;
    }

    public void setSparePartName(String sparePartName) {
        this.sparePartName = sparePartName;
    }

    public String getSparePartName() {
        return this.sparePartName;
    }

    public void setSparePartUnit(String sparePartUnit) {
        this.sparePartUnit = sparePartUnit;
    }

    public String getSparePartUnit() {
        return this.sparePartUnit;
    }

    public void setSparePartSpec(String sparePartSpec) {
        this.sparePartSpec = sparePartSpec;
    }

    public String getSparePartSpec() {
        return this.sparePartSpec;
    }

    public void setSparePartCode(String sparePartCode) {
        this.sparePartCode = sparePartCode;
    }

    public String getSparePartCode() {
        return this.sparePartCode;
    }

    public void setTransferBy(String transferBy) {
        this.transferBy = transferBy;
    }

    public String getTransferBy() {
        return this.transferBy;
    }

    public void setTransferPhone(String transferPhone) {
        this.transferPhone = transferPhone;
    }

    public String getTransferPhone() {
        return this.transferPhone;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public Date getTransferTime() {
        return this.transferTime;
    }

    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy;
    }

    public String getReceiveBy() {
        return this.receiveBy;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceivePhone() {
        return this.receivePhone;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr1() {
        return this.attr1;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr2() {
        return this.attr2;
    }

    public void setAttr3(Long attr3) {
        this.attr3 = attr3;
    }

    public Long getAttr3() {
        return this.attr3;
    }

    public void setAttr4(Long attr4) {
        this.attr4 = attr4;
    }

    public Long getAttr4() {
        return this.attr4;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getCreateId() {
        return this.createId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Long getUpdateId() {
        return this.updateId;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("sparePartId", (Object)this.getSparePartId()).append("providerId", (Object)this.getProviderId()).append("providerCode", (Object)this.getProviderCode()).append("providerName", (Object)this.getProviderName()).append("warehouseCode", (Object)this.getWarehouseCode()).append("warehouseName", (Object)this.getWarehouseName()).append("sparePartName", (Object)this.getSparePartName()).append("sparePartUnit", (Object)this.getSparePartUnit()).append("sparePartSpec", (Object)this.getSparePartSpec()).append("sparePartCode", (Object)this.getSparePartCode()).append("transferBy", (Object)this.getTransferBy()).append("transferPhone", (Object)this.getTransferPhone()).append("transferTime", (Object)this.getTransferTime()).append("receiveBy", (Object)this.getReceiveBy()).append("receivePhone", (Object)this.getReceivePhone()).append("receiveTime", (Object)this.getReceiveTime()).append("remark", (Object)this.getRemark()).append("attr1", (Object)this.getAttr1()).append("attr2", (Object)this.getAttr2()).append("attr3", (Object)this.getAttr3()).append("attr4", (Object)this.getAttr4()).append("createId", (Object)this.getCreateId()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateId", (Object)this.getUpdateId()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).toString();
    }
}
