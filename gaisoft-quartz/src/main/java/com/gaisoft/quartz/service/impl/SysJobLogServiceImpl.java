package com.gaisoft.quartz.service.impl;

import com.gaisoft.quartz.domain.SysJobLog;
import com.gaisoft.quartz.mapper.SysJobLogMapper;
import com.gaisoft.quartz.service.ISysJobLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysJobLogServiceImpl
implements ISysJobLogService {
    @Autowired
    private SysJobLogMapper jobLogMapper;

    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
        return this.jobLogMapper.selectJobLogList(jobLog);
    }

    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        return this.jobLogMapper.selectJobLogById(jobLogId);
    }

    @Override
    public void addJobLog(SysJobLog jobLog) {
        this.jobLogMapper.insertJobLog(jobLog);
    }

    @Override
    public int deleteJobLogByIds(Long[] logIds) {
        return this.jobLogMapper.deleteJobLogByIds(logIds);
    }

    @Override
    public int deleteJobLogById(Long jobId) {
        return this.jobLogMapper.deleteJobLogById(jobId);
    }

    @Override
    public void cleanJobLog() {
        this.jobLogMapper.cleanJobLog();
    }
}
