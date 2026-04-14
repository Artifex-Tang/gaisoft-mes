package com.gaisoft.kb.controller;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.kb.domain.KbChat;
import com.gaisoft.kb.service.IKbChatService;
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
@RequestMapping(value={"/aftersales/chat"})
public class KbChatController
extends BaseController {
    @Autowired
    private IKbChatService kbChatService;

    @GetMapping(value={"/list"})
    public TableDataInfo list(KbChat kbChat) {
        kbChat.setCreateBy(this.getUsername());
        this.startPage();
        List<KbChat> list = this.kbChatService.selectKbChatList(kbChat);
        return this.getDataTable(list);
    }

    @Log(title="聊天记录同步", businessType=BusinessType.EXPORT)
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, KbChat kbChat) {
        List<KbChat> list = this.kbChatService.selectKbChatList(kbChat);
        ExcelUtil util = new ExcelUtil(KbChat.class);
        util.exportExcel(response, list, "聊天记录同步数据");
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getInfo(@PathVariable(value="id") Long id) {
        return this.success((Object)this.kbChatService.selectKbChatById(id));
    }

    @Log(title="聊天记录同步", businessType=BusinessType.INSERT)
    @PostMapping
    public List<KbChat> add(@RequestBody List<KbChat> kbChats) {
        for (KbChat kbChat : kbChats) {
            kbChat.setCreateBy(this.getUsername());
            this.kbChatService.insertKbChat(kbChat);
        }
        KbChat bean = new KbChat();
        bean.setChatId(kbChats.get(0).getChatId());
        bean.setSessionId(kbChats.get(0).getSessionId());
        bean.setCreateBy(this.getUsername());
        List<KbChat> list = this.kbChatService.selectKbChatList(bean);
        return list;
    }

    @Log(title="聊天记录同步", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbChat kbChat) {
        kbChat.setUpdateBy(this.getUsername());
        return this.toAjax(this.kbChatService.updateKbChat(kbChat));
    }

    @Log(title="聊天记录同步", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{ids}"})
    public AjaxResult remove(@PathVariable Long[] ids) {
        return this.toAjax(this.kbChatService.deleteKbChatByIds(ids));
    }
}
