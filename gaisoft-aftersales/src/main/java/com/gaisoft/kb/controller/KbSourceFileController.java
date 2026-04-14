package com.gaisoft.kb.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbSourceFile;
import com.gaisoft.kb.service.IKbSourceFileService;
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
@RequestMapping(value={"/kb/file"})
public class KbSourceFileController
extends BaseController {
    @Autowired
    private IKbSourceFileService kbSourceFileService;

    @PreAuthorize(value="@ss.hasPermi('kb:file:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(KbSourceFile kbSourceFile) {
        this.startPage();
        List<KbSourceFile> list = this.kbSourceFileService.selectKbSourceFileList(kbSourceFile);
        return this.getDataTable(list);
    }

    @PostMapping(value={"/listByIds"})
    public TableDataInfo listByIds(@RequestBody List<String> ids) {
        List<KbSourceFile> list = this.kbSourceFileService.selectKbSourceFileListByIds(ids);
        return this.getDataTable(list);
    }

    @Log(title="文件配置", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbSourceFile kbSourceFile) {
        List<KbSourceFile> list = this.kbSourceFileService.selectKbSourceFileList(kbSourceFile);
        ExcelUtil util = new ExcelUtil(KbSourceFile.class);
        util.exportExcel(response, list, "文件配置数据");
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getInfo(@PathVariable(value="id") String id) {
        return this.success((Object)this.kbSourceFileService.selectKbSourceFileById(id));
    }

    @Log(title="文件配置", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbSourceFile kbSourceFile) {
        this.kbSourceFileService.deleteKbSourceFileById(kbSourceFile.getId());
        kbSourceFile.setCreateBy(this.getUsername());
        return this.toAjax(this.kbSourceFileService.insertKbSourceFile(kbSourceFile));
    }

    @Log(title="文件配置", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbSourceFile kbSourceFile) {
        return this.toAjax(this.kbSourceFileService.updateKbSourceFile(kbSourceFile));
    }

    @Log(title="文件配置", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{ids}"})
    public AjaxResult remove(@PathVariable String[] ids) {
        return this.toAjax(this.kbSourceFileService.deleteKbSourceFileByIds(ids));
    }
}
