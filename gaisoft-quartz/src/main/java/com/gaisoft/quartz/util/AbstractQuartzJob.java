package com.gaisoft.quartz.util;

import com.gaisoft.common.utils.ExceptionUtil;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.bean.BeanUtils;
import com.gaisoft.common.utils.spring.SpringUtils;
import com.gaisoft.quartz.domain.SysJob;
import com.gaisoft.quartz.domain.SysJobLog;
import com.gaisoft.quartz.service.ISysJobLogService;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractQuartzJob
implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);
    private static ThreadLocal<Date> threadLocal = new ThreadLocal();

    public void execute(JobExecutionContext context) {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp((Object)((Object)sysJob), (Object)context.getMergedJobDataMap().get((Object)"TASK_PROPERTIES"));
        try {
            this.before(context, sysJob);
            if (sysJob != null) {
                this.doExecute(context, sysJob);
            }
            this.after(context, sysJob, null);
        }
        catch (Exception e) {
            log.error("任务执行异常  - ：", (Throwable)e);
            this.after(context, sysJob, e);
        }
    }

    protected void before(JobExecutionContext context, SysJob sysJob) {
        threadLocal.set(new Date());
    }

    protected void after(JobExecutionContext context, SysJob sysJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        long runMs = sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            sysJobLog.setStatus("1");
            String errorMsg = StringUtils.substring((String)ExceptionUtil.getExceptionMessage((Throwable)e), (int)0, (int)2000);
            sysJobLog.setExceptionInfo(errorMsg);
        } else {
            sysJobLog.setStatus("0");
        }
        ((ISysJobLogService)SpringUtils.getBean(ISysJobLogService.class)).addJobLog(sysJobLog);
    }

    protected abstract void doExecute(JobExecutionContext var1, SysJob var2) throws Exception;
}
