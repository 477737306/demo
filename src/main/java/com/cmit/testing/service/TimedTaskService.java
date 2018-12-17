package com.cmit.testing.service;

import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TimedTask;
import com.cmit.testing.page.PageBean;

import java.util.List;

public interface TimedTaskService extends BaseService<TimedTask> {

    /**
     * 修改
     */
     int updateByTestCaseIdAndType(TimedTask timedTask);

    /**
     * 供给系统自动更新任务状态
     * @param timedTask
     * @return
     */
     int updateStatusByCaseIdAndType(TimedTask timedTask);

    /**
     * 批量删除
     */
    int deleteByIds(List<Integer> ids);
    
   /**
    * 根据条件分页查询定时任务信息
    * @param currentPage
    * @param pageSize
    * @param timedTask
    * @return
    */
    PageBean<TimedTask> findPage(int currentPage, int pageSize, TimedTask timedTask);

    /**
     * 根据条件查询定时任务信息
     * @param timedTask
     * @return
     */
    List<TimedTask> findAll(TimedTask timedTask);

    /**
     * 根据用例id和用例类型删除定时任务
     */
    int deleteByTestCaseIdAndType(Integer testCaseId, String type);

    /**
     * 添加定时任务
     * @param timedTask
     */
    void addTimedTask(TimedTask timedTask);

    /**
     * 删除定时任务
     * @param timedTask
     */
    void deleteTimedTask(TimedTask timedTask);
}
