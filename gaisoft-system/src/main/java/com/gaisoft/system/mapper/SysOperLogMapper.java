package com.gaisoft.system.mapper;

import com.gaisoft.system.domain.SysOperLog;
import java.util.List;

public interface SysOperLogMapper {
    public void insertOperlog(SysOperLog var1);

    public List<SysOperLog> selectOperLogList(SysOperLog var1);

    public int deleteOperLogByIds(Long[] var1);

    public SysOperLog selectOperLogById(Long var1);

    public void cleanOperLog();
}
