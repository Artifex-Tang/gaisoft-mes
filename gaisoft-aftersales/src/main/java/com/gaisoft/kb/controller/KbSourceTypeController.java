package com.gaisoft.kb.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbSourceType;
import com.gaisoft.kb.service.IKbSourceTypeService;
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
@RequestMapping(value={"/kb/type"})
public class KbSourceTypeController
extends BaseController {
    @Autowired
    private IKbSourceTypeService kbSourceTypeService;

    @GetMapping(value={"/list"})
    public AjaxResult list(KbSourceType kbSourceType) {
        List<KbSourceType> list = this.kbSourceTypeService.selectKbSourceTypeList(kbSourceType);
        return this.success(list);
    }

    @GetMapping(value={"/listAndDept"})
    public AjaxResult listAndDept(KbSourceType kbSourceType) {
        kbSourceType.setDeptId(this.getDeptId());
        List<KbSourceType> list = this.kbSourceTypeService.selectKbSourceTypeListAndDept(kbSourceType);
        return this.success(list);
    }

    @Log(title="文件分类", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbSourceType kbSourceType) {
        List<KbSourceType> list = this.kbSourceTypeService.selectKbSourceTypeList(kbSourceType);
        ExcelUtil util = new ExcelUtil(KbSourceType.class);
        util.exportExcel(response, list, "文件分类数据");
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getInfo(@PathVariable(value="id") Long id) {
        return this.success((Object)this.kbSourceTypeService.selectKbSourceTypeById(id));
    }

    @Log(title="文件分类", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbSourceType kbSourceType) {
        return this.toAjax(this.kbSourceTypeService.insertKbSourceType(kbSourceType));
    }

    @Log(title="文件分类", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbSourceType kbSourceType) {
        return this.toAjax(this.kbSourceTypeService.updateKbSourceType(kbSourceType));
    }

    @Log(title="文件分类", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{ids}"})
    public AjaxResult remove(@PathVariable Long[] ids) {
        return this.toAjax(this.kbSourceTypeService.deleteKbSourceTypeByIds(ids));
    }
}
