package com.gaisoft.kb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbSession;
import com.gaisoft.kb.service.IKbSessionService;
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
@RequestMapping(value={"/aftersales/session"})
public class KbSessionController
extends BaseController {
    @Autowired
    private IKbSessionService kbSessionService;

    @GetMapping(value={"/list"})
    public TableDataInfo list(KbSession kbSession) {
        this.startPage();
        kbSession.setCreateBy(this.getUsername());
        List<KbSession> list = this.kbSessionService.selectKbSessionList(kbSession);
        return this.getDataTable(list);
    }

    @Log(title="session", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbSession kbSession) {
        List<KbSession> list = this.kbSessionService.selectKbSessionList(kbSession);
        ExcelUtil util = new ExcelUtil(KbSession.class);
        util.exportExcel(response, list, "session数据");
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getInfo(@PathVariable(value="id") Long id) {
        return this.success((Object)this.kbSessionService.selectKbSessionById(id));
    }

    @Log(title="session", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbSession kbSession) throws JsonProcessingException {
        kbSession.setCreateBy(this.getUsername());
        return new AjaxResult(200, "创建成功", (Object)this.kbSessionService.insertKbSession(kbSession));
    }

    @Log(title="session", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbSession kbSession) {
        kbSession.setUserId(this.getUserId());
        kbSession.setUpdateBy(this.getUsername());
        return this.toAjax(this.kbSessionService.updateKbSession(kbSession));
    }

    @Log(title="session", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{ids}"})
    public AjaxResult remove(@PathVariable Long[] ids) throws JsonProcessingException {
        return this.toAjax(this.kbSessionService.deleteKbSessionByIds(ids));
    }
}
