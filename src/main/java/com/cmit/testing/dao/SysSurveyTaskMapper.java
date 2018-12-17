package com.cmit.testing.dao;

import com.cmit.testing.entity.SysSurveyTask;

import java.util.List;
import java.util.Set;

import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.vo.StepReportVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysSurveyTaskMapper extends BaseMapper<SysSurveyTask> {

    List<SysSurveyTask> getSysSurveyTaskByIds(@Param("ids") Set<Integer> ids);
    /**
     * 新增众测任务
     */
    int insert(SysSurveyTask record);

    /**
     * 根据任务名和任务发布人分页查询众测任务信息
     * @param taskName 众测任务名
     * @param taskIss  任务发布人
     * @return
     */
    List<SysSurveyTask> findByPage(@Param("taskName") String taskName, @Param("taskIss") String taskIss);

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
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据用例id集合、众测任务Id和省名称汇总用例报告数据
     *
     * @param testcaseIdList
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<SurveyTaskReportVO> caseReportList(@Param("testcaseIdList") List<Integer> testcaseIdList, @Param("surveyTaskId") Integer surveyTaskId, @Param("province") String province);

    /**
     * 根据用例id集合、众测任务Id汇总用例报告数据
     *
     * @param testcaseIdList
     * @param surveyTaskId
     * @return
     */
    List<SurveyTaskReportVO> caseProvinceReportList(@Param("testcaseIdList") List<Integer> testcaseIdList, @Param("surveyTaskId") Integer surveyTaskId);


    /**
     * 查询用例步骤平均耗时
     *
     * @param testCaseId
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<StepReportVO> caseStepReport(@Param("testCaseId") String testCaseId, @Param("surveyTaskId") Integer surveyTaskId, @Param("province") String province);
}