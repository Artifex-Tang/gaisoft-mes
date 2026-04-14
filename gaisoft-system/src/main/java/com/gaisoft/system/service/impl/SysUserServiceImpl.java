package com.gaisoft.system.service.impl;

import com.gaisoft.common.annotation.DataScope;
import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.bean.BeanValidators;
import com.gaisoft.common.utils.spring.SpringUtils;
import com.gaisoft.system.domain.SysPost;
import com.gaisoft.system.domain.SysUserPost;
import com.gaisoft.system.domain.SysUserRole;
import com.gaisoft.system.mapper.SysPostMapper;
import com.gaisoft.system.mapper.SysRoleMapper;
import com.gaisoft.system.mapper.SysUserMapper;
import com.gaisoft.system.mapper.SysUserPostMapper;
import com.gaisoft.system.mapper.SysUserRoleMapper;
import com.gaisoft.system.service.ISysConfigService;
import com.gaisoft.system.service.ISysDeptService;
import com.gaisoft.system.service.ISysUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class SysUserServiceImpl
implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysPostMapper postMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysUserPostMapper userPostMapper;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    protected Validator validator;

    @Override
    @DataScope(deptAlias="d", userAlias="u")
    public List<SysUser> selectUserList(SysUser user) {
        return this.userMapper.selectUserList(user);
    }

    @Override
    @DataScope(deptAlias="d", userAlias="u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return this.userMapper.selectAllocatedList(user);
    }

    @Override
    @DataScope(deptAlias="d", userAlias="u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return this.userMapper.selectUnallocatedList(user);
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return this.userMapper.selectUserByUserName(userName);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return this.userMapper.selectUserById(userId);
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = this.roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = this.postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull((Object)user.getUserId()) ? -1L : user.getUserId();
        SysUser info = this.userMapper.checkUserNameUnique(user.getUserName());
        return !StringUtils.isNotNull((Object)info) || info.getUserId().longValue() == userId.longValue();
    }

    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull((Object)user.getUserId()) ? -1L : user.getUserId();
        SysUser info = this.userMapper.checkPhoneUnique(user.getPhonenumber());
        return !StringUtils.isNotNull((Object)info) || info.getUserId().longValue() == userId.longValue();
    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull((Object)user.getUserId()) ? -1L : user.getUserId();
        SysUser info = this.userMapper.checkEmailUnique(user.getEmail());
        return !StringUtils.isNotNull((Object)info) || info.getUserId().longValue() == userId.longValue();
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull((Object)user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin((Long)SecurityUtils.getUserId())) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = ((SysUserServiceImpl)SpringUtils.getAopProxy((Object)this)).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    @Override
    @Transactional
    public int insertUser(SysUser user) {
        int rows = this.userMapper.insertUser(user);
        this.insertUserPost(user);
        this.insertUserRole(user);
        return rows;
    }

    @Override
    public boolean registerUser(SysUser user) {
        return this.userMapper.insertUser(user) > 0;
    }

    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        this.insertUserRole(user);
        this.userPostMapper.deleteUserPostByUserId(userId);
        this.insertUserPost(user);
        return this.userMapper.updateUser(user);
    }

    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        this.insertUserRole(userId, roleIds);
    }

    @Override
    public int updateUserStatus(SysUser user) {
        return this.userMapper.updateUser(user);
    }

    @Override
    public int updateUserProfile(SysUser user) {
        return this.userMapper.updateUser(user);
    }

    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return this.userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    @Override
    public int resetPwd(SysUser user) {
        return this.userMapper.updateUser(user);
    }

    @Override
    public int resetUserPwd(String userName, String password) {
        return this.userMapper.resetUserPwd(userName, password);
    }

    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    public void insertUserPost(SysUser user) {
        Object[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty((Object[])posts)) {
            ArrayList<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Object postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId((Long)postId);
                list.add(up);
            }
            this.userPostMapper.batchUserPost(list);
        }
    }

    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty((Object[])roleIds)) {
            ArrayList<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            this.userRoleMapper.batchUserRole(list);
        }
    }

    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        this.userRoleMapper.deleteUserRoleByUserId(userId);
        this.userPostMapper.deleteUserPostByUserId(userId);
        return this.userMapper.deleteUserById(userId);
    }

    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            this.checkUserAllowed(new SysUser(userId));
            this.checkUserDataScope(userId);
        }
        this.userRoleMapper.deleteUserRole(userIds);
        this.userPostMapper.deleteUserPost(userIds);
        return this.userMapper.deleteUserByIds(userIds);
    }

    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                SysUser u = this.userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull((Object)u)) {
                    BeanValidators.validateWithException((Validator)this.validator, (Object)user, (Class[])new Class[0]);
                    this.deptService.checkDeptDataScope(user.getDeptId());
                    String password = this.configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword((String)password));
                    user.setCreateBy(operName);
                    this.userMapper.insertUser(user);
                    successMsg.append("<br/>" + ++successNum + "、账号 " + user.getUserName() + " 导入成功");
                    continue;
                }
                if (isUpdateSupport.booleanValue()) {
                    BeanValidators.validateWithException((Validator)this.validator, (Object)user, (Class[])new Class[0]);
                    this.checkUserAllowed(u);
                    this.checkUserDataScope(u.getUserId());
                    this.deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    this.userMapper.updateUser(user);
                    successMsg.append("<br/>" + ++successNum + "、账号 " + user.getUserName() + " 更新成功");
                    continue;
                }
                failureMsg.append("<br/>" + ++failureNum + "、账号 " + user.getUserName() + " 已存在");
            }
            catch (Exception e) {
                String msg = "<br/>" + ++failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, (Throwable)e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }
}
