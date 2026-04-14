package com.gaisoft.system.domain;

import com.gaisoft.common.core.domain.BaseEntity;
import com.gaisoft.common.xss.Xss;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysNotice
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long noticeId;
    private String noticeTitle;
    private String noticeType;
    private String noticeContent;
    private String status;

    public Long getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    @Xss(message="公告标题不能包含脚本字符")
    @NotBlank(message="公告标题不能为空")
    @Size(min=0, max=50, message="公告标题不能超过50个字符")
    public @NotBlank(message="公告标题不能为空") @Size(min=0, max=50, message="公告标题不能超过50个字符") String getNoticeTitle() {
        return this.noticeTitle;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeType() {
        return this.noticeType;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() {
        return this.noticeContent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("noticeId", (Object)this.getNoticeId()).append("noticeTitle", (Object)this.getNoticeTitle()).append("noticeType", (Object)this.getNoticeType()).append("noticeContent", (Object)this.getNoticeContent()).append("status", (Object)this.getStatus()).append("createBy", (Object)this.getCreateBy()).append("createTime", (Object)this.getCreateTime()).append("updateBy", (Object)this.getUpdateBy()).append("updateTime", (Object)this.getUpdateTime()).append("remark", (Object)this.getRemark()).toString();
    }
}
