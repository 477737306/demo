package com.cmit.testing.quartz;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 定时任务管理类
 *
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */
public class QuartzManager {

    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    /**
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param jobName
     *            任务名
     * @param cls
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:47:44
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, Class cls, String time, Map<String,Object> params) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger != null) {
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();// 任务名，任务组，任务执行类
            jobDetail.getJobDataMap().put("data", params);
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(jobName,TRIGGER_GROUP_NAME);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
            // 创建Trigger对象
            trigger = (CronTrigger) triggerBuilder.build();
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName
     *            任务名
     * @param jobGroupName
     *            任务组名
     * @param triggerName
     *            触发器名
     * @param triggerGroupName
     *            触发器组名
     * @param jobClass
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:48:15
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, Class jobClass,
                              String time,Map<String,Object> params) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger != null) {
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类
            jobDetail.getJobDataMap().put("data", params);
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName,triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
            // 创建Trigger对象
            trigger = (CronTrigger) triggerBuilder.build();
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     * @param time
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:21
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void modifyJobTime(String jobName, String time,Map<String,Object> param) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(jobName,TRIGGER_GROUP_NAME);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName,JOB_GROUP_NAME));
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time,param);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param triggerName
     * @param triggerGroupName
     * @param time
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:37
     * @version V2.0
     */
    public static void modifyJobTime(String triggerName,
                                     String triggerGroupName, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName,triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
                // 创建Trigger对象
                CronTrigger ct = (CronTrigger) triggerBuilder.build();

                // 重启触发器
                sched.resumeTrigger(triggerKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:51
     * @version V2.0
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseTrigger(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器
            sched.unscheduleJob(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName,JOB_GROUP_NAME));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 移除一个任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:01
     * @version V2.0
     */
    public static void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));// 停止触发器
            sched.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName));// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName,jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  按组名删除任务
     * @param jobGroupName 组名
     */
    public static void removeGroup(String jobGroupName){
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(jobGroupName);
            Set<JobKey> jobkeySet = sched.getJobKeys(matcher);
            List<JobKey> jobkeyList = new ArrayList<JobKey>();
            jobkeyList.addAll(jobkeySet);
            sched.deleteJobs(jobkeyList);
            //System.out.println("change");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     *
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:18
     * @version V2.0
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     *
     *
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     *
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:26
     * @version V2.0
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
