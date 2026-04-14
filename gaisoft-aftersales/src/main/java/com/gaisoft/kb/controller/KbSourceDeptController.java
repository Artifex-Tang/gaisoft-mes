package com.gaisoft.kb.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbSourceDept;
import com.gaisoft.kb.service.IKbSourceDeptService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/kb/dept"})
public class KbSourceDeptController
extends BaseController {
    @Autowired
    private IKbSourceDeptService kbSourceDeptService;

    @GetMapping(value={"/list"})
    public TableDataInfo list(KbSourceDept kbSourceDept) {
        this.startPage();
        List<KbSourceDept> list = this.kbSourceDeptService.selectKbSourceDeptList(kbSourceDept);
        return this.getDataTable(list);
    }

    @Log(title="gl", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbSourceDept kbSourceDept) {
        List<KbSourceDept> list = this.kbSourceDeptService.selectKbSourceDeptList(kbSourceDept);
        ExcelUtil util = new ExcelUtil(KbSourceDept.class);
        util.exportExcel(response, list, "gl数据");
    }

    @GetMapping(value={"/{sourceId}"})
    public AjaxResult getInfo(@PathVariable(value="sourceId") Long sourceId) {
        return this.success((Object)this.kbSourceDeptService.selectKbSourceDeptBySourceId(sourceId));
    }

    @Log(title="gl", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbSourceDept kbSourceDept) {
        return this.toAjax(this.kbSourceDeptService.insertKbSourceDept(kbSourceDept));
    }

    @Log(title="gl", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbSourceDept kbSourceDept) {
        return this.toAjax(this.kbSourceDeptService.updateKbSourceDept(kbSourceDept));
    }

    @Log(title="gl", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{sourceIds}"})
    public AjaxResult remove(@PathVariable Long[] sourceIds) {
        return this.toAjax(this.kbSourceDeptService.deleteKbSourceDeptBySourceIds(sourceIds));
    }
}
