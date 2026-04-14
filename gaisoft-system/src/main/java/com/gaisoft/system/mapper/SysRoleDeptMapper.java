package com.gaisoft.system.mapper;

import com.gaisoft.system.domain.SysRoleDept;
import java.util.List;

public interface SysRoleDeptMapper {
    public int deleteRoleDeptByRoleId(Long var1);

    public int deleteRoleDept(Long[] var1);

    public int selectCountRoleDeptByDeptId(Long var1);

    public int batchRoleDept(List<SysRoleDept> var1);
}
