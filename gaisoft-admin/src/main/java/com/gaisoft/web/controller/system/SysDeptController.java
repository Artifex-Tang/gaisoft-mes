package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysDept;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.service.ISysDeptService;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
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
@RequestMapping(value={"/system/dept"})
public class SysDeptController
extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @PreAuthorize(value="@ss.hasPermi('system:dept:list')")
    @GetMapping(value={"/list"})
    public AjaxResult list(SysDept dept) {
        List depts = this.deptService.selectDeptList(dept);
        return this.success(depts);
    }

    @PreAuthorize(value="@ss.hasPermi('system:dept:list')")
    @GetMapping(value={"/list/exclude/{deptId}"})
    public AjaxResult excludeChild(@PathVariable(value="deptId", required=false) Long deptId) {
        List<SysDept> depts = this.deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> (long)d.getDeptId().intValue() == deptId || ArrayUtils.contains((Object[])StringUtils.split((String)d.getAncestors(), (String)","), (Object)(deptId + "")));
        return this.success(depts);
    }

    @PreAuthorize(value="@ss.hasPermi('system:dept:query')")
    @GetMapping(value={"/{deptId}"})
    public AjaxResult getInfo(@PathVariable Long deptId) {
        this.deptService.checkDeptDataScope(deptId);
        return this.success(this.deptService.selectDeptById(deptId));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dept:add')")
    @Log(title="部门管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (!this.deptService.checkDeptNameUnique(dept)) {
            return this.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(this.getUsername());
        return this.toAjax(this.deptService.insertDept(dept));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dept:edit')")
    @Log(title="部门管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        this.deptService.checkDeptDataScope(deptId);
        if (!this.deptService.checkDeptNameUnique(dept)) {
            return this.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if (dept.getParentId().equals(deptId)) {
            return this.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        if (StringUtils.equals((CharSequence)"1", (CharSequence)dept.getStatus()) && this.deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return this.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(this.getUsername());
        return this.toAjax(this.deptService.updateDept(dept));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dept:remove')")
    @Log(title="部门管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{deptId}"})
    public AjaxResult remove(@PathVariable Long deptId) {
        if (this.deptService.hasChildByDeptId(deptId)) {
            return this.warn("存在下级部门,不允许删除");
        }
        if (this.deptService.checkDeptExistUser(deptId)) {
            return this.warn("部门存在用户,不允许删除");
        }
        this.deptService.checkDeptDataScope(deptId);
        return this.toAjax(this.deptService.deleteDeptById(deptId));
    }
}
