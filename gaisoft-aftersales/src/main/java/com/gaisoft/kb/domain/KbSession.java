package com.gaisoft.kb.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class KbSession
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private String sessionId;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private String sessionName;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private String chatId;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private Date createDate;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private Date updateDate;
    @Excel(name="${comment}", readConverterExp="$column.readConverterExp()")
    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("id", (Object)this.getId()).append("sessionId", (Object)this.getSessionId()).append("sessionName", (Object)this.getSessionName()).append("chatId", (Object)this.getChatId()).append("createDate", (Object)this.getCreateDate()).append("createTime", (Object)this.getCreateTime()).append("updateDate", (Object)this.getUpdateDate()).append("updateTime", (Object)this.getUpdateTime()).append("userId", (Object)this.getUserId()).toString();
    }
}
