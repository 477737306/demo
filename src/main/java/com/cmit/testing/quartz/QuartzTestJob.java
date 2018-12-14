package com.cmit.testing.quartz;

import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TimedTask;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.ExecuteCaseBO;
import com.cmit.testing.service.ScriptStepService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.TimedTaskService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.TaskService;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.ThreadUtils;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 用例执行任务
 */
@Component
public class QuartzTestJob implements Job {

    private  static QuartzTestJob quartzTestJob;

    @Autowired
    private ScriptStepService scriptStepService;

    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private TimedTaskService timedTaskService;


    @PostConstruct
    public void init() {
        quartzTestJob = this;
        quartzTestJob.scriptStepService = this.scriptStepService;
        quartzTestJob.testCaseService = this.testCaseService;
        quartzTestJob.taskService = this.taskService;
        quartzTestJob.appCaseService = this.appCaseService;
        quartzTestJob.timedTaskService = this.timedTaskService;

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        Map<String,Object> data =(Map<String,Object>) dataMap.get("data");
        Integer testCaseId = (Integer) data.get("testCaseId");

        //执行
        if("app".equals(data.get("type").toString()))
        {
            AppCase appCase = quartzTestJob.appCaseService.getAppCaseById(testCaseId);
            if (appCase != null)
            {
                if (appCase.getExecuteType().equals(1))
                {
                    // 若是定时任务，任务执行完成则需要将状态改为关闭
                    TimedTask timedTask = new TimedTask();
                    timedTask.setTestCaseId(testCaseId);
                    timedTask.setTestCaseType("app");
                    // 0开启，1关闭
                    timedTask.setStatus(1);
                    quartzTestJob.timedTaskService.updateStatusByCaseIdAndType(timedTask);
                }
                ExecuteCaseBO caseBO = new ExecuteCaseBO();
                caseBO.setType(AppConstants.EXECUTE_TYPE_SINGLECASE);
                caseBO.setCaseId(appCase.getCaseId());
                caseBO.setRealExecuteNum(1);
                quartzTestJob.taskService.commonExecute(caseBO);
            }
        } else { //执行web用例
        	// 生成用例副本
        	TestCase t1 = quartzTestJob.scriptStepService.executeTestCaseBefore(quartzTestJob.testCaseService.selectByPrimaryKey(testCaseId), false);
        	// 执行用例副本
        	scriptStepService.executeTestCase(t1);
        	// 判断用例是否执行结束
        	while (2 != ThreadUtils.getTestcaseExecuteStatus(t1.getId())) {
        		Thread.yield();
        	}
        	// 更改用例与用例副本的状态
        	scriptStepService.executeTestCaseAfter(t1);
        }
    }
}
