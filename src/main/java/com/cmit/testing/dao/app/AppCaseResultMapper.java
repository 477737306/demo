package com.cmit.testing.dao.app;

import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCaseResult;
import com.cmit.testing.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppCaseResultMapper {

    /**
     * 批量删除
     * @param ids 结果resultId集合
     * @return
     */
    int deleteResultByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据executeId删除结果相关的数据
     */
    int deleteResultByCaseId(@Param("appCaseId") Integer caseId);
    int deleteResultByExecuteId(@Param("executeId") Integer executeId);
    /**
     * 根据caseId查询resultId的集合
     */
    List<AppCaseResult> getResultIdsByCaseId(@Param("appCaseId") Integer appCaseId);

    int deleteByPrimaryKey(@Param("resultId") Integer id);

    int insert(AppCaseResult record);

    int insertSelective(AppCaseResult record);

    AppCaseResult selectByPrimaryKey(@Param("resultId") Integer id);
    AppCaseResult selectByExecuteId(@Param("executeId") Integer id);

    int updateByPrimaryKeySelective(AppCaseResult record);

    int updateByPrimaryKeySelectiveVO(CaseExcResultVO caseExcResultVO);

    int updateByPrimaryKeyWithBLOBs(AppCaseResult record);

    int updateByPrimaryKey(AppCaseResult record);

    List<CaseExcResultVO> listCaseExcResult(Integer caseid);
    /**
     * 根据副本用例ID获取对应的报告信息
     */
    List<CommonResultVO> getAppResultByCaseIdAndStatus(@Param("caseId") Integer caseId, @Param("executeResult") Integer executeResult);

    List<CaseExcProcessVO> listCaseExcProcess(Integer caseid);

    List<CaseExcResultVO> getAppCaseResultByCaseId(CaseExcResultVO caseExcResultVO);

    List<CaseExcResultVO> TestList(CaseExcResultVO caseExcResultVO);
    List<RecordStepVO>findstep(RecordStepVO recordStepVO);
    List<TestCaseReportVO>CaseResults(TestCaseReportVO testCaseReportVO);
    int DelTestList(@Param("ids") List<Integer> ids);

    Integer queryCaseBugCount();

    /**
     * 查询App侧，所有的报告总数，缺陷报告数
     * @return
     */
    Map<String, Long> queryAppResultCount();

   int countByExecuteId(Integer executeId);
    /**
     * 查询用例步骤平均耗时
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<StepReportVO> appCaseStepReport(@Param("caseId") Integer caseId, @Param("surveyTaskId") Integer surveyTaskId, @Param("province") String province);

    /**
     * 根据用例id查询省份
     * @param caseIds
     * @return
     */
    List<String> getProvinceByAppCaseIds(@Param("caseIds") List<Integer> caseIds);
    List<Integer> getResultIdsByCaseIds(@Param("caseIds") List<Integer> caseIds);
    /**
     * 根据用例id集合、众测任务Id和省名称汇总用例报告数据
     *
     * @param testcaseIdList
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<SurveyTaskReportVO> appCaseReportList(@Param("appCaseIdList") List<Integer> testcaseIdList, @Param("surveyTaskId") Integer surveyTaskId, @Param("province") String province);

    int updateBytetestCaseReport(TestCaseReport testCaseReport);

    List<CommonResultVO> getCaseReportByCaseIdAndExecuteNum(@Param("caseId") Integer caseid, @Param("number") Integer number);

    /**
     * 获取截图下载的相关信息
     * @param caseVO
     * @return
     */
    List<CommonResultVO> getDownloadZipFileInfo(CommonResultVO caseVO);
    List<AppCaseResult> getTestAppCaseReportByFewDays(@Param("day") String day, @Param("date") String date);
}