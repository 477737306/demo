package com.cmit.testing.listener.timer;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author XieZuLiang
 * @description TODO 任务调度管理
 * @date 2018/10/26 0026.
 */
@Component
public class QuartzScheduledManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzScheduledManager.class);

    private static final String JOB_GROUP_NAME     = "cronSyncFromTesting_Job";
    private static final String TRIGGER_GROUP_NAME = "cronSyncFromTesting_Trigger";

    private SchedulerFactory schedulerFactory;

    public QuartzScheduledManager()
    {
        schedulerFactory = new StdSchedulerFactory();
    }

    /**
     * 添加定时任务：使用默认的任务组名，触发器名，触发器组名
     * @param jobName    任务名称
     * @param cronTime   执行时间表达式
     * @param jobCls     任务
     */
    public boolean addJob(String jobName, String cronTime, Class jobCls)
    {
        return addJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, cronTime, jobCls);
    }

    /**
     * 添加一个定时任务
     * @param jobName            任务名称
     * @param jobGroupName       任务组名
     * @param triggerName        触发器名
     * @param triggerGroupName   触发器组名
     * @param cronTime           执行时间表达式
     * @param jobCls             任务
     */
    public boolean addJob(String jobName, String jobGroupName, String triggerName,
                              String triggerGroupName, String cronTime, Class jobCls)
    {
        if (!CronExpression.isValidExpression(cronTime))
        {
            LOGGER.error("Illegal cron expression format({})", cronTime);
            return false;
        }
        try
        {
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobCls)
                    .withIdentity(new JobKey(jobName, jobGroupName))
                    .build();
            // 触发器  触发器名,触发器组
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronTime))
                    .withIdentity(new TriggerKey(triggerName, triggerGroupName))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown())
            {
                scheduler.start();
            }
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("QuartzScheduledManager add Job failed : ", e);
            return false;
        }
    }

    /**
     * 修改任务的触发时间：使用默认的任务组名，触发器名，触发器组名
     * @param jobName   任务名称
     * @param cronTime  触发时间
     * @return
     */
    public boolean modifyJobCronTime(String jobName, String cronTime)
    {
        return modifyJobCronTime(jobName, TRIGGER_GROUP_NAME, cronTime);
    }


    /**
     * 修改任务的触发时间
     * @param triggerName
     * @param triggerGroupName
     * @param cronTime
     * @return
     */
    public boolean modifyJobCronTime(String triggerName, String triggerGroupName, String cronTime)
    {
        if (!CronExpression.isValidExpression(cronTime))
        {
            LOGGER.error("Illegal cron expression format({})", cronTime);
            return false;
        }
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
        try
        {
            Scheduler scheduler = schedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return false;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronTime))
            {
                // 根据新的时间重新构建触发器
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronTime)).build();
                // 重启触发器
                scheduler.rescheduleJob(triggerKey, trigger);
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("QuartzScheduledManager modify Job CronTime failed : ", e);
            return false;
        }
    }

    /**
     * 删除任务
     * @param jobName
     * @return
     */
    public boolean deleteJob(String jobName)
    {
        return deleteJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME);
    }

    /**
     * 删除任务
     * @param jobName             任务名称
     * @param jobGroupName        任务组名
     * @param triggerName         触发器名
     * @param triggerGroupName    触发器组名
     * @return
     */
    public boolean deleteJob(String jobName, String jobGroupName,
                             String triggerName, String triggerGroupName)
    {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
            JobKey jobKey = new JobKey(jobName,jobGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(jobKey);

            return true;
        } catch (Exception e) {
            LOGGER.error("");
            return false;
        }
    }

}
