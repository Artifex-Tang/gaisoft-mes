package com.gaisoft.quartz.domain;

import com.gaisoft.common.annotation.Excel;
import com.gaisoft.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysJobLog
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="日志序号")
    private Long jobLogId;
    @Excel(name="任务名称")
    private String jobName;
    @Excel(name="任务组名")
    private String jobGroup;
    @Excel(name="调用目标字符串")
    private String invokeTarget;
    @Excel(name="日志信息")
    private String jobMessage;
    @Excel(name="执行状态", readConverterExp="0=正常,1=失败")
    private String status;
    @Excel(name="异常信息")
    private String exceptionInfo;
    private Date startTime;
    private Date stopTime;

    public Long getJobLogId() {
        return this.jobLogId;
    }

    public void setJobLogId(Long jobLogId) {
        this.jobLogId = jobLogId;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget() {
        return this.invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getJobMessage() {
        return this.jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExceptionInfo() {
        return this.exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String toString() {
        return new ToStringBuilder((Object)this, ToStringStyle.MULTI_LINE_STYLE).append("jobLogId", (Object)this.getJobLogId()).append("jobName", (Object)this.getJobName()).append("jobGroup", (Object)this.getJobGroup()).append("jobMessage", (Object)this.getJobMessage()).append("status", (Object)this.getStatus()).append("exceptionInfo", (Object)this.getExceptionInfo()).append("startTime", (Object)this.getStartTime()).append("stopTime", (Object)this.getStopTime()).toString();
    }
}
