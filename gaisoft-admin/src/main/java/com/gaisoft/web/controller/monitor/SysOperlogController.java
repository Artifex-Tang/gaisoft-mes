package com.gaisoft.web.controller.monitor;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.domain.SysOperLog;
import com.gaisoft.system.service.ISysOperLogService;
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
@RequestMapping(value={"/monitor/operlog"})
public class SysOperlogController
extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysOperLog operLog) {
        this.startPage();
        List list = this.operLogService.selectOperLogList(operLog);
        return this.getDataTable(list);
    }

    @Log(title="操作日志", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List list = this.operLogService.selectOperLogList(operLog);
        ExcelUtil util = new ExcelUtil(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Log(title="操作日志", businessType=BusinessType.DELETE)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping(value={"/{operIds}"})
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return this.toAjax(this.operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title="操作日志", businessType=BusinessType.CLEAN)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping(value={"/clean"})
    public AjaxResult clean() {
        this.operLogService.cleanOperLog();
        return this.success();
    }
}
