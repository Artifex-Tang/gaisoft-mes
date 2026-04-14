package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.domain.SysPost;
import com.gaisoft.system.service.ISysPostService;
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
@RequestMapping(value={"/system/post"})
public class SysPostController
extends BaseController {
    @Autowired
    private ISysPostService postService;

    @PreAuthorize(value="@ss.hasPermi('system:post:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysPost post) {
        this.startPage();
        List list = this.postService.selectPostList(post);
        return this.getDataTable(list);
    }

    @Log(title="岗位管理", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('system:post:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysPost post) {
        List list = this.postService.selectPostList(post);
        ExcelUtil util = new ExcelUtil(SysPost.class);
        util.exportExcel(response, list, "岗位数据");
    }

    @PreAuthorize(value="@ss.hasPermi('system:post:query')")
    @GetMapping(value={"/{postId}"})
    public AjaxResult getInfo(@PathVariable Long postId) {
        return this.success(this.postService.selectPostById(postId));
    }

    @PreAuthorize(value="@ss.hasPermi('system:post:add')")
    @Log(title="岗位管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        if (!this.postService.checkPostNameUnique(post)) {
            return this.error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        if (!this.postService.checkPostCodeUnique(post)) {
            return this.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(this.getUsername());
        return this.toAjax(this.postService.insertPost(post));
    }

    @PreAuthorize(value="@ss.hasPermi('system:post:edit')")
    @Log(title="岗位管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPost post) {
        if (!this.postService.checkPostNameUnique(post)) {
            return this.error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        if (!this.postService.checkPostCodeUnique(post)) {
            return this.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(this.getUsername());
        return this.toAjax(this.postService.updatePost(post));
    }

    @PreAuthorize(value="@ss.hasPermi('system:post:remove')")
    @Log(title="岗位管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{postIds}"})
    public AjaxResult remove(@PathVariable Long[] postIds) {
        return this.toAjax(this.postService.deletePostByIds(postIds));
    }

    @GetMapping(value={"/optionselect"})
    public AjaxResult optionselect() {
        List posts = this.postService.selectPostAll();
        return this.success(posts);
    }
}
