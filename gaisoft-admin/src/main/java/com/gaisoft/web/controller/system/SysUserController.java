package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysDept;
import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.page.TableDataInfo;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.poi.ExcelUtil;
import com.gaisoft.system.service.ISysDeptService;
import com.gaisoft.system.service.ISysPostService;
import com.gaisoft.system.service.ISysRoleService;
import com.gaisoft.system.service.ISysUserService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/system/user"})
public class SysUserController
extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysPostService postService;

    @PreAuthorize(value="@ss.hasPermi('system:user:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysUser user) {
        this.startPage();
        List list = this.userService.selectUserList(user);
        return this.getDataTable(list);
    }

    @Log(title="用户管理", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('system:user:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysUser user) {
        List list = this.userService.selectUserList(user);
        ExcelUtil util = new ExcelUtil(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title="用户管理", businessType=BusinessType.IMPORT)
    @PreAuthorize(value="@ss.hasPermi('system:user:import')")
    @PostMapping(value={"/importData"})
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil util = new ExcelUtil(SysUser.class);
        List userList = util.importExcel(file.getInputStream());
        String operName = this.getUsername();
        String message = this.userService.importUser(userList, Boolean.valueOf(updateSupport), operName);
        return this.success(message);
    }

    @PostMapping(value={"/importTemplate"})
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil util = new ExcelUtil(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:query')")
    @GetMapping(value={"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value="userId", required=false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull((Object)userId)) {
            this.userService.checkUserDataScope(userId);
            SysUser sysUser = this.userService.selectUserById(userId);
            ajax.put("data", (Object)sysUser);
            ajax.put("postIds", (Object)this.postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        List<SysRole> roles = this.roleService.selectRoleAll();
        ajax.put("roles", (Object)(SysUser.isAdmin((Long)userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList())));
        ajax.put("posts", (Object)this.postService.selectPostAll());
        return ajax;
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:add')")
    @Log(title="用户管理", businessType=BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        this.deptService.checkDeptDataScope(user.getDeptId());
        this.roleService.checkRoleDataScope(user.getRoleIds());
        if (!this.userService.checkUserNameUnique(user)) {
            return this.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (StringUtils.isNotEmpty((String)user.getPhonenumber()) && !this.userService.checkPhoneUnique(user)) {
            return this.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty((String)user.getEmail()) && !this.userService.checkEmailUnique(user)) {
            return this.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(this.getUsername());
        user.setPassword(SecurityUtils.encryptPassword((String)user.getPassword()));
        return this.toAjax(this.userService.insertUser(user));
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:edit')")
    @Log(title="用户管理", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        this.userService.checkUserAllowed(user);
        this.userService.checkUserDataScope(user.getUserId());
        this.deptService.checkDeptDataScope(user.getDeptId());
        this.roleService.checkRoleDataScope(user.getRoleIds());
        if (!this.userService.checkUserNameUnique(user)) {
            return this.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (StringUtils.isNotEmpty((String)user.getPhonenumber()) && !this.userService.checkPhoneUnique(user)) {
            return this.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty((String)user.getEmail()) && !this.userService.checkEmailUnique(user)) {
            return this.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(this.getUsername());
        return this.toAjax(this.userService.updateUser(user));
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:remove')")
    @Log(title="用户管理", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{userIds}"})
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains((Object[])userIds, (Object)this.getUserId())) {
            return this.error("当前用户不能删除");
        }
        return this.toAjax(this.userService.deleteUserByIds(userIds));
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:resetPwd')")
    @Log(title="用户管理", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/resetPwd"})
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        this.userService.checkUserAllowed(user);
        this.userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword((String)user.getPassword()));
        user.setUpdateBy(this.getUsername());
        return this.toAjax(this.userService.resetPwd(user));
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:edit')")
    @Log(title="用户管理", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/changeStatus"})
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        this.userService.checkUserAllowed(user);
        this.userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(this.getUsername());
        return this.toAjax(this.userService.updateUserStatus(user));
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:query')")
    @GetMapping(value={"/authRole/{userId}"})
    public AjaxResult authRole(@PathVariable(value="userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = this.userService.selectUserById(userId);
        List<SysRole> roles = this.roleService.selectRolesByUserId(userId);
        ajax.put("user", (Object)user);
        ajax.put("roles", (Object)(SysUser.isAdmin((Long)userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList())));
        return ajax;
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:edit')")
    @Log(title="用户管理", businessType=BusinessType.GRANT)
    @PutMapping(value={"/authRole"})
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        this.userService.checkUserDataScope(userId);
        this.roleService.checkRoleDataScope(roleIds);
        this.userService.insertUserAuth(userId, roleIds);
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:list')")
    @GetMapping(value={"/deptTree"})
    public AjaxResult deptTree(SysDept dept) {
        return this.success(this.deptService.selectDeptTreeList(dept));
    }
}
