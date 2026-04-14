package com.gaisoft.kb.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import com.gaisoft.kb.domain.KbSourceType;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class KbSourceFile
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name="文件名称")
    private String name;
    @Excel(name="文件类型")
    private Long typeId;
    private String tenantId;
    private String parentId;
    private String sourceType;
    private String type;
    @Excel(name="文件大小")
    private String size;
    private List<KbSourceType> kbSourceTypeList;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getTypeId() {
        return this.typeId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    public List<KbSourceType> getKbSourceTypeList() {
        return this.kbSourceTypeList;
    }

    public void setKbSourceTypeList(List<KbSourceType> kbSourceTypeList) {
        this.kbSourceTypeList = kbSourceTypeList;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("id", (Object)this.getId()).append("name", (Object)this.getName()).append("sourceType", (Object)this.getSourceType()).append("typeId", (Object)this.getTypeId()).append("tenantId", (Object)this.getTenantId()).append("parentId", (Object)this.getParentId()).append("size", (Object)this.getSize()).append("createTime", (Object)this.getCreateTime()).append("createBy", (Object)this.getCreateBy()).append("kbSourceTypeList", this.getKbSourceTypeList()).toString();
    }
}
