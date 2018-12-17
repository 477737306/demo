package com.cmit.testing.web;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.SysTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.SysTaskService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/task")
@Permission
public class SysTaskController extends BaseController{

    @Autowired
    private SysTaskService sysTaskService;

    @Autowired
	private TestCaseService caseService;

    @Autowired
    private BusinessService businessService;
    @Autowired
    private AppCaseService appCaseService;

    /**
     * 根据任务id生成任务报告
     * @param id
     * @return
     */
    @RequestMapping(value="getTestCaseReportByTaskId" , method = RequestMethod.GET)
    public JsonResultUtil getTestCaseReportByTaskId(String id){
        List<Map<String,Object>> map = sysTaskService.getTestCaseReportByTaskId(Integer.parseInt(id));
        return new JsonResultUtil(map);
    }

    /**
     * 根据用例ids生成报告
     * @param ids
     * @return
     */
    @RequestMapping(value="getTestCaseReportByCaseIds" , method = RequestMethod.GET)
    public JsonResultUtil getTestCaseReportByCaseIds(String ids){
        List<Map<String,Object>> map = sysTaskService.getTestCaseReportByCaseIds(ids);
        return new JsonResultUtil(map);
    }

    /**
     * 分页查询所有任务
     * @return
     */
    @RequestMapping(value="getAll" , method = RequestMethod.GET)
    public JsonResultUtil getAll(PageBean<SysTask> pageBean , SysTask sysTask){

        return new JsonResultUtil(200,"操作成功",sysTaskService.getAll(pageBean ,sysTask));
    }


    /**
     * 添加任务
     * 
     * @param map
     * @return
     */
    @RequestMapping(value="add",method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody Map<String , Object> map){
    	Map<String, List<Integer>> caseIdsMap=new LinkedHashMap<String, List<Integer>>();
    	List<Integer> webList = new ArrayList<Integer>();
    	List<Integer> appList = new ArrayList<Integer>();

        SysTask sysTask =new SysTask();
        String taskName = (String) map.get("taskName");
        List<Map<String ,Object>> busList = (List<Map<String ,Object>>) map.get("businessList");
        for (Map<String ,Object > busMap : busList) {
            List<Map<String,String>> caseList = (List<Map<String,String>>)busMap.get("testCases");
            for (Map<String,String> caseMap : caseList) {
                String caseid = caseMap.get("testCaseId");
                String type = caseMap.get("type");
                if ("web".equals(type)) {
    				webList.add(Integer.parseInt(caseid));
    			} else if ("app".equals(type)) {
    				appList.add(Integer.parseInt(caseid));
    			}
            }
        }
        caseIdsMap.put("web", webList);
        caseIdsMap.put("app", appList);

        sysTask.setIds(JSON.toJSONString(caseIdsMap));
        sysTask.setTaskName(taskName);
        sysTask.setCreateTime(new Date());
        sysTask.setCreateBy(getShiroUser().getAccount());
        sysTaskService.insert(sysTask);
        String businessId = String.valueOf(map.get("businessId")==null ? "":map.get("businessId"));
        if(StringUtils.isNotEmpty(businessId)){
            Business business = new Business();
            //在业务里面添加任务id
            business.setSysTaskId(sysTask.getId());
            business.setId(Integer.parseInt(businessId));
            businessService.updateByPrimaryKeySelective(business);
        }
        return new JsonResultUtil(200,"操作成功",sysTask.getId());
    }

