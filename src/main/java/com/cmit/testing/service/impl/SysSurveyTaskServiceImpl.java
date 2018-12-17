package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.ParameterMapper;
import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.dao.SysSurveyTaskMapper;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.entity.app.ExecuteCaseBO;
import com.cmit.testing.entity.vo.StepReportVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.ScriptStepService;
import com.cmit.testing.service.SysSurveyTaskService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.TaskService;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.ThreadUtils;
import com.cmit.testing.utils.ToolUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysSurveyTaskServiceImpl extends BaseServiceImpl<SysSurveyTask> implements SysSurveyTaskService {
    @Autowired
    private SysSurveyTaskMapper sysSurveyTaskMapper;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private ParameterMapper parameterMapper;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private ScriptStepService scriptStepService;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;

    @Autowired
    private AppCaseResultMapper appCaseResultMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppCaseService appCaseService;

    @Override
    public PageBean<SysSurveyTask> findPage(int currentPage, int pageSize, String taskName, String taskIss) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<SysSurveyTask> sysSurveyTasks = sysSurveyTaskMapper.findByPage(taskName, taskIss);
        PageBean<SysSurveyTask> page_ = new PageBean<>(currentPage, pageSize, (int) page.getTotal());
        page_.setItems(sysSurveyTasks);
        return page_;
    }

    @Override
    public  List<SysSurveyTask> findAll( String taskName, String taskIss) {
        return sysSurveyTaskMapper.findByPage(taskName, taskIss);
    }

    @Override
    public List<SurveyTaskReportVO> caseReportList(Integer surveyTaskId, String province, Integer type) {
        //解析web端 用例Id
        SysSurveyTask sysSurveyTask = sysSurveyTaskMapper.selectByPrimaryKey(surveyTaskId);
        if (sysSurveyTask == null) {
            return Collections.emptyList();
        }
        String json = sysSurveyTask.getCaseIds();
        JSONObject jsonObject = JsonUtil.parseJsonObject(json);
        List<Integer> testcaseIdList = (List<Integer>) jsonObject.get("web");
        if (testcaseIdList.size() == 0) {
            return Collections.emptyList();
        }
        //不分省
        if (0 == type) {
            return sysSurveyTaskMapper.caseReportList(testcaseIdList, surveyTaskId, province);
        } else if (1 == type) {
            //分省
            return sysSurveyTaskMapper.caseProvinceReportList(testcaseIdList, surveyTaskId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<StepReportVO> caseStepReport(String testCaseId, Integer surveyTaskId, String province) {
        return sysSurveyTaskMapper.caseStepReport(testCaseId, surveyTaskId, province);
    }

    @Override
    public String getJsonResult(SysSurveyTask sysSurveyTask,Map caseIds) {
        Map<String, Object> rootMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();
        List<Map<String, Object>> caseList = new ArrayList<>();
        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> bodays = new HashMap<>();
        //web
        caseList =getRootResult(sysSurveyTask, caseIds);
        //app
        caseList.addAll(appCaseService.syncSysSurveyTaskData(sysSurveyTask, caseIds));

        if (CollectionUtils.isEmpty(caseList))
        {
            // 无用例报告生成
            return null;
        }
        bodyMap.put("CASE", caseList);
        headerMap.put("IP", "" + sysSurveyTask.getIp());
        if (sysSurveyTask.getTaskId() == null)
            headerMap.put("IP", "");


        bodyMap.put("TASK_ID", sysSurveyTask.getTaskId());
        if (sysSurveyTask.getTaskId() == null)
            bodyMap.put("TASK_ID", "");
        headerMap.put("TIME", new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(sysSurveyTask.getTime()));
        rootMap.put("BODY", bodyMap);
        rootMap.put("HEADER", headerMap);
        bodays.put("ROOT", rootMap);

        String jsonResult = JSONObject.toJSONString(bodays);

        return jsonResult;
    }

    @Override
    public void surveyTaskExcute(Integer surveyTaskid, Map<String, List<Integer>> caseMap) {
        List<Integer> webList = caseMap.get("web");
        System.out.println(webList.toString());
        for (Integer id : webList) {
            List<TestCase> testCase1 = testCaseService.getTestCaseBySurTaskIdAndCaseId(surveyTaskid, id);
            if (null != testCase1 && testCase1.size() > 0) {
                for (TestCase testCase : testCase1) {
                    testCaseService.deleteByPrimaryKey(testCase.getId());
                }
            }
        }
        //重新排好序的testCase 集合
        List<Map<String, List<TestCase>>> reorderList = ToolUtil.reorderByCaseIds(caseMap);
        for (Map<String, List<TestCase>> tcMap : reorderList){
            List<TestCase> list1 = tcMap.get("web");
            List<TestCase> list2 = tcMap.get("app");
            List<Integer> webCaseIds = new ArrayList<>();
            List<Integer> copyCaseIds = new ArrayList<>();
            if (null != list1 && list1.size() > 0){
                //web 用例执行
                for (TestCase tc : list1){
                	tc.setSurveyTaskId(surveyTaskid);
                	// 生成用例副本
                    TestCase t1 = scriptStepService.executeTestCaseBefore(tc, false);
                    // 执行用例副本
					scriptStepService.executeTestCase(t1);
			        webCaseIds.add(t1.getId());
                }

            }
            if(null != list2 && list2.size() > 0)
            {
                List<Integer> oldCaseIds = new ArrayList<>();
                // app用例执行
                for (TestCase testCase : list2)
                {
                    oldCaseIds.add(testCase.getId());
                }
                ExecuteCaseBO caseBO = new ExecuteCaseBO();
                caseBO.setCaseIds(oldCaseIds);
                caseBO.setSurveyTaskId(surveyTaskid);
                caseBO.setType(AppConstants.EXECUTE_TYPE_SURVEYTASK);
                caseBO.setRealExecuteNum(1);
                copyCaseIds = taskService.commonExecute(caseBO);
            }
            for (Integer caseId : copyCaseIds)
            {
                // 副本ID
                while (!appCaseService.getAppCaseFinishStatus(caseId))
                {
                    Thread.yield();
                }
            }
            for (Integer webCaseId : webCaseIds) {
				// 判断用例是否执行结束
				while (2 != ThreadUtils.getTestcaseExecuteStatus(webCaseId)) {
					Thread.yield();
				}
				// 更改用例与用例副本的状态
				scriptStepService.executeTestCaseAfter(testCaseService.selectByPrimaryKey(webCaseId));
			}
        }
    }

    public List<Map<String, Object>> getRootResult(SysSurveyTask sysSurveyTask,Map caseIds) {

        Map<String, Object> bodyMap = new HashMap<>();
        List<Map<String, Object>> caseList = new ArrayList<>();
        List<Map<String, String>> proList = new ArrayList<>();
        Map<String, String> proMap = new HashMap<>();
        Map<String, String> proMap2 = new HashMap<>();
        Map<String, Object> caseMap = new HashMap<>();
        List webCaseList= (List) caseIds.get("web");

        List<SurveyTaskReportVO> list = this.caseReportList(sysSurveyTask.getId(), null, 0);
        //无用例报告生成
        if (null == list || list.size() == 0) {
            return Collections.emptyList();
        }


        String json = sysSurveyTask.getCaseIds();
        JSONObject jsonObject = JsonUtil.parseJsonObject(json);
        List<Integer> testcaseIdList = (List<Integer>) jsonObject.get("web");
        List<SurveyTaskReportVO> list2 = this.caseReportList(sysSurveyTask.getId(), null, 1);

        for (Integer integer : testcaseIdList) {
            if(webCaseList.size()>0){
            if(!webCaseList.contains(integer)){
                continue;
            }
          }
            List<String> provinceList = new ArrayList<>();
            List<String> provinceList1 = new ArrayList<>();
            List<String> phoneList = new ArrayList<>();
            TestCase testCase = testCaseService.selectByPrimaryKey(integer);
            List<Parameter> parameterList = parameterMapper.getByTaseCaseId(integer);
            if (null == parameterList || parameterList.size()<1){
                 phoneList = testCaseService.getPhoneListByTestCase(testCase);
            }else {
                Parameter parameter = parameterList.get(0);
                String phones = parameter.getValue();
                phoneList = Arrays.asList(phones.split(","));
            }

            SimCard phone = simCardMapper.getByPhone(phoneList.get(0));
            if (phone != null) {    //有手机号码
                if (list2 != null && list2.size() > 0) {
                    caseMap = new HashMap<>();
                    proList = new ArrayList<>();

                    for (SurveyTaskReportVO taskReportVO : list) {
                        if (taskReportVO.getTestcaseId().equals(integer)) {
                            proMap2 = getProList(taskReportVO, sysSurveyTask, "1", "全国平均");
                            proList.add(proMap2);
                            break;
                        }
                    }

                    for (SurveyTaskReportVO surveyTaskReport : list2) {
                        if (surveyTaskReport.getTestcaseId().equals(integer)) {
                            proMap = getProList(surveyTaskReport, sysSurveyTask, "2", "" + surveyTaskReport.getProvince());
                            proList.add(proMap);
                            //添加省列表
//                            provinceList1.add(surveyTaskReport.getProvince());
                        }
                    }
//                    //遍历手机号添加省
//                    for (String phoneNum : arr) {
//                        List<TestCaseReport> testCaseReport = testCaseReportMapper.getTestCaseReportByTestCaseIdAndPhone(integer, phoneNum);
//                        if (testCaseReport == null || testCaseReport.size() == 0) {
//                            SimCard byPhone = simCardMapper.getByPhone(phoneNum);
//                            if (!provinceList1.contains(byPhone.getProvince())) {
//                                if (!provinceList.contains(byPhone.getProvince()))
//                                    //去重
//                                    provinceList.add(byPhone.getProvince());
//                            }
//                        }
//                    }
//                    for (String s : provinceList) {
//                        SurveyTaskReportVO surveyTaskReportVO = new SurveyTaskReportVO();
//                        surveyTaskReportVO.setMsgConsumeTimeScore("0");
//                        surveyTaskReportVO.setInputScore("0");
//                        surveyTaskReportVO.setClickScore("0");
//                        surveyTaskReportVO.setAvgConsumeTime("0");
//                        surveyTaskReportVO.setAvgClickNum("0");
//                        surveyTaskReportVO.setAvgInputNum("0");
//                        surveyTaskReportVO.setAvgMsgConsumeTime("0");
//                        surveyTaskReportVO.setConsumeTimeScore("0");
//                        surveyTaskReportVO.setScore("0");
//                        surveyTaskReportVO.setSuccessRote("0");
//                        proMap = getProList(surveyTaskReportVO, sysSurveyTask, "2", s);
//                        proList.add(proMap);
//                    }
                    //fakeDATA
                    caseMap.put("CASE_NAME", "" + testCase.getName());
                    caseMap.put("PRO_LIST", proList);
                    caseList.add(caseMap);
                }
            }
            //没有手机号
            else {
                caseMap = new HashMap<>();
                proList = new ArrayList<>();
                for (SurveyTaskReportVO surveyTaskReportVO : list) {
                    if (integer.equals(surveyTaskReportVO.getTestcaseId())) {
                        proMap = getProList(surveyTaskReportVO, sysSurveyTask, "0", "不区分省");
                        proList.add(proMap);
                        caseMap.put("CASE_NAME", "" + surveyTaskReportVO.getTestcaseName());
                        caseMap.put("PRO_LIST", proList);
                        caseList.add(caseMap);
                        break;
                    }
                }
            }
        }






        return caseList;
    }

    public Map<String, String> getProList(SurveyTaskReportVO surveyTaskReportVO, SysSurveyTask sysSurveyTask, String proType, String proName) {
	/*	List<Map<String ,Object>> caseList = new ArrayList<>();

    	List<Map<String ,String>> proList = new ArrayList<>();
		Map<String ,Object> caseMap = new HashMap<>();*/

        Map<String, String> proMap = new HashMap<>();

        if ("1".equals(sysSurveyTask.getTgAccuracy())) {
            proMap.put("TG_ACCURACY", "" + surveyTaskReportVO.getSuccessRote());
            proMap.put("SC_ACCURACY", "" + surveyTaskReportVO.getScore());
        }
        if ("1".equals(sysSurveyTask.getTgClicksAvg())) {
            proMap.put("TG_CLICKS_AVG", "" + surveyTaskReportVO.getAvgClickNum());
            proMap.put("SC_CLICKS_AVG", "" + surveyTaskReportVO.getClickScore());
        }
        if ("1".equals(sysSurveyTask.getTgInputAvg())) {
            proMap.put("TG_INPUT_AVG", "" + surveyTaskReportVO.getAvgInputNum());
            proMap.put("SC_INPUT_AVG", "" + surveyTaskReportVO.getInputScore());
        }
        if ("1".equals(sysSurveyTask.getTgNoteAvg())) {
            proMap.put("TG_NOTE_AVG", "" + surveyTaskReportVO.getAvgMsgConsumeTime());
            proMap.put("SC_NOTE_AVG", "" + surveyTaskReportVO.getMsgConsumeTimeScore());
        }
        if ("1".equals(sysSurveyTask.getTgStepAvg())) {
            proMap.put("TG_STEP_AVG", "" + surveyTaskReportVO.getAvgConsumeTime());
            proMap.put("SC_STEP_AVG", surveyTaskReportVO.getConsumeTimeScore());
        }

        proMap.put("PRO_TYPE", proType);
        proMap.put("PRO_NAME", "" + proName);

        /*proList.add(proMap);*/
			/*caseMap.put("CASE_NAME",surveyTaskReportVO.getTestcaseName());
			caseMap.put("PRO_LIST",proList);*/

//		cssaseList.add(caseMap);
        return proMap;
    }

    @Override
    public SysSurveyTask selectByTaskId(Integer taskId) {
        return sysSurveyTaskMapper.selectByTaskId(taskId);
    }

    @Override
    public int deleteByTaskId(Integer taskId) {
        return sysSurveyTaskMapper.deleteByTaskId(taskId);
    }

    @Override
    public int insert(SysSurveyTask surveyTask, Integer type) {
        if (type == 0) {
            surveyTask.setCaseState("0");
            surveyTask.setFeedback("0");
        } else if (type == 1) {
            String careatBy = getShiroUser().getName();
            surveyTask.setTime(new Date());
            surveyTask.setTaskIss(careatBy);
            surveyTask.setCaseState("0");
            surveyTask.setFeedback("0");
        }

        return sysSurveyTaskMapper.insert(surveyTask);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return sysSurveyTaskMapper.deleteByIds(ids);
    }

}
