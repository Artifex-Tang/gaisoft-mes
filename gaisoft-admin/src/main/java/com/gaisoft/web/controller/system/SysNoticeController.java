package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.system.domain.SysNotice;
import com.gaisoft.system.service.ISysNoticeService;
import java.util.List;
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
@RequestMapping(value={"/system/notice"})
public class SysNoticeController
extends BaseController {
    @Autowired
    private ISysNoticeService noticeService;

    @PreAuthorize(value="@ss.hasPermi('system:notice:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysNotice notice) {
        this.startPage();
        List list = this.noticeService.selectNoticeList(notice);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('system:notice:query')")
    @GetMapping(value={"/{noticeId}"})
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return this.success(this.noticeService.selectNoticeById(noticeId));
    }

    @PreAuthorize(value="@ss.hasPermi('system:notice:add')")
    @Log(title="通知公告", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(this.getUsername());
        return this.toAjax(this.noticeService.insertNotice(notice));
    }

    @PreAuthorize(value="@ss.hasPermi('system:notice:edit')")
    @Log(title="通知公告", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(this.getUsername());
        return this.toAjax(this.noticeService.updateNotice(notice));
    }

    @PreAuthorize(value="@ss.hasPermi('system:notice:remove')")
    @Log(title="通知公告", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{noticeIds}"})
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return this.toAjax(this.noticeService.deleteNoticeByIds(noticeIds));
    }
}
