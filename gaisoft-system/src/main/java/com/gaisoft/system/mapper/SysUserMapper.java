package com.gaisoft.system.mapper;

import com.gaisoft.common.core.domain.entity.SysUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper {
    public List<SysUser> selectUserList(SysUser var1);

    public List<SysUser> selectAllocatedList(SysUser var1);

    public List<SysUser> selectUnallocatedList(SysUser var1);

    public SysUser selectUserByUserName(String var1);

    public SysUser selectUserById(Long var1);

    public int insertUser(SysUser var1);

    public int updateUser(SysUser var1);

    public int updateUserAvatar(@Param(value="userName") String var1, @Param(value="avatar") String var2);

    public int resetUserPwd(@Param(value="userName") String var1, @Param(value="password") String var2);

    public int deleteUserById(Long var1);

    public int deleteUserByIds(Long[] var1);

    public SysUser checkUserNameUnique(String var1);

    public SysUser checkPhoneUnique(String var1);

    public SysUser checkEmailUnique(String var1);
}
