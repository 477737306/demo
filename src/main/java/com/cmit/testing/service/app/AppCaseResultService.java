package com.cmit.testing.service.app;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseResult;
import com.cmit.testing.entity.vo.*;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BaseService;
import org.apache.ibatis.annotations.Case;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 *creat by chenxiaozhang
 * 2018,9.5
 *
 */
public interface AppCaseResultService extends BaseService<AppCaseResult> {
    int deleteByPrimaryKey(List<Integer> ids);

    AppCaseResult selectByPrimaryKey(Integer id);

    int insert(AppCaseResultService record);

    int updateByPrimaryKeySelective(CaseExcResultVO caseExcResultVO);

    int  updateBytetestCaseReport(TestCaseReport testCaseReport);

    int updateByPrimaryKey(Integer id);

    /**
     * 根据所选用例生成date数据
     * @param list
     * @return
     */
    List<Map<String,Object>> getAppCaseReportByCaseId(List<AppCase> list, Integer number);

    PageBean<CaseExcResultVO> listCaseExcProcess(PageBean<CaseExcResultVO> pageBean, CaseExcResultVO caseExcResultVO);
    PageBean<CaseExcResultVO> TestList(PageBean<CaseExcResultVO> pageBean, CaseExcResultVO caseExcResultVO);
    PageBean<RecordStepVO>findstep(PageBean<RecordStepVO> pageBean, RecordStepVO recordStepVO);
    List<RecordStepVO>findstep(RecordStepVO recordStepVO);
    List<TestCaseReportVO>CaseResults(TestCaseReportVO testCaseReportVO);
    /**
     * 根据CaseId删除结果相关的数据
     * @param
     * @return
     */

    int DelTestList(List<Integer> ids);

    /**
     * 根据CaseId删除结果相关的数据
     * @param caseId
     * @return
     */
    int deleteResultByCaseId(Integer caseId);

    /**
     * 根据CaseId查询结果集合
     */
    List<AppCaseResult> getResultIdsByCaseId(Integer caseId);
    /**
     * 查询用例步骤平均耗时
     *
     * @param surveyTaskId
     * @param province
     * @return
     */
    List<StepReportVO> caseStepReport(Integer caseId, Integer surveyTaskId, String province);
    List<SurveyTaskReportVO> appCaseReportList(Integer surveyTaskId, String province);
}
