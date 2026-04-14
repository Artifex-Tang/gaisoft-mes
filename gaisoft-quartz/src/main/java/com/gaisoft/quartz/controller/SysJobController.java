package com.gaisoft.quartz.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.constant.Constants;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.exception.job.TaskException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.quartz.domain.SysJob;
import com.gaisoft.quartz.service.ISysJobService;
import com.gaisoft.quartz.util.CronUtils;
import com.gaisoft.quartz.util.ScheduleUtils;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/job"})
public class SysJobController
extends BaseController {
    @Autowired
    private ISysJobService jobService;

    @PreAuthorize(value="@ss.hasPermi('monitor:job:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysJob sysJob) {
        this.startPage();
        List<SysJob> list = this.jobService.selectJobList(sysJob);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:export')")
    @Log(title="定时任务", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysJob sysJob) {
        List<SysJob> list = this.jobService.selectJobList(sysJob);
        ExcelUtil util = new ExcelUtil(SysJob.class);
        util.exportExcel(response, list, "定时任务");
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:query')")
    @GetMapping(value={"/{jobId}"})
    public AjaxResult getInfo(@PathVariable(value="jobId") Long jobId) {
        return this.success((Object)this.jobService.selectJobById(jobId));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:add')")
    @Log(title="定时任务", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return this.error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        if (StringUtils.containsIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence)"rmi:")) {
            return this.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])new String[]{"ldap:", "ldaps:"})) {
            return this.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])new String[]{"http://", "https://"})) {
            return this.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])Constants.JOB_ERROR_STR)) {
            return this.error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return this.error("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setCreateBy(this.getUsername());
        return this.toAjax(this.jobService.insertJob(job));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:edit')")
    @Log(title="定时任务", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return this.error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        if (StringUtils.containsIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence)"rmi:")) {
            return this.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])new String[]{"ldap:", "ldaps:"})) {
            return this.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])new String[]{"http://", "https://"})) {
            return this.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        if (StringUtils.containsAnyIgnoreCase((CharSequence)job.getInvokeTarget(), (CharSequence[])Constants.JOB_ERROR_STR)) {
            return this.error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return this.error("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setUpdateBy(this.getUsername());
        return this.toAjax(this.jobService.updateJob(job));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title="定时任务", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/changeStatus"})
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException {
        SysJob newJob = this.jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return this.toAjax(this.jobService.changeStatus(newJob));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title="定时任务", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/run"})
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException {
        boolean result = this.jobService.run(job);
        return result ? this.success() : this.error("任务不存在或已过期！");
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:remove')")
    @Log(title="定时任务", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{jobIds}"})
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException {
        this.jobService.deleteJobByIds(jobIds);
        return this.success();
    }
}
