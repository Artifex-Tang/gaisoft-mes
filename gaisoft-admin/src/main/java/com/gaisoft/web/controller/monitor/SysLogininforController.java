package com.gaisoft.web.controller.monitor;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.framework.web.service.SysPasswordService;
import com.gaisoft.system.domain.SysLogininfor;
import com.gaisoft.system.service.ISysLogininforService;
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
@RequestMapping(value={"/monitor/logininfor"})
public class SysLogininforController
extends BaseController {
    @Autowired
    private ISysLogininforService logininforService;
    @Autowired
    private SysPasswordService passwordService;

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysLogininfor logininfor) {
        this.startPage();
        List list = this.logininforService.selectLogininforList(logininfor);
        return this.getDataTable(list);
    }

    @Log(title="登录日志", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysLogininfor logininfor) {
        List list = this.logininforService.selectLogininforList(logininfor);
        ExcelUtil util = new ExcelUtil(SysLogininfor.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title="登录日志", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{infoIds}"})
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return this.toAjax(this.logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title="登录日志", businessType=BusinessType.CLEAN)
    @DeleteMapping(value={"/clean"})
    public AjaxResult clean() {
        this.logininforService.cleanLogininfor();
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:unlock')")
    @Log(title="账户解锁", businessType=BusinessType.OTHER)
    @GetMapping(value={"/unlock/{userName}"})
    public AjaxResult unlock(@PathVariable(value="userName") String userName) {
        this.passwordService.clearLoginRecordCache(userName);
        return this.success();
    }
}
