package com.gaisoft.repair.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RsRepairServiceProvider
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long providerId;
    @Excel(name="维修服务商编码")
    private String providerCode;
    @Excel(name="维修服务商名称")
    private String providerName;
    @Excel(name="维修服务商负责人")
    private String providerCharge;
    @Excel(name="维修服务商联系人")
    private String providerContactPerson;
    @Excel(name="维修服务商联系方式")
    private String providerContactPhone;
    @Excel(name="省ID")
    private Long provinceId;
    @Excel(name="省")
    private String province;
    @Excel(name="市ID")
    private Long cityId;
    @Excel(name="市")
    private String city;
    @Excel(name="区(县)ID")
    private Long districtId;
    @Excel(name="区(县)")
    private String district;
    @Excel(name="维修服务商归属区域")
    private String serviceRegion;
    @Excel(name="是否删除(0:正常,1:删除)")
    private String isDel;
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

    public void setProviderCharge(String providerCharge) {
        this.providerCharge = providerCharge;
    }

    public String getProviderCharge() {
        return this.providerCharge;
    }

    public void setProviderContactPerson(String providerContactPerson) {
        this.providerContactPerson = providerContactPerson;
    }

    public String getProviderContactPerson() {
        return this.providerContactPerson;
    }

    public void setProviderContactPhone(String providerContactPhone) {
        this.providerContactPhone = providerContactPhone;
    }

    public String getProviderContactPhone() {
        return this.providerContactPhone;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getProvinceId() {
        return this.provinceId;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return this.province;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getDistrictId() {
        return this.districtId;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setServiceRegion(String serviceRegion) {
        this.serviceRegion = serviceRegion;
    }

    public String getServiceRegion() {
        return this.serviceRegion;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getIsDel() {
        return this.isDel;
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
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("providerId", (Object)this.getProviderId()).append("providerCode", (Object)this.getProviderCode()).append("providerName", (Object)this.getProviderName()).append("providerCharge", (Object)this.getProviderCharge()).append("providerContactPerson", (Object)this.getProviderContactPerson()).append("providerContactPhone", (Object)this.getProviderContactPhone()).append("provinceId", (Object)this.getProvinceId()).append("province", (Object)this.getProvince()).append("cityId", (Object)this.getCityId()).append("city", (Object)this.getCity()).append("districtId", (Object)this.getDistrictId()).append("district", (Object)this.getDistrict()).append("serviceRegion", (Object)this.getServiceRegion()).append("isDel", (Object)this.getIsDel()).append("remark", (Object)this.getRemark()).append("attr1", (Object)this.getAttr1()).append("attr2", (Object)this.getAttr2()).append("attr3", (Object)this.getAttr3()).append("attr4", (Object)this.getAttr4()).append("createId", (Object)this.getCreateId()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateId", (Object)this.getUpdateId()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).toString();
    }
}
