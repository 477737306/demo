package com.cmit.testing.service;

import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.vo.StepReportVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

public interface SysSurveyTaskService extends BaseService<SysSurveyTask> {

    /**
     * 根据任务名和任务发布人分页查询众测任务信息
     *
     * @param currentPage 当前页码
     * @param pageSize    每页展示条数
     * @param taskName    任务名
     * @param taskIss     任务发布人
     * @return
     */
    PageBean<SysSurveyTask> findPage(int currentPage, int pageSize, String taskName, String taskIss);

    /**
     * 新增众测任务
     */
    int insert(SysSurveyTask record, Integer type);

    /**
     * 根据任务id查询
     *
     * @param taskId
     * @return
     */
    SysSurveyTask selectByTaskId(Integer taskId);

    /**
     * 根据任务id删除
     *
     * @param taskId
     * @return
     */
    int deleteByTaskId(Integer taskId);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 根据全网任务id和省名称汇总用例报告数据
     *
     * @param surveyTaskId
     * @param province
     * @param type
     * @return
     */
    List<SurveyTaskReportVO> caseReportList(Integer surveyTaskId, String province, Integer type);

    /**
     * 查询用例步骤平均耗时
     *
     * @param testCaseId
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<StepReportVO> caseStepReport(String testCaseId, Integer surveyTaskId, String province);

    String getJsonResult(SysSurveyTask sysSurveyTask, Map caseIds);

    /**
     * 获取所有任务
     * @param taskName
     * @param taskIss
     * @return
     */
    List<SysSurveyTask> findAll(String taskName, String taskIss);

    void surveyTaskExcute(Integer surveyTaskid, Map<String, List<Integer>> caseMap);
}
