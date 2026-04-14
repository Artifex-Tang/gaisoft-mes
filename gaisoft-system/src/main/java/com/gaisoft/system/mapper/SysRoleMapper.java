package com.gaisoft.system.mapper;

import com.gaisoft.common.core.domain.entity.SysRole;
import java.util.List;

public interface SysRoleMapper {
    public List<SysRole> selectRoleList(SysRole var1);

    public List<SysRole> selectRolePermissionByUserId(Long var1);

    public List<SysRole> selectRoleAll();

    public List<Long> selectRoleListByUserId(Long var1);

    public SysRole selectRoleById(Long var1);

    public List<SysRole> selectRolesByUserName(String var1);

    public SysRole checkRoleNameUnique(String var1);

    public SysRole checkRoleKeyUnique(String var1);

    public int updateRole(SysRole var1);

    public int insertRole(SysRole var1);

    public int deleteRoleById(Long var1);

    public int deleteRoleByIds(Long[] var1);
}
