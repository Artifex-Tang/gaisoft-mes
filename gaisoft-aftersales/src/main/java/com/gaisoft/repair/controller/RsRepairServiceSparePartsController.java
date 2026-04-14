package com.gaisoft.repair.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.repair.domain.RsRepairServiceSpareParts;
import com.gaisoft.repair.service.IRsRepairServiceSparePartsService;
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
@RequestMapping(value={"/repair/provider"})
public class RsRepairServiceSparePartsController
extends BaseController {
    @Autowired
    private IRsRepairServiceSparePartsService rsRepairServiceSparePartsService;

    @PreAuthorize(value="@ss.hasPermi('repair:provider:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        this.startPage();
        List<RsRepairServiceSpareParts> list = this.rsRepairServiceSparePartsService.selectRsRepairServiceSparePartsList(rsRepairServiceSpareParts);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('repair:provider:export')")
    @Log(title="维修服务商备件管理", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        List<RsRepairServiceSpareParts> list = this.rsRepairServiceSparePartsService.selectRsRepairServiceSparePartsList(rsRepairServiceSpareParts);
        ExcelUtil util = new ExcelUtil(RsRepairServiceSpareParts.class);
        util.exportExcel(response, list, "维修服务商备件管理数据");
    }

    @PreAuthorize(value="@ss.hasPermi('repair:provider:query')")
    @GetMapping(value={"/{sparePartId}"})
    public AjaxResult getInfo(@PathVariable(value="sparePartId") Long sparePartId) {
        return this.success((Object)this.rsRepairServiceSparePartsService.selectRsRepairServiceSparePartsBySparePartId(sparePartId));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:provider:add')")
    @Log(title="维修服务商备件管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        return this.toAjax(this.rsRepairServiceSparePartsService.insertRsRepairServiceSpareParts(rsRepairServiceSpareParts));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:provider:edit')")
    @Log(title="维修服务商备件管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RsRepairServiceSpareParts rsRepairServiceSpareParts) {
        return this.toAjax(this.rsRepairServiceSparePartsService.updateRsRepairServiceSpareParts(rsRepairServiceSpareParts));
    }

    @PreAuthorize(value="@ss.hasPermi('repair:provider:remove')")
    @Log(title="维修服务商备件管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{sparePartIds}"})
    public AjaxResult remove(@PathVariable Long[] sparePartIds) {
        return this.toAjax(this.rsRepairServiceSparePartsService.deleteRsRepairServiceSparePartsBySparePartIds(sparePartIds));
    }
}
