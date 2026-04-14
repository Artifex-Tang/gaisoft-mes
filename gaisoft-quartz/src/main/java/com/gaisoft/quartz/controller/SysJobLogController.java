package com.gaisoft.quartz.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.quartz.domain.SysJobLog;
import com.gaisoft.quartz.service.ISysJobLogService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/jobLog"})
public class SysJobLogController
extends BaseController {
    @Autowired
    private ISysJobLogService jobLogService;

    @PreAuthorize(value="@ss.hasPermi('monitor:job:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysJobLog sysJobLog) {
        this.startPage();
        List<SysJobLog> list = this.jobLogService.selectJobLogList(sysJobLog);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:export')")
    @Log(title="任务调度日志", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysJobLog sysJobLog) {
        List<SysJobLog> list = this.jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil util = new ExcelUtil(SysJobLog.class);
        util.exportExcel(response, list, "调度日志");
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:query')")
    @GetMapping(value={"/{jobLogId}"})
    public AjaxResult getInfo(@PathVariable Long jobLogId) {
        return this.success((Object)this.jobLogService.selectJobLogById(jobLogId));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:remove')")
    @Log(title="定时任务调度日志", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{jobLogIds}"})
    public AjaxResult remove(@PathVariable Long[] jobLogIds) {
        return this.toAjax(this.jobLogService.deleteJobLogByIds(jobLogIds));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:job:remove')")
    @Log(title="调度日志", businessType=BusinessType.CLEAN)
    @DeleteMapping(value={"/clean"})
    public AjaxResult clean() {
        this.jobLogService.cleanJobLog();
        return this.success();
    }
}
