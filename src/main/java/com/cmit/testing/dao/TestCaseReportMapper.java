package com.cmit.testing.dao;

import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TestCaseReportMapper extends BaseMapper<TestCaseReport> {
    /**
     * 用例报告分页
     * @param testCaseReportVO
     * @return
     */
    List<TestCaseReportVO> findPage(TestCaseReportVO testCaseReportVO);

    Map<String,Long> testcaseReportCount();
    
    List<TestCaseReport> getTestCaseReportByTestCaseIdAndExcuteResult(@Param("id") Integer id, @Param("excuteResult") Integer excuteResult);

    /**
     * 根据用例id查询用例报告
     * @param id
     * @return
     */
    List<TestCaseReport> getTestCaseReportByCaseId(@Param("id") Integer id, @Param("status") Integer status);

    List<TestCaseReport> getCaseReportByCaseIdAndExcuteNum(@Param("id") Integer id, @Param("number") Integer number);
    /**
     * 获取web执行用例的手机号码
     * @param caseId 用例ID
     * @param executeStatus 0-成功；1-失败
     * @return
     */
    List<String> getWebPhoneNumList(@Param("caseId") Integer caseId, @Param("executeStatus") Integer executeStatus);


    /**
     * 根据用例id批量删除
     * @param testcastIds
     * @return
     */
    int deleteByTestcastIds(@Param("testcastIds") List<Integer> testcastIds);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据用例id查询省份
     * @param testcastIds
     * @return
     */
    List<String> getProvinceByTestcaseIds(@Param("testcastIds") List<Integer> testcastIds);

    /**
     * 根据用例id查询报告id
     * @param ids
     * @return
     */
    List<TestCaseReport> getIdsByTestCaseIds(@Param("ids") List<Integer> ids);

    /**
     * 查询用例执行成功的结果
     * @param id
     * @return
     */
    List<TestCaseReport> getCaseReportSuccessByCaseId(@Param("id") Integer id);

    /**
     * 通过用例id获取报告的成功数和失败数
     * @param testcastId
     * @return
     */
    Map<String,Long> getSuccessNumAndFailureNumByTestcastId(@Param("testcastId") Integer testcastId);

    /**
     * 获取缺陷总数
     */
    Integer getDefectsCount();

    /**
     *根据用例id和手机号查找报告
     */
    List<TestCaseReport> getTestCaseReportByTestCaseIdAndPhone(@Param("id") Integer id, @Param("phoneNum") String phoneNum);

    List<TestCaseReport> getTestCaseReportByFewDays(@Param("day") String day, @Param("date") String date);

    List<TestCaseReport> getTestCaseReportByTestCaseId(@Param("id") Integer id);

    /**
	 * 根据原用例id和省份获取最新的用例报告
	 * 
	 * @param oldTestcaseId
	 * @param province
	 * @return
	 */
    TestCaseReport getTestCaseReportByOldTestcaseIdAndProvince(@Param("oldTestcaseId") Integer oldTestcaseId, @Param("province") String province);

    List<TestCaseReportVO> getTestCaseReport4Zip(TestCaseReportVO testCaseReportVO);

}