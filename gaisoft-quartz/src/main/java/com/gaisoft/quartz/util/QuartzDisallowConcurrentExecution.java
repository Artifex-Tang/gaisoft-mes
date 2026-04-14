package com.gaisoft.quartz.util;

import com.gaisoft.quartz.domain.SysJob;
import com.gaisoft.quartz.util.AbstractQuartzJob;
import com.gaisoft.quartz.util.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution
extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
