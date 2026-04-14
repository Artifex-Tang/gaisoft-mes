package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysDictData;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.service.ISysDictDataService;
import com.gaisoft.system.service.ISysDictTypeService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/dict/data"})
public class SysDictDataController
extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize(value="@ss.hasPermi('system:dict:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysDictData dictData) {
        this.startPage();
        List list = this.dictDataService.selectDictDataList(dictData);
        return this.getDataTable(list);
    }

    @Log(title="字典数据", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('system:dict:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysDictData dictData) {
        List list = this.dictDataService.selectDictDataList(dictData);
        ExcelUtil util = new ExcelUtil(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:query')")
    @GetMapping(value={"/{dictCode}"})
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return this.success(this.dictDataService.selectDictDataById(dictCode));
    }

    @GetMapping(value={"/type/{dictType}"})
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = this.dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull((Object)data)) {
            data = new ArrayList();
        }
        return this.success(data);
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:add')")
    @Log(title="字典数据", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(this.getUsername());
        return this.toAjax(this.dictDataService.insertDictData(dict));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:edit')")
    @Log(title="字典数据", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(this.getUsername());
        return this.toAjax(this.dictDataService.updateDictData(dict));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:remove')")
    @Log(title="字典类型", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{dictCodes}"})
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        this.dictDataService.deleteDictDataByIds(dictCodes);
        return this.success();
    }
}
