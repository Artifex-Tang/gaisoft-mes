package com.gaisoft.quartz.util;

import com.gaisoft.common.constant.Constants;
import com.gaisoft.common.constant.ScheduleConstants;
import com.gaisoft.common.exception.job.TaskException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.common.utils.spring.SpringUtils;
import com.gaisoft.quartz.domain.SysJob;
import com.gaisoft.quartz.util.CronUtils;
import com.gaisoft.quartz.util.QuartzDisallowConcurrentExecution;
import com.gaisoft.quartz.util.QuartzJobExecution;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

public class ScheduleUtils {
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob) {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey((String)("TASK_CLASS_NAME" + jobId), (String)jobGroup);
    }

    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey((String)("TASK_CLASS_NAME" + jobId), (String)jobGroup);
    }

    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException, TaskException {
        Class<? extends Job> jobClass = ScheduleUtils.getQuartzJobClass(job);
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(ScheduleUtils.getJobKey(jobId, jobGroup)).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule((String)job.getCronExpression());
        cronScheduleBuilder = ScheduleUtils.handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);
        CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity(ScheduleUtils.getTriggerKey(jobId, jobGroup)).withSchedule((ScheduleBuilder)cronScheduleBuilder).build();
        jobDetail.getJobDataMap().put("TASK_PROPERTIES", (Object)job);
        if (scheduler.checkExists(ScheduleUtils.getJobKey(jobId, jobGroup))) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        if (StringUtils.isNotNull((Object)CronUtils.getNextExecution(job.getCronExpression()))) {
            scheduler.scheduleJob(jobDetail, (Trigger)trigger);
        }
        if (job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb) throws TaskException {
        switch (job.getMisfirePolicy()) {
            case "0": {
                return cb;
            }
            case "1": {
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            }
            case "2": {
                return cb.withMisfireHandlingInstructionFireAndProceed();
            }
            case "3": {
                return cb.withMisfireHandlingInstructionDoNothing();
            }
        }
        throw new TaskException("The task misfire policy '" + job.getMisfirePolicy() + "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
    }

    public static boolean whiteList(String invokeTarget) {
        String packageName = StringUtils.substringBefore((String)invokeTarget, (String)"(");
        int count = StringUtils.countMatches((CharSequence)packageName, (CharSequence)".");
        if (count > 1) {
            return StringUtils.containsAnyIgnoreCase((CharSequence)invokeTarget, (CharSequence[])Constants.JOB_WHITELIST_STR);
        }
        Object obj = SpringUtils.getBean((String)StringUtils.split((String)invokeTarget, (String)".")[0]);
        String beanPackageName = obj.getClass().getPackage().getName();
        return StringUtils.containsAnyIgnoreCase((CharSequence)beanPackageName, (CharSequence[])Constants.JOB_WHITELIST_STR) && !StringUtils.containsAnyIgnoreCase((CharSequence)beanPackageName, (CharSequence[])Constants.JOB_ERROR_STR);
    }
}
