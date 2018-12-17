package com.cmit.testing.service;

import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

public interface TestCaseReportService extends BaseService<TestCaseReport>{

    /**
     * 获取步骤执行结果成功率
     * @param testCaseReport
     * @return
     */
    double getStepSuccessRate(TestCaseReport testCaseReport);

    /**
     * 用例报告
     * @param testCaseReportVO
     * @return
     */
    List<TestCaseReportVO> findAll(TestCaseReportVO testCaseReportVO);

    List<TestCaseReport> getTestCaseReportByTestCaseIdAndExcuteResult(Integer id, Integer excuteResult);

    /**
     * 根据用例id查询用例报告
     * @param id
     * @return
     */
    List<TestCaseReport> getTestCaseReportByCaseId(Integer id, Integer status);

    /**
     * 根据用例id和省份轮次查询报告
     * @param id
     * @param status
     * @return
     */
    List<TestCaseReport> getCaseReportByCaseIdAndExcuteNum(Integer id, Integer status);

    /**
     * 根据报告id删除
     * @param map
     * @return
     */
    int deleteByIds(Map<String, Object> map);

    /**
     * 根据用例id查询省份
     * @param testcastIds
     * @return
     */
    List<String> getProvinceByTestcaseIds(List<Integer> testcastIds);

    List<TestCaseReport> getTestCaseReportByTestCaseId(Integer id);

	/**
	 * 根据原用例id和省份获取最新的用例报告
	 * 
	 * @param oldTestcaseId
	 * @param province
	 * @return
	 */
	TestCaseReport getTestCaseReportByOldTestcaseIdAndProvince(Integer oldTestcaseId, String province);

	List<TestCaseReportVO> getTestCaseReport4Zip(TestCaseReportVO testCaseReportVO);
}
