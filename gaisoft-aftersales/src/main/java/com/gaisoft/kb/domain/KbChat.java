package com.gaisoft.kb.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class KbChat
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    @Excel(name="对话ID")
    private String messageId;
    @Excel(name="聊天窗_ID")
    private String chatId;
    @Excel(name="SESSION_ID")
    private String sessionId;
    @Excel(name="原始对话json字符")
    private String content;
    @Excel(name="打包原始对话json字符串后的 json字符串")
    private String packageContent;
    private String role;
    private String order;
    @Excel(name="大json字符串")
    private String reference;

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setPackageContent(String packageContent) {
        this.packageContent = packageContent;
    }

    public String getPackageContent() {
        return this.packageContent;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return this.reference;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("id", (Object)this.getId()).append("messageId", (Object)this.getMessageId()).append("chatId", (Object)this.getChatId()).append("sessionId", (Object)this.getSessionId()).append("content", (Object)this.getContent()).append("packageContent", (Object)this.getPackageContent()).append("reference", (Object)this.getReference()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).toString();
    }
}
