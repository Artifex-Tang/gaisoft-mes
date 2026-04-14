package com.gaisoft.kb.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbIcon;
import com.gaisoft.kb.service.IKbIconService;
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
@RequestMapping(value={"/kb/icon"})
public class KbIconController
extends BaseController {
    @Autowired
    private IKbIconService kbIconService;

    @GetMapping(value={"/list"})
    public TableDataInfo list(KbIcon kbIcon) {
        this.startPage();
        List<KbIcon> list = this.kbIconService.selectKbIconList(kbIcon);
        return this.getDataTable(list);
    }

    @Log(title="图标管理", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbIcon kbIcon) {
        List<KbIcon> list = this.kbIconService.selectKbIconList(kbIcon);
        ExcelUtil util = new ExcelUtil(KbIcon.class);
        util.exportExcel(response, list, "图标管理数据");
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getInfo(@PathVariable(value="id") Long id) {
        return this.success((Object)this.kbIconService.selectKbIconById(id));
    }

    @Log(title="图标管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbIcon kbIcon) {
        return this.toAjax(this.kbIconService.insertKbIcon(kbIcon));
    }

    @Log(title="图标管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbIcon kbIcon) {
        return this.toAjax(this.kbIconService.updateKbIcon(kbIcon));
    }

    @Log(title="图标管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{ids}"})
    public AjaxResult remove(@PathVariable Long[] ids) {
        return this.toAjax(this.kbIconService.deleteKbIconByIds(ids));
    }
}
