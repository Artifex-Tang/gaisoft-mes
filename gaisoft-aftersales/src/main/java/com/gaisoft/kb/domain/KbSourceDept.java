package com.gaisoft.kb.domain;

import com.gaisoft.common.core.domain.BaseEntity;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

public class KbSourceDept
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long sourceId;
    private Long deptId;
    private String ancestors;
    @Transient
    private List<String> ancestorss;
    @Transient
    private List<Long> sourceIds;

    public String getAncestors() {
        return this.ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public List<String> getAncestorss() {
        return this.ancestorss;
    }

    public void setAncestorss(List<String> ancestorss) {
        this.ancestorss = ancestorss;
    }

    public List<Long> getSourceIds() {
        return this.sourceIds;
    }

    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("sourceId", (Object)this.getSourceId()).append("deptId", (Object)this.getDeptId()).toString();
    }
}
