package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.dao.SysTaskMapper;
import com.cmit.testing.dao.TestCaseMapper;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.entity.SysTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.ExecuteCaseBO;
import com.cmit.testing.entity.vo.CommonCaseVO;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.TaskService;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.ThreadUtils;
import com.cmit.testing.utils.ToolUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysTaskServiceImpl extends BaseServiceImpl<SysTask> implements SysTaskService {

	@Autowired
	private SysTaskMapper sysTaskMapper;

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestCaseMapper testCaseMapper;

	@Autowired
	private ScriptStepService scriptStepService;

	@Autowired
	private SysSurveyTaskService sysSurveyTaskService;

	@Autowired
	private TestCaseReportMapper testCaseReportMapper;
	@Autowired
	private TestCaseReportService caseReportService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AppCaseResultService appCaseResultService;
    @Autowired
	private AppCaseResultMapper appCaseResultMapper;
    @Autowired
	private AppCaseService appCaseService;
	/**
	 * 根据任务id查询用例
	 *
	 * @param id
	 * @return
	 */
	@Override
	public PageBean<TestCase> getTaskReportById(PageBean<TestCase> pageBean, Integer id) {
		return testCaseService.getTestCaseByTaskId(pageBean, id);
	}

    /**
     * 根据任务id生成报告
     * @param id
     * @return
     */
    @Override
    public List<Map<String,Object>> getTestCaseReportByTaskId(Integer id) {
        List<TestCase> list = testCaseMapper.getTestCaseByTaskId(id);
//        return testCaseService.getTestCaseReportByTaskId(list);
		return null;
    }

    /**
     * 根据所选用例ids生成报告
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, Object>> getTestCaseReportByCaseIds(String ids) {
        String[] arr = ids.split(",");
        List<TestCase> list = new ArrayList<>();
        for(int i = 0 ; i < arr.length ; i++){
            TestCase testCase = testCaseMapper.getTestCaseByCaseId(Integer.parseInt(arr[i]));
            list.add(testCase);
        }
//        return testCaseService.getTestCaseReportByTaskId(list);
		return null;
    }

	@Override
	public List<SysTask> getAll() {
		List<SysTask> list = sysTaskMapper.getAll(new SysTask());
		return list;
	}

	@Override
	public void executeTask(SysTask sysTask) {
		String caseIds = sysTask.getIds();
		Map<String, List<Integer>> caseMap =(Map<String, List<Integer>>) JSON.parse(caseIds);
		List<Map<String, List<TestCase>>> reorderList = ToolUtil.reorderByCaseIds(caseMap);
		for (Map<String, List<TestCase>> tcMap : reorderList){
			List<TestCase> list1 = tcMap.get("web");
			List<TestCase> list2 = tcMap.get("app");
			List<Integer> webCaseIds = new ArrayList<>();
			List<Integer> copyCaseIds = new ArrayList<>();
			if (null != list1 && list1.size() > 0){
				//web 用例执行
				for (TestCase tc : list1){
					tc.setSysTaskId(sysTask.getId());
					// 生成用例副本
					TestCase t1 = scriptStepService.executeTestCaseBefore(tc, false);
					// 执行用例副本
					scriptStepService.executeTestCase(t1);
			        webCaseIds.add(t1.getId());
				}
			}
			if(null != list2 && list2.size() > 0)
			{
				// app用例执行
				List<Integer> oldCaseIds = new ArrayList<>();
				for (TestCase testCase : list2)
				{
					oldCaseIds.add(testCase.getId());
				}
				ExecuteCaseBO caseBO = new ExecuteCaseBO();
				caseBO.setCaseIds(oldCaseIds);
				caseBO.setSysTaskId(sysTask.getId());
				// 批量执行
				caseBO.setType(AppConstants.EXECUTE_TYPE_BATCHTASK);
				caseBO.setRealExecuteNum(1);
				copyCaseIds = taskService.commonExecute(caseBO);
			}
			for (Integer caseId : copyCaseIds)
			{
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
    /**
     * 分页查询所有任务
     * @param pageBean
     * @param sysTask
     * @return
     */
    @Override
    public PageBean<SysTask> getAll(PageBean<SysTask> pageBean , SysTask sysTask) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<SysTask> list = sysTaskMapper.getAll(sysTask);
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

	/**
	 * 任务用例报告
	 * @param taskId 任务id
	 * @param province 省份
	 * @return
	 */
	@Override
	public List<TestCaseReportVO> findReportByTaskId(Integer taskId,String province) {
		List<TestCaseReportVO> testCaseReportVOList = new ArrayList<>();
		TestCaseReportVO testCaseReportVO = new TestCaseReportVO();
		testCaseReportVO.setProvince(province);
		SysSurveyTask sysSurveyTask = sysSurveyTaskService.selectByPrimaryKey(taskId);
		String caseIds = sysSurveyTask.getCaseIds();
		Map<String,List<Integer>> map =(Map<String,List<Integer>>) JSON.parse(caseIds);
		testCaseReportVO.setTestcastIds(map.get("web"));
		testCaseReportVO.setSurveyTaskId(taskId);
		//web
		testCaseReportVOList.addAll(testCaseReportMapper.findPage(testCaseReportVO));
		//app
		testCaseReportVOList.addAll(appCaseResultService.CaseResults(testCaseReportVO));
		return testCaseReportVOList;
	}

	/**
	 * 根据任务id查询省份
	 * @param id
	 * @return
	 */
	@Override
	public HashSet<String> getProviceCaseByTaskId(Integer id) {
		SysSurveyTask sysSurveyTask = sysSurveyTaskService.selectByPrimaryKey(id);
		HashSet<String> hashSet= new HashSet<>();
		String caseIds = sysSurveyTask.getCaseIds();
		Map<String,List<Integer>> map =(Map<String,List<Integer>>) JSON.parse(caseIds);
		//WEB
		if(null != map.get("web") && map.get("web").size() >0)
		hashSet.addAll(testCaseReportMapper.getProvinceByTestcaseIds(map.get("web")));
		//app
		if(null != map.get("app") && map.get("app").size() >0)
		hashSet.addAll(appCaseResultMapper.getProvinceByAppCaseIds(map.get("app")));
		return hashSet;
	}

	/**
	 * 根据任务id查询用例
	 * @param id
	 * @return
	 */
	@Override
	public Map<String,Object> getTestCaeBySysTaskId(Integer id) {
		SysTask sysTask = sysTaskMapper.selectByPrimaryKey(id);
		String caseIds = sysTask.getIds();
		Map<String,List<Integer>> map =(Map<String,List<Integer>>) JSON.parse(caseIds);

		Map<String, Object> resultMap = new HashMap<>();
		List<TestCase> webList = new ArrayList<>();
		List<AppCase> appList = new ArrayList<>();
		//WEB
		if(null != map.get("web") && map.get("web").size() >0) {
			webList.addAll(testCaseMapper.getTestCaseByIds(map.get("web")));
		}
		//app
		if(null != map.get("app") && map.get("app").size() >0) {
			appList.addAll(appCaseService.getTestCaseByIds(map.get("app")));
		}
		List<CommonCaseVO> list = new ArrayList<>();
		for (AppCase appCase : appList)
		{
			CommonCaseVO caseVO = new CommonCaseVO();

			caseVO.setId(appCase.getCaseId());
			caseVO.setBusinessId(appCase.getBusinessId());
			caseVO.setName(appCase.getCaseName());
			caseVO.setType("app");
			//caseVO
			list.add(caseVO);
		}
		resultMap.put("bSysTaskId", id);
		resultMap.put("web", webList);
		resultMap.put("app", list);
		return resultMap;
	}
}
