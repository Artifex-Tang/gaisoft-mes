package com.gaisoft.repair.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IrIssueRecode
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long issueId;
    @Excel(name="问题编码")
    private String issueCode;
    @Excel(name="问题名称")
    private String issueName;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name="问题接收时间", width=30.0, dateFormat="yyyy-MM-dd")
    private Date receiveTime;
    @Excel(name="接收人ID")
    private Long receiveId;
    @Excel(name="接收人")
    private String receiveBy;
    @Excel(name="接收人所属部门或公司")
    private String deptName;
    @Excel(name="问题反馈客户名称")
    private String customerName;
    @Excel(name="问题反馈人")
    private String contactPerson;
    @Excel(name="反馈人联系方式")
    private String contactPhone;
    @Excel(name="关联产品ID")
    private Long productId;
    @Excel(name="产品SN")
    private String productSn;
    @Excel(name="产品名称")
    private String productName;
    @Excel(name="产品型号")
    private String productSpec;
    @Excel(name="问题类别")
    private String issueType;
    @Excel(name="问题等级")
    private String issueLevel;
    @Excel(name="负责工程师")
    private String serviceEngineer;
    @Excel(name="问题设备SN码")
    private String deviceSn;
    @Excel(name="问题设备名称")
    private String deviceName;
    @Excel(name="问题状态(0:待处理,1:处理中,2:已解决,3:已关闭)")
    private String status;
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

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getIssueId() {
        return this.issueId;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode;
    }

    public String getIssueCode() {
        return this.issueCode;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueName() {
        return this.issueName;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public Long getReceiveId() {
        return this.receiveId;
    }

    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy;
    }

    public String getReceiveBy() {
        return this.receiveBy;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getProductSn() {
        return this.productSn;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }

    public String getProductSpec() {
        return this.productSpec;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueType() {
        return this.issueType;
    }

    public void setIssueLevel(String issueLevel) {
        this.issueLevel = issueLevel;
    }

    public String getIssueLevel() {
        return this.issueLevel;
    }

    public void setServiceEngineer(String serviceEngineer) {
        this.serviceEngineer = serviceEngineer;
    }

    public String getServiceEngineer() {
        return this.serviceEngineer;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
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
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("issueId", (Object)this.getIssueId()).append("issueCode", (Object)this.getIssueCode()).append("issueName", (Object)this.getIssueName()).append("receiveTime", (Object)this.getReceiveTime()).append("receiveId", (Object)this.getReceiveId()).append("receiveBy", (Object)this.getReceiveBy()).append("deptName", (Object)this.getDeptName()).append("customerName", (Object)this.getCustomerName()).append("contactPerson", (Object)this.getContactPerson()).append("contactPhone", (Object)this.getContactPhone()).append("productId", (Object)this.getProductId()).append("productSn", (Object)this.getProductSn()).append("productName", (Object)this.getProductName()).append("productSpec", (Object)this.getProductSpec()).append("issueType", (Object)this.getIssueType()).append("issueLevel", (Object)this.getIssueLevel()).append("serviceEngineer", (Object)this.getServiceEngineer()).append("deviceSn", (Object)this.getDeviceSn()).append("deviceName", (Object)this.getDeviceName()).append("status", (Object)this.getStatus()).append("remark", (Object)this.getRemark()).append("attr1", (Object)this.getAttr1()).append("attr2", (Object)this.getAttr2()).append("attr3", (Object)this.getAttr3()).append("attr4", (Object)this.getAttr4()).append("createId", (Object)this.getCreateId()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateId", (Object)this.getUpdateId()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).toString();
    }
}
