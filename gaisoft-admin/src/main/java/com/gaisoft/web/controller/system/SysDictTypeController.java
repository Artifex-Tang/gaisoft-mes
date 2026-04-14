package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysDictType;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.service.ISysDictTypeService;
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
@RequestMapping(value={"/system/dict/type"})
public class SysDictTypeController
extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize(value="@ss.hasPermi('system:dict:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysDictType dictType) {
        this.startPage();
        List list = this.dictTypeService.selectDictTypeList(dictType);
        return this.getDataTable(list);
    }

    @Log(title="字典类型", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('system:dict:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysDictType dictType) {
        List list = this.dictTypeService.selectDictTypeList(dictType);
        ExcelUtil util = new ExcelUtil(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:query')")
    @GetMapping(value={"/{dictId}"})
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return this.success(this.dictTypeService.selectDictTypeById(dictId));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:add')")
    @Log(title="字典类型", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!this.dictTypeService.checkDictTypeUnique(dict)) {
            return this.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(this.getUsername());
        return this.toAjax(this.dictTypeService.insertDictType(dict));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:edit')")
    @Log(title="字典类型", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (!this.dictTypeService.checkDictTypeUnique(dict)) {
            return this.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(this.getUsername());
        return this.toAjax(this.dictTypeService.updateDictType(dict));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:remove')")
    @Log(title="字典类型", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{dictIds}"})
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        this.dictTypeService.deleteDictTypeByIds(dictIds);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:remove')")
    @Log(title="字典类型", businessType=BusinessType.CLEAN)
    @DeleteMapping(value={"/refreshCache"})
    public AjaxResult refreshCache() {
        this.dictTypeService.resetDictCache();
        return this.success();
    }

    @GetMapping(value={"/optionselect"})
    public AjaxResult optionselect() {
        List dictTypes = this.dictTypeService.selectDictTypeAll();
        return this.success(dictTypes);
    }
}
