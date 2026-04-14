package com.gaisoft.repair.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.repair.domain.IrIssueRecode;
import com.gaisoft.repair.service.IIrIssueRecodeService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping(value={"/repair/recode"})
public class IrIssueRecodeController
extends BaseController {
    @Autowired
    private IIrIssueRecodeService irIssueRecodeService;

    @PreAuthorize(value="@ss.hasPermi('repair:recode:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(IrIssueRecode irIssueRecode) {
        this.startPage();
        List<IrIssueRecode> list = this.irIssueRecodeService.selectIrIssueRecodeList(irIssueRecode);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('repair:recode:export')")
    @Log(title="售后问题登记", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, IrIssueRecode irIssueRecode) {
        List<IrIssueRecode> list = this.irIssueRecodeService.selectIrIssueRecodeList(irIssueRecode);
        ExcelUtil util = new ExcelUtil(IrIssueRecode.class);
        util.exportExcel(response, list, "售后问题登记数据");
    }

    @PreAuthorize(value="@ss.hasPermi('repair:recode:query')")
    @GetMapping(value={"/{issueId}"})
    public AjaxResult getInfo(@PathVariable(value="issueId") Long issueId) {
        return this.success((Object)this.irIssueRecodeService.selectIrIssueRecodeByIssueId(issueId));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:recode:add')")
    @Log(title="售后问题登记", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IrIssueRecode irIssueRecode) {
        return this.toAjax(this.irIssueRecodeService.insertIrIssueRecode(irIssueRecode));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:recode:edit')")
    @Log(title="售后问题登记", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IrIssueRecode irIssueRecode) {
        return this.toAjax(this.irIssueRecodeService.updateIrIssueRecode(irIssueRecode));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:recode:remove')")
    @Log(title="售后问题登记", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{issueIds}"})
    public AjaxResult remove(@PathVariable Long[] issueIds) {
        return this.toAjax(this.irIssueRecodeService.deleteIrIssueRecodeByIssueIds(issueIds));
    }
}
