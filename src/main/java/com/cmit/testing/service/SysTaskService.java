package com.cmit.testing.service;

import com.cmit.testing.entity.SysTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface SysTaskService extends BaseService<SysTask> {

    /**
     * 根据id生成任务报告
     * @param id
     * @return
     */
    List<Map<String,Object>> getTestCaseReportByTaskId(Integer id);

    /**
     * 根据用例ids生成报告
     * @param ids
     * @return
     */
    List<Map<String,Object>> getTestCaseReportByCaseIds(String ids);
	/**
	 * 根据id查询任务报告
	 *
	 * @param id
	 * @return
	 */
	PageBean<TestCase> getTaskReportById(PageBean<TestCase> pageBean, Integer id);

    /**
     * 查询所有任务
     * @return
     */
    PageBean<SysTask> getAll(PageBean<SysTask> pageBean, SysTask sysTask);
	/**
	 * 查询所有任务
	 *
	 * @return
	 */
	List<SysTask> getAll();

	void executeTask(SysTask sysTask);

	/**
	 * 任务用例报告
	 * @param taskId 任务id
	 * @param province 省份
	 * @return
	 */
	List<TestCaseReportVO> findReportByTaskId(Integer taskId, String province);

	/**
	 * 根据任务id查询省份
	 * @param id
	 * @return
	 */
	HashSet<String> getProviceCaseByTaskId(Integer id);

	/**
	 * 根据任务id查询用例
	 * @param id
	 * @return
	 */
	Map<String,Object> getTestCaeBySysTaskId(Integer id);
}