    /**
     * 获取批量执行任务用例进度
     * @param map
     * @return
     */
    @RequestMapping(value = "/getTestCaseProgress", method = RequestMethod.POST)
    public JsonResultUtil getTestCaseProgress(@RequestBody Map<String, Object> map) {
        Integer sysTaskId = (Integer) map.get("sysTaskId");
        List<Integer> appCaseList = (List<Integer>) map.get("app");
        List<Integer> webCaseList = (List<Integer>) map.get("web");
        List<Map<String, Integer>> listMap = new ArrayList<>();
        List<Map<String, Integer>> list = (List<Map<String, Integer>>) RedisUtil.getObject("caseList");
        List<Map<String, Integer>> appList = (List<Map<String, Integer>>) RedisUtil.getObject("appCaseList");
        if (null == list || list.size() == 0 ) {
            return new JsonResultUtil();
        }
        if (null != webCaseList && webCaseList.size() > 0) {
            for (Integer id : webCaseList) {
                Map<String, Integer> resultMap = new HashMap<>();
                resultMap.put("caseId", id);
                resultMap.put("type", 0);
                TestCase testCase1 = new TestCase();
                List<TestCase> testCaseList = caseService.getTestCasesByOldTestcaseIdAndSysTaskId(id,sysTaskId);
                if (null != testCaseList && testCaseList.size() > 0) {
                    testCase1 = testCaseList.get(0);
                }
                Integer number = 0;
                Integer executeNumber = 0;
                for (Map<String, Integer> caseMap : list) {
                    if (caseMap.get("caseId").equals(testCase1.getId())) {
                        number = caseMap.get("number");
                        executeNumber = caseMap.get("executeNumber");
                    }
                }
                resultMap.put("number", number);
                resultMap.put("executeNumber", executeNumber);
                if(number.equals(executeNumber) && number >0 && executeNumber > 0){
                    resultMap.put("status",0);
                }else{
                    resultMap.put("status",1);
                }
                listMap.add(resultMap);
            }
        }
        // app 众测任务进度获取
        if (null != appCaseList && appCaseList.size() > 0) {
            for (Integer id : appCaseList)
            {
                Map<String, Integer> resultMap = new HashMap<>();
                resultMap.put("caseId", id);
                resultMap.put("type", 1);
                Integer number = 0;
                Integer executeNumber = 0;
                AppCase copyAppCase = new AppCase();
                List<AppCase> copyCaseList = appCaseService.getAppCaseTaskIdAndCaseId(sysTaskId, id);
                if (CollectionUtils.isNotEmpty(copyCaseList))
                {
                    copyAppCase = copyCaseList.get(0);
                }
                if (copyAppCase != null)
                {
                    for (Map<String, Integer> caseMap : appList)
                    {
                        if (caseMap.get("caseId").equals(copyAppCase.getCaseId()))
                        {
                            number = caseMap.get("number");
                            executeNumber = caseMap.get("executeNumber");
                        }
                    }
                    resultMap.put("number", number);
                    resultMap.put("executeNumber", executeNumber);
                    if(number.equals(executeNumber) && number >0 && executeNumber >0)
                    {
                        resultMap.put("status",0);
                    }
                    else
                    {
                        resultMap.put("status",1);
                    }
                    listMap.add(resultMap);
                }
            }
        }
        return new JsonResultUtil(listMap);
    }

    @RequestMapping(value="update",method = RequestMethod.PUT)
    public JsonResultUtil update(@RequestBody SysTask sysTask){
        sysTask.setUpdateTime(new Date());
        sysTask.setUpdateBy(getShiroUser().getAccount());
        return new JsonResultUtil(200,"操作成功", sysTaskService.updateByPrimaryKey(sysTask));
    }

    @RequestMapping(value="get/{id}" ,method = RequestMethod.GET)
    public JsonResultUtil get(String id){
        SysTask sysTask = sysTaskService.selectByPrimaryKey(Integer.parseInt(id));
        return new JsonResultUtil(200,"操作成功", sysTask);
    }

    @RequestMapping(value="delete/{id}",method = RequestMethod.DELETE)
    public JsonResultUtil delete(String id){

        return new JsonResultUtil(200,"操作成功", sysTaskService.deleteByPrimaryKey(Integer.parseInt(id)));
    }

    @RequestMapping(value="executeTask",method = RequestMethod.GET)
    public JsonResultUtil executeTask(String id){
    	SysTask sysTask = sysTaskService.selectByPrimaryKey(Integer.parseInt(id));
    	sysTask.setStatus("1");
    	sysTaskService.updateByPrimaryKey(sysTask);
		sysTaskService.executeTask(sysTask);
    	return new JsonResultUtil(200, "操作成功", null);
    }

    /**
     * 根据任务id查询省份
     * @param id
     * @return
     */
    @RequestMapping(value="getProviceCaseByTaskId/{id}" , method = RequestMethod.GET)
    public JsonResultUtil getProviceCaseByTaskId(@PathVariable("id")Integer id){
        return new JsonResultUtil(sysTaskService.getProviceCaseByTaskId(id));
    }

    /**
     * 任务用例报告
     * @param id 任务id
     * @param province 省份
     * @return
     */
    @RequestMapping(value="findReportByTaskId/{id}" , method = RequestMethod.GET)
    public JsonResultUtil findReportByTaskId(@PathVariable("id")Integer id,String province){
        return new JsonResultUtil(sysTaskService.findReportByTaskId(id,province));
    }

    /**
     * 根据任务id查询用例
     * @param id 任务id
     * @return
     */
    @RequestMapping(value="getTestCaeBySysTaskId/{id}" , method = RequestMethod.GET)
    public JsonResultUtil getTestCaeBySysTaskId(@PathVariable("id")Integer id){
        return new JsonResultUtil(sysTaskService.getTestCaeBySysTaskId(id));
    }

}
