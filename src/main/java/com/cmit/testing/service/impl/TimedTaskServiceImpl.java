package com.cmit.testing.service.impl;

import com.cmit.testing.dao.TimedTaskMapper;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TimedTask;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.quartz.QuartzManager;
import com.cmit.testing.quartz.QuartzTestTaskUtil;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.TimedTaskService;
import com.cmit.testing.utils.Constants;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TimedTaskServiceImpl extends BaseServiceImpl<TimedTask> implements TimedTaskService {

    @Autowired
    TimedTaskMapper timedTaskMapper;

    @Override
    public int updateByTestCaseIdAndType(TimedTask timedTask) {
        timedTask.setUpdateTime(new Date());
        timedTask.setUpdateBy(getShiroUser().getName());
        return timedTaskMapper.updateByTestCaseIdAndType(timedTask);
    }

    @Override
    public int updateStatusByCaseIdAndType(TimedTask timedTask) {
        timedTask.setUpdateTime(new Date());
        timedTask.setUpdateBy("系统");
        if (timedTask.getStatus().equals(1))
        {
            //关闭任务，将任务队列中的数据移除
            //QuartzManager.removeJob(Constants.APP_JOB_NAME + timedTask.getTestCaseId(),
              //      Constants.APP_JOB_GROUP_NAME,Constants.APP_TRIGGER_NAME,Constants.APP_TRIGGER_GROUP_NAME);
        }
        return timedTaskMapper.updateStatusByCaseIdAndType(timedTask);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return timedTaskMapper.deleteByIds(ids);
    }

    @Override
    public PageBean<TimedTask> findPage(int currentPage, int pageSize, TimedTask timedTask) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<TimedTask> timedTasks = timedTaskMapper.findAll(timedTask);
        PageBean<TimedTask> page_ = new PageBean<>(currentPage, pageSize, (int) page.getTotal());
        page_.setItems(timedTasks);
        return page_;
    }

    @Override
    public List<TimedTask>  findAll(TimedTask timedTask) {
        return timedTaskMapper.findAll(timedTask);
    }

    /**
     * 添加定时任务
     * @param timedTask
     */
    @Override
    public void addTimedTask(TimedTask timedTask){
        String userName = getShiroUser().getName();
        timedTask.setStatus(0);//默认开启
        timedTask.setCreateBy(userName);
        timedTask.setUpdateBy(userName);
        timedTask.setCreateTime(new Date());
        timedTask.setUpdateTime(new Date());

        if("web".equals(timedTask.getTestCaseType())) {
            timedTask.setJobName(Constants.WEB_JOB_NAME+timedTask.getTestCaseId());
            timedTask.setTriggerName(Constants.WEB_TRIGGER_NAME);
            timedTask.setGroupName(Constants.WEB_JOB_GROUP_NAME);
            timedTask.setTriggerGroupName(Constants.WEB_TRIGGER_GROUP_NAME);
        }
        else if ("app".equals(timedTask.getTestCaseType()))
        {
            timedTask.setJobName(Constants.APP_JOB_NAME + timedTask.getTestCaseId());
            timedTask.setTriggerName(Constants.APP_TRIGGER_NAME);
            timedTask.setGroupName(Constants.APP_JOB_GROUP_NAME);
            timedTask.setTriggerGroupName(Constants.APP_TRIGGER_GROUP_NAME);
        }
        //删除定时任务
        timedTaskMapper.deleteByTestCaseIdAndType(timedTask.getTestCaseId(),timedTask.getTestCaseType());
        QuartzTestTaskUtil.removeTimedTask(timedTask);
        //添加定时任务
        timedTaskMapper.insert(timedTask);
        QuartzTestTaskUtil.addTimedTask(timedTask);
    }

    /**
     * 删除定时任务
     * @param timedTask
     */
    @Override
    public void deleteTimedTask(TimedTask timedTask ){
        List<TimedTask> timedTasks = timedTaskMapper.findAll(timedTask);
        if(timedTasks!= null && timedTasks.size()>0) {
            for (TimedTask task : timedTasks) {
                timedTaskMapper.deleteByPrimaryKey(task.getId());//删除表里的定时任务
                QuartzTestTaskUtil.removeTimedTask(task);//删除正在运行的定时任务
            }
        }
    }

    @Override
    public int deleteByTestCaseIdAndType(Integer testCaseId, String type) {
        return timedTaskMapper.deleteByTestCaseIdAndType(testCaseId,type);
    }


}
