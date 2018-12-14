package com.cmit.testing.quartz.timing;

import com.cmit.testing.listener.TaskScheduleService;
import com.cmit.testing.service.FastDFSClearService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimingScheduledJob {

    public static Logger logger = LoggerFactory.getLogger(Class.class);

    @Autowired
    private TaskScheduleService taskScheduleService;

    /**
     * App相关的定时任务调度方法 该方法已经废弃
     */
   /* @Scheduled(cron = "0 * * * * ?")
    public void timingScheduledAppTask()
    {
        taskScheduleService.queryScheduledTaskList();
    }*/

    /**
     * APP相关的垃圾文件定时清理
     * 每天凌晨3点清理临时文件
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void clearRubbishFiles()
    {
        taskScheduleService.clearRubbishFiles();
    }

    /**
     * APP相关的短信内容校验超时处理
     */
    @Scheduled(cron = "0 * * * * ?")
    public void smsMsgVerifyTask()
    {
        taskScheduleService.smsMsgVerifyTask();
    }

    /**
     * 通过手机设备发送短信
     * 每隔10s扫描一次短信任务
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void sendMessageByPhone()
    {
        taskScheduleService.querySendMessageTask();
    }

    /**
     * 更新设备的状态
     * 每隔30s推送一次消息给前端页面
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void updateDeviceListTask(){
        taskScheduleService.updateDeviceListTask();
    }


}
