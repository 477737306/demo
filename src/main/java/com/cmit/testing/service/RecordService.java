package com.cmit.testing.service;

import com.cmit.testing.entity.RecordStep;
import com.cmit.testing.entity.vo.CaseExecutionProcessVO;
import com.cmit.testing.entity.vo.RecordStepVO;
import com.cmit.testing.page.PageBean;
import java.util.List;
/**
 * Created by Suviky on 2018/7/31 17:05
 */
public interface RecordService extends BaseService<RecordStep>{
    boolean insertRecord(List<RecordStep> list);

    //获取依赖用例的执行结果
    List<String> getInfoFromDependCase(Integer id);

    /**
     * 步骤详情
     * @param recordStepVO
     * @return
     */
    List<RecordStepVO> findAll(RecordStepVO recordStepVO);


        /**
         * 根据报告id删除
         * @param testcaseReportId
         * @return
         */
    int deleteByTestcaseReportId(Integer testcaseReportId);
    
    /**
     * 根据用例id和手机号查询用例的执行过程 
     * @param testcastId 用例ID
     * @param phoneNum   手机号码
     * @return
     */
    List<CaseExecutionProcessVO> getCaseExecutionProcess(Integer testcastId, String phoneNum);
}
