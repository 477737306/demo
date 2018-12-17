package com.cmit.testing.quartz;

import com.cmit.testing.entity.TimedTask;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.TimedTaskService;
import com.cmit.testing.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用例定时任务工具类
 */
@Component
public class QuartzTestTaskUtil {

    public static Logger logger = LoggerFactory.getLogger(Class.class);

    private  static QuartzTestTaskUtil quartzTestTaskUtil;

    @Autowired
    private TimedTaskService timedTaskService;


    @PostConstruct
    public void init() {
        quartzTestTaskUtil = this;
        quartzTestTaskUtil.timedTaskService = this.timedTaskService;
    }

    /**
     * 服务启动时扫描添加定时任务
     */
    public static void startTimedTask(){
        TimedTask timedTask = new TimedTask();
        timedTask.setStatus(0);//查询开启状态的定时任务
        List<TimedTask> timedTasks =  quartzTestTaskUtil.timedTaskService.findAll(timedTask);
        if(timedTasks !=  null && timedTasks.size()>0){
            for (TimedTask timedTask_ : timedTasks) {
                addTimedTask(timedTask_);
            }
        }
    }

    /**
     *  添加定时任务
     *  @param timedTask
     */
    public static void addTimedTask(TimedTask timedTask) {
        try {
            Map<String, Object> map = new HashMap<>();
            if("web".equals(timedTask.getTestCaseType())) { //web
                map.put("type","web");
                map.put("testCaseId", timedTask.getTestCaseId());
            }
            else
            {
                //app定时任务处理
                map.put("type", "app");
                map.put("testCaseId", timedTask.getTestCaseId());
            }
            //添加新的定时任务
            QuartzManager.addJob(timedTask.getJobName(), timedTask.getGroupName(), timedTask.getTriggerName(), timedTask.getTriggerGroupName(), QuartzTestJob.class, timedTask.getCron(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除定时任务
     * @param timedTask
     */
    public static void removeTimedTask(TimedTask timedTask) {
        try {
            QuartzManager.removeJob(timedTask.getJobName(), timedTask.getGroupName(), timedTask.getTriggerName(),timedTask.getTriggerGroupName());  //删除之前的定时任务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
