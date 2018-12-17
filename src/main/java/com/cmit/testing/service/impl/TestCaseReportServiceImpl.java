package com.cmit.testing.service.impl;

import com.cmit.testing.dao.RecordStepMapper;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.entity.RecordStep;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.service.TestCaseReportService;
import com.cmit.testing.service.app.AppCaseResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TestCaseReportServiceImpl extends BaseServiceImpl<TestCaseReport> implements TestCaseReportService {

    @Autowired
    private TestCaseReportMapper testCaseReportMapper;

    @Autowired
    private RecordStepMapper recordStepMapper;

    @Autowired
    private AppCaseResultService appCaseResultService;


    /**
     * 获取步骤执行结果成功率
     * @param testCaseReport
     * @return
     */
    @Override
    public double getStepSuccessRate(TestCaseReport testCaseReport) {
        List<RecordStep> list = recordStepMapper.getScriptStepsByTestCaseReport(testCaseReport);
        int successNumber = 0;
        for(RecordStep recordStep : list){
            if(recordStep.getResult().equals("1")){
                successNumber++;
            }
        }
        return successNumber/list.size();
    }

    /**
     * 用例报告
     * @param testCaseReportVO
     * @return
     */
    @Override
    public List<TestCaseReportVO> findAll(TestCaseReportVO testCaseReportVO) {
        List<TestCaseReportVO> testCaseReportVOS = new ArrayList<>();
        List<TestCaseReportVO> appTestCaseReport = new ArrayList<>();
        List<TestCaseReportVO> webTestCaseReport = new ArrayList<>();

        if(null==testCaseReportVO.getType()){
            webTestCaseReport = testCaseReportMapper.findPage(testCaseReportVO);
            appTestCaseReport = appCaseResultService.CaseResults(testCaseReportVO);
            testCaseReportVOS.addAll(webTestCaseReport);//web用例报告
            testCaseReportVOS.addAll(appTestCaseReport);//app用例报告
        }else if(testCaseReportVO.getType().equals("web")) {
            webTestCaseReport = testCaseReportMapper.findPage(testCaseReportVO);
            testCaseReportVOS.addAll(webTestCaseReport);//web用例报告
        }else if(testCaseReportVO.getType().equals("app")) {
            appTestCaseReport = appCaseResultService.CaseResults(testCaseReportVO);
            testCaseReportVOS.addAll( appTestCaseReport);//app用例报告
        }
        return testCaseReportVOS;
    }

    @Override
	public List<TestCaseReport> getTestCaseReportByTestCaseIdAndExcuteResult(Integer id, Integer excuteResult) {
		return testCaseReportMapper.getTestCaseReportByTestCaseIdAndExcuteResult(id,excuteResult);
	}

    /**
     * 根据用例id查询用例报告
     * @param id
     * @return
     */
    @Override
    public List<TestCaseReport> getTestCaseReportByCaseId(Integer id ,Integer status) {

        return testCaseReportMapper.getTestCaseReportByCaseId(id,status);
    }
    /**
     * 根据用例id查询用例报告
     * @param id
     * @return
     */
    @Override
    public List<TestCaseReport> getTestCaseReportByTestCaseId(Integer id) {

        return testCaseReportMapper.getTestCaseReportByTestCaseId(id);
    }

    public List<TestCaseReport> getCaseReportByCaseIdAndExcuteNum(Integer id ,Integer number) {

        return testCaseReportMapper.getCaseReportByCaseIdAndExcuteNum(id,number);
    }

    @Override
    public int deleteByIds(Map<String, Object> map) {

        List<Map<String,Object>> data = (List<Map<String,Object>>)map.get("data");

        List<Integer> webIds = new ArrayList<>();
        List<Integer> appIds = new ArrayList<>();
        for (Map<String, Object> datum : data) {
            if("web".equals(datum.get("type").toString())){  //web
                webIds.add((Integer) datum.get("id"));
            }else if("app".equals(datum.get("type").toString())){  //app
                appIds.add((Integer) datum.get("id"));
            }

        }
        int t = 0;

        if(webIds.size()>0){
            t += testCaseReportMapper.deleteByIds(webIds); //删除web报告
            recordStepMapper.deleteByTestcaseReportIds(webIds);
        }
        if(appIds.size()>0){
            t=appCaseResultService.deleteByPrimaryKey(appIds);//删除app报告
        }

        return t;
    }

    /**
     * 根据用例id查询省份
     * @param testcastIds
     * @return
     */
    @Override
    public List<String> getProvinceByTestcaseIds(List<Integer> testcastIds) {
        return testCaseReportMapper.getProvinceByTestcaseIds(testcastIds);
    }

	@Override
	public TestCaseReport getTestCaseReportByOldTestcaseIdAndProvince(Integer oldTestcaseId, String province) {
		return testCaseReportMapper.getTestCaseReportByOldTestcaseIdAndProvince(oldTestcaseId, province);
	}

    @Override
    public List<TestCaseReportVO> getTestCaseReport4Zip(TestCaseReportVO testCaseReportVO) {
        return testCaseReportMapper.getTestCaseReport4Zip(testCaseReportVO);
    }
}
