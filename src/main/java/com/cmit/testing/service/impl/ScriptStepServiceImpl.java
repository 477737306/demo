package com.cmit.testing.service.impl;

import com.cmit.testing.common.WebConstant;
import com.cmit.testing.common.thread.ScriptStepScreenshotExecutionThread;
import com.cmit.testing.dao.ParameterMapper;
import com.cmit.testing.dao.ScriptStepMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.enums.ResultEnum;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.service.ProjectMenuService;
import com.cmit.testing.service.ScriptStepService;
import com.cmit.testing.service.SimCardService;
import com.cmit.testing.service.TestCaseReportService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.ThreadUtils;
import com.cmit.testing.utils.ToolUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class ScriptStepServiceImpl extends BaseServiceImpl<ScriptStep> implements ScriptStepService {

    @Autowired
    private ScriptStepMapper scriptStepMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private SimCardService simCardService;

    @Autowired
    private TestCaseService caseService;

    @Autowired
    private TestCaseReportService caseReportService;

    @Autowired
    private ProjectMenuService projectMenuService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(ScriptStep record) {
        return 0;
    }

    @Override
    public int insertSelective(ScriptStep record) {
        return 0;
    }

    @Override
    public ScriptStep selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(ScriptStep record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(ScriptStep record) {
        return 0;
    }

    @Override
    public List<ScriptStep> selectByScriptId(Integer scriptId) {
        return scriptStepMapper.selectByScriptId(scriptId, null);
    }

    @Override
    public int deleteByModelScript(Integer id) {
        return 0;
    }

    /**
     * 封装参数
     *
     * @param scriptSteps 脚本步骤
     * @param phone       电话号码
     * @return
     */
    public List<ScriptStep> encapsulationParameters(List<ScriptStep> scriptSteps, List<Parameter> mList, List<Parameter> wList, String phone) {
        List<ScriptStep> sList = new ArrayList<>();  // 步骤数组
        for (int j = 0; j < scriptSteps.size(); j++) {  // 循环拼接步骤参数
            ScriptStep step = null;  // 取对象
            try {
                step = (ScriptStep) scriptSteps.get(j).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (scriptSteps.get(j).getParamKey() != null) {
                if (scriptSteps.get(j).getParamType().equals(1)) { // 判断类型
                    if (mList.size() > 0) {
                        if (scriptSteps.get(j).getParamKey().equals(mList.get(0).getParam())) { // 相等传参
                            step.setParamValue(phone);
                        } else {
                            for (int k = 0; k < mList.size(); k++) { // 判断单个是否相等
                                if (step.getParamKey().equals(mList.get(k).getParam())) {
                                    step.setParamValue(mList.get(k).getValue());  // 相等传参
                                }
                            }
                        }
                    }
                } else if (scriptSteps.get(j).getParamType().equals(2)) { // 取全局变量  字典表 dic
                    step.setParamValue(wList.get(0).getValue());
                } else if (scriptSteps.get(j).getParamType().equals(3)) {  // 拼接服务密码
                    SimCard sm = simCardService.getByPhone(phone);
                    if (sm != null) {
                        if (StringUtils.isNotEmpty(sm.getServiceCode()))
                            step.setParamValue(sm.getServiceCode());
                    }
                }
            }
            sList.add(step);
        }
        return sList;
    }

    /**
     * 执行单个用例
     *
     * @param testCase
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestCase executeTestCase(TestCase testCase) {
    	// 获取用例的手机号
    	List<String> list = caseService.getPhoneListByTestCase(testCase);
    	if (null == list || list.size() == 0) {
    		testCase.setIsFinish(0);
            testCase.setExecuteNumber(0);
            caseService.updateByPrimaryKey(testCase);
            throw new TestSystemException(ResultEnum.TESTCASE_PHONE_NOT_EXIST);
        }

    	// 设置用例参数为1-多参，再从参数表中查询数据
        Parameter param = new Parameter();
        param.setTestcaseId(testCase.getOldTestcaseId());
        param.setMultiFlag(1); // 1-多参
        List<Parameter> mList = parameterMapper.findParameter(param);// 多参变量
        // 设置用例参数为2-全局变量,再从参数表中获取数据
        param.setMultiFlag(2);// 2-全局变量
        List<Parameter> wList = parameterMapper.findParameter(param);// 全局变量

        // 获取用例的脚本步骤
        List<ScriptStep> scriptSteps = scriptStepMapper.selectByScriptId(testCase.getScriptId(), null);
        if (list.size() > 0 || (wList != null && wList.size() > 0)) {
            //获取执行次数
            Integer retry = testCase.getRetry();
            if (null == retry) {
                retry = 1;
            }
            if(list.size() > 0){
                testCase.setExecuteNumber(testCase.getExecuteNumber() + list.size() * retry);
            }
            if(wList.size() > 0){
                testCase.setExecuteNumber(testCase.getExecuteNumber() + wList.size() * retry);
            }
            for (int j = 0; j < retry; j++) {
                // 遍历手机号码
                for (int i = 0; i < list.size(); i++) {
                    // 步骤数组
                    List<ScriptStep> sList = new ArrayList<>();
                    sList = encapsulationParameters(scriptSteps, mList, wList, list.get(i));
                    runScriptStep(sList, testCase, list.get(i));
                }
            }
        }
        return testCase;
    }

    /**
     * 脚本执行
     *
     * @param scriptSteps
     * @param testCase
     * @param number
     */
    public void runScriptStep(List<ScriptStep> scriptSteps, TestCase testCase, String number) {
       /* ScriptStepExecutionThread task = new ScriptStepExecutionThread(scriptSteps,testCase,number);
        new Thread(task).start();*/
        //添加线程任务到队列，自动执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                Callable task = new ScriptStepScreenshotExecutionThread(scriptSteps, testCase, number);
                ThreadUtils.addTask(task);
            }
        }).start();
    }

    /**
     * 执行用例前的操作：设置原用例状态为1-执行中,更改相关业务数据,生成用例副本
     * @param testCase 原用例
     * @param isExecuteBusiness 是否业务执行(业务执行生成的用例副本的业务id保留为原业务id,其他用例副本业务id设置为空)
     * @return 用例副本
     */
    public TestCase executeTestCaseBefore(TestCase testCase, boolean isExecuteBusiness) {
    	// 先记录传入的批量任务id与众测任务id,原用例不保存批量任务id与众测任务id,但生成的用例副本要保存相应的批量任务id与众测任务id
    	Integer sysTaskId = null;
    	Integer surveyTaskId = null;
    	if (ToolUtil.isNum(testCase.getSysTaskId())) {
    		sysTaskId = testCase.getSysTaskId();    		
    	}
    	if (ToolUtil.isNum(testCase.getSurveyTaskId())) {
    		surveyTaskId = testCase.getSurveyTaskId();
    	}

    	// 设置用例执行状态为1-执行中,原用例不保存批量任务id与众测任务id
    	testCase.setIsFinish(WebConstant.TestCaseExecuteStatus.EXECUTING);
    	testCase.setSysTaskId(null);
    	testCase.setSurveyTaskId(null);
    	caseService.updateByPrimaryKeySelective(testCase);

    	// 用例执行前更改相关业务数据
    	projectMenuService.exeBeforeUpdateData(testCase.getBusinessId());

    	// 设定批次号
    	int excuteNum = 1;// 批次号默认为1
    	Map<String, Integer> maxExcuteNum = caseService.getTestCaseMaxExcuteNum(testCase.getId());
    	// 如果该用例存在副本批次号,说明该用例执行过
    	if (ToolUtil.isNotEmpty(maxExcuteNum)) {
    		if (testCase.getIsMerge() == 1) {// 1-不合并
    			excuteNum = Integer.parseInt(maxExcuteNum.get("maxnum").toString()) + 1;
    		} else if (testCase.getIsMerge() == 0) {// 0-合并
    			excuteNum = Integer.parseInt(maxExcuteNum.get("maxnum").toString());
			} 
    	}
    	if (!isExecuteBusiness) {
    		testCase.setBusinessId(null);
    	}
    	testCase.setSysTaskId(sysTaskId);
    	testCase.setSurveyTaskId(surveyTaskId);
    	testCase.setExcuteNum(excuteNum);
    	testCase.setOldTestcaseId(testCase.getId());
    	testCase.setId(null);
    	testCase.setCreateTime(new Date());
    	testCase.setUpdateTime(new Date());
    	// 保存一份新的用例,即生成原用例的副本
        caseService.insert(testCase);
		return testCase;
    }

    /**
     * 执行用例后的操作：更新当前用例(即原用例副本)的执行状态(0-执行完成),更新原用例的执行状态(0-执行完成),更改相关业务数据
     * @param testCase 用例副本
     */
    public void executeTestCaseAfter(TestCase testCase) {
    	// 获取当前用例执行报告的成功数与失败数
        List<TestCaseReport> successCaseReportList = caseReportService.getTestCaseReportByTestCaseIdAndExcuteResult(testCase.getId(), 0);
        List<TestCaseReport> faildCaseReportList = caseReportService.getTestCaseReportByTestCaseIdAndExcuteResult(testCase.getId(), 1);
        int successCaseReportNumber = successCaseReportList.size();
        int faildCaseReportNumber = faildCaseReportList.size();

        // 更新当前用例(即原用例副本)的执行状态(0-执行完成)、成功数、失败数、成功率
        testCase.setSuccessNum(successCaseReportNumber);
        testCase.setFailureNum(faildCaseReportNumber);
        testCase.setIsFinish(WebConstant.TestCaseExecuteStatus.COMPLETED);
        testCase.setUpdateTime(new Date());
        testCase.setSuccessRate(ToolUtil.numericalPrecision(successCaseReportNumber * 1d / (successCaseReportNumber + faildCaseReportNumber), 2));			
        caseService.updateByPrimaryKey(testCase);

        // 更新原用例的执行状态(0-执行完成)、成功数、失败数、成功率
        TestCase oldTestCase = caseService.selectByPrimaryKey(testCase.getOldTestcaseId());
        int success = oldTestCase.getSuccessNum() + successCaseReportNumber; // 最新成功数 = 原用例成功数 + 当前用例副本执行成功数
        int faild = oldTestCase.getFailureNum() + faildCaseReportNumber; // 最新失败数 = 原用例失败数 + 当前用例副本执行失败数
        double success_rate = ToolUtil.numericalPrecision(success * 1d / (faild + success), 2);// 成功率
        oldTestCase.setIsFinish(WebConstant.TestCaseExecuteStatus.COMPLETED);
        oldTestCase.setSuccessNum(success);
        oldTestCase.setFailureNum(faild);
        oldTestCase.setSuccessRate(success_rate);
        oldTestCase.setUpdateTime(new Date());
        caseService.updateByPrimaryKey(oldTestCase);

        // 用例执行完成后更改相关业务数据
        projectMenuService.exeAfterUpdateData(oldTestCase.getBusinessId());
    }
}
