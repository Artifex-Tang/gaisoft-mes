package com.gaisoft.web.controller.system;

import com.gaisoft.common.annotation.Log;
import com.gaisoft.common.config.RuoYiConfig;
import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.AjaxResult;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.enums.BusinessType;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.file.FileUploadUtils;
import com.gaisoft.common.utils.file.MimeTypeUtils;
import com.gaisoft.framework.web.service.TokenService;
import com.gaisoft.system.service.ISysUserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/system/user/profile"})
public class SysProfileController
extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;

    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = this.getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success((Object)user);
        ajax.put("roleGroup", (Object)this.userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", (Object)this.userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    @Log(title="个人信息", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = this.getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty((String)user.getPhonenumber()) && !this.userService.checkPhoneUnique(currentUser)) {
            return this.error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty((String)user.getEmail()) && !this.userService.checkEmailUnique(currentUser)) {
            return this.error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (this.userService.updateUserProfile(currentUser) > 0) {
            this.tokenService.setLoginUser(loginUser);
            return this.success();
        }
        return this.error("修改个人信息异常，请联系管理员");
    }

    @Log(title="个人信息", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/updatePwd"})
    public AjaxResult updatePwd(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        LoginUser loginUser = this.getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword((String)oldPassword, (String)password)) {
            return this.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword((String)newPassword, (String)password)) {
            return this.error("新密码不能与旧密码相同");
        }
        if (this.userService.resetUserPwd(userName, newPassword = SecurityUtils.encryptPassword((String)newPassword)) > 0) {
            loginUser.getUser().setPassword(newPassword);
            this.tokenService.setLoginUser(loginUser);
            return this.success();
        }
        return this.error("修改密码异常，请联系管理员");
    }

    @Log(title="用户头像", businessType=BusinessType.UPDATE)
    @PostMapping(value={"/avatar"})
    public AjaxResult avatar(@RequestParam(value="avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = this.getLoginUser();
            String avatar = FileUploadUtils.upload((String)RuoYiConfig.getAvatarPath(), (MultipartFile)file, (String[])MimeTypeUtils.IMAGE_EXTENSION);
            if (this.userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", (Object)avatar);
                loginUser.getUser().setAvatar(avatar);
                this.tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return this.error("上传图片异常，请联系管理员");
    }
}
