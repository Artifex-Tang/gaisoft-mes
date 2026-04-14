package com.gaisoft.system.mapper;

import com.gaisoft.system.domain.SysLogininfor;
import java.util.List;

public interface SysLogininforMapper {
    public void insertLogininfor(SysLogininfor var1);

    public List<SysLogininfor> selectLogininforList(SysLogininfor var1);

    public int deleteLogininforByIds(Long[] var1);

    public int cleanLogininfor();
}
