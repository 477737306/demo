package com.cmit.testing.dao;

import com.cmit.testing.entity.RecordStep;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.vo.CaseExecutionProcessVO;
import com.cmit.testing.entity.vo.RecordStepVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RecordStepMapper extends BaseMapper<RecordStep>{
    int deleteByPrimaryKey(Integer id);

    int insert(RecordStep record);

    int insertSelective(RecordStep record);

    int batchInsert(List<RecordStep> list);

    RecordStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordStep record);

    int updateByPrimaryKey(RecordStep record);

    List<String> getInfoFromDependCase(Integer id);

    /**
     * 根据用例报告id查询步骤详情
     * @param testCaseReport
     * @return
     */
    List<RecordStep> getScriptStepsByTestCaseReport(TestCaseReport testCaseReport);

    /**
     * 根据报告id删除
     * @param testcaseReportIds
     * @return
     */
    int deleteByTestcaseReportIds(@Param("testcaseReportIds") List<Integer> testcaseReportIds);

    /**
     * 步骤详情报告分页
     * @param recordStepVO
     * @return
     */
    List<RecordStepVO> findPage(RecordStepVO recordStepVO);
    
    /**
     * 根据用例id和手机号查询用例的执行过程 
     * @param testcastId 用例id
     * @param phoneNum   手机号码
     * @return
     */
    List<CaseExecutionProcessVO> getCaseExecutionProcess(@Param("testcastId") Integer testcastId, @Param("phoneNum") String phoneNum);
}