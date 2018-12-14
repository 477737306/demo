package com.cmit.testing.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.vo.StepReportVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import com.cmit.testing.service.SysSurveyTaskService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.ToolUtil;
import com.cmit.testing.utils.verify.HttpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.*;

/**
 * 众测任务相关接口
 *
 * @author hp
 */
@RestController
@RequestMapping(value = "/api/v1/sysSurveyTask")
@Permission
public class SysSurveyTaskController extends BaseController {
    @Autowired
    private SysSurveyTaskService sysSurveyTaskService;
    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private AppCaseService appCaseService;

    @Autowired
    private AppCaseResultService appCaseResultService;


    @Value("${task.host}")
    String host;

    @Value("${task.path}")
    String path;



    /**
     * 根据任务名和任务发布人分页查询众测任务信息
     *
     * @param currentPage 当前页码
     * @param pageSize    每页展示条数
     * @param taskName    众测任务名
     * @param taskIss     任务发布人
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(@QueryParam("currentPage") Integer currentPage,
                                   @QueryParam("pageSize") Integer pageSize,
                                   @QueryParam("taskName") String taskName,
                                   @QueryParam("taskIss") String taskIss) {
        return new JsonResultUtil(200, "操作成功", sysSurveyTaskService.findPage(currentPage, pageSize, taskName, taskIss));
    }

    /**
     * 添加 type=1
     *
     * @param surveyTask
     * @return
     */
    @RequestMapping(value = "/add/{type}", method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody SysSurveyTask surveyTask, @PathVariable("type") Integer type) {
        if (sysSurveyTaskService.insert(surveyTask, type) > 0)
            return new JsonResultUtil(200, "新增成功");
        return new JsonResultUtil(300, "新增失败,请重试或联系管理员");
    }

    /**
     * 根据任务id查询用例
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectSurveyTaskTestCaseByid", method = RequestMethod.GET)
    public JsonResultUtil selectSurveyTaskTestCaseByid(Integer id) {
        SysSurveyTask sysSurveyTask = sysSurveyTaskService.selectByPrimaryKey(id);
        String caseIds = sysSurveyTask.getCaseIds();
        Map<String, Object> surveyTaskMap = new HashMap<>();
        List<TestCase> webList = new ArrayList<>();
        List<AppCase> appList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (caseIds != null) {
            map = JSON.parseObject(caseIds);
            List<Integer> webIds = (List<Integer>) map.get("web");
            List<Integer> appIds = (List<Integer>) map.get("app");
            for (Integer caseId : webIds) {
                TestCase testCase = testCaseService.selectByPrimaryKey(caseId);
                webList.add(testCase);
            }
            for (Integer caseId : appIds) {
                //查询app用例接口
                AppCase appCase = appCaseService.getAppCaseById(caseId);
                appList.add(appCase);
            }
        }
        surveyTaskMap.put("web", webList);
        surveyTaskMap.put("app", appList);
        return new JsonResultUtil(surveyTaskMap);
    }


    /**
     * 选择用例
     *
     * @param surveyTask
     * @return
     */
    @RequestMapping(value = "/updateCaseState", method = RequestMethod.PUT)
    public JsonResultUtil updateCaseState(@RequestBody SysSurveyTask surveyTask) {
        Integer id = surveyTask.getId();
        SysSurveyTask sysSurveyTask = sysSurveyTaskService.selectByPrimaryKey(id);
        String executionState = sysSurveyTask.getExecutionState();
        if ("1".equals(executionState)) {
            return new JsonResultUtil(300, "任务正在执行，无法选择用例");
        }
        int tt = sysSurveyTaskService.updateByPrimaryKeySelective(surveyTask);
        if (tt > 0) {
            return new JsonResultUtil(200, "操作成功");
        } else {
            return new JsonResultUtil(300, "操作失败");
        }

    }

    /**
     * 根据主键批量删除
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
        sysSurveyTaskService.deleteByIds(ids);
        return new JsonResultUtil(200, "操作成功");
    }

    /**
     * 根据任务id生成任务报告
     *
     * @param surveyTaskId
     * @return
     */
    @RequestMapping(value = "/caseReportList", method = RequestMethod.GET)
    public JsonResultUtil caseReportList(Integer surveyTaskId, String province) {

        List<SurveyTaskReportVO>  caseReportList=new ArrayList();
        List<SurveyTaskReportVO>  appCaseReportList=new ArrayList();
        List<SurveyTaskReportVO> reportVOS = new ArrayList<>();

        try {
            //web
            caseReportList=sysSurveyTaskService.caseReportList(surveyTaskId, province, 0);
            if (caseReportList!= null && caseReportList.size()>0){
            reportVOS.addAll(caseReportList);}
            //app
            appCaseReportList= appCaseResultService.appCaseReportList(surveyTaskId, province);
            if (appCaseReportList!=null&&appCaseReportList.size()>0)
              reportVOS.addAll(appCaseReportList);

            return new JsonResultUtil(reportVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResultUtil(300, "查询失败");
    }

    /**
     * 根据用例id生成步骤报告
     *
     * @param testCaseId
     * @param surveyTaskId
     * @return
     */
    @RequestMapping(value = "/caseStepReport", method = RequestMethod.GET)
    public JsonResultUtil caseStepReport(String testCaseId, Integer surveyTaskId, String province, Integer source) {
        List<StepReportVO> reportVOS = null;
        if (null == source) {
            return new JsonResultUtil(300, "查询失败");
        }
        try {
            if (SurveyTaskReportVO.APP_SOURCE.equals(source)) {
                int appcaseId = Integer.parseInt(testCaseId);
                reportVOS = appCaseResultService.caseStepReport(appcaseId, surveyTaskId, province);
            } else if (SurveyTaskReportVO.WEB_SOURCE.equals(source)) {
                reportVOS = sysSurveyTaskService.caseStepReport(testCaseId, surveyTaskId, province);
            }
            return new JsonResultUtil(reportVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResultUtil(300, "查询失败");
    }

    /**
     * 众测平台任务执行
     *
     * @param map
     */
    @RequestMapping(value = "/surveyTaskExcute", method = RequestMethod.POST)
    public JsonResultUtil surveyTaskExcute(@RequestBody Map<String, Object> map) {
        Integer surveyTaskId = (Integer) map.get("surveyTaskId");
        Map<String, List<Integer>> caseMap = (Map<String, List<Integer>>) map.get("caseMap");
        /*List<Integer> webList = caseMap.get("web");
			List<Integer> appList = caseMap.get("app");*/
        sysSurveyTaskService.surveyTaskExcute(surveyTaskId, caseMap);
        return new JsonResultUtil(200, "操作成功");
    }

    /**
     * 获取众测任务用例进度
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/getTestCaseProgress", method = RequestMethod.POST)
    public JsonResultUtil getTestCaseProgress(@RequestBody Map<String, Object> map) {
        Integer surveyTaskId = (Integer) map.get("surveyTaskId");
        List<Integer> appCaseList = (List<Integer>) map.get("app");
        List<Integer> webCaseList = (List<Integer>) map.get("web");
        List<Map<String, Integer>> listMap = new ArrayList<>();
        List<Map<String, Integer>> list = (List<Map<String, Integer>>) RedisUtil.getObject("caseList");
        List<Map<String, Integer>> appList = (List<Map<String, Integer>>) RedisUtil.getObject("appCaseList");
        if (null == list || list.size() == 1) {
            return new JsonResultUtil();
        }
        if (null != webCaseList && webCaseList.size() > 0) {
            for (Integer id : webCaseList) {
                Map<String, Integer> resultMap = new HashMap<>();
                resultMap.put("caseId", id);
                resultMap.put("type", 0);
                TestCase testCase1 = new TestCase();
                List<TestCase> testCaseList = testCaseService.getTestCaseBySurTaskIdAndCaseId(surveyTaskId, id);
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
                if(number == executeNumber && number>0 && executeNumber>0){
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
                List<AppCase> copyCaseList = appCaseService.getAppCaseBySurveyTaskIdAndCaseId(surveyTaskId, id);
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
                    if(number == executeNumber && number>0 && executeNumber>0)
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

    /**
     * 推送信息到众测平台
     */
    @RequestMapping(value = "/PushSurveyTask", method = RequestMethod.POST)
    public JsonResultUtil PushSurveyTask(@RequestBody Map<String, Object> map) {
        //根据sysSurveyTask 获取该任务执行的用例结果
        //对查看的用例结果进行按需封装
        //调用httpUtils 发送http请求推送信息
        Integer id = (Integer) map.get("id");

        //httpUtils 中的URL为 host + path + querys 拼接而成的URL
        Map caseIdsMap = (Map) map.get("caseIds");
        String method = "POST";  // 请求方式
        Map<String, String> header = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodays = new HashMap<>();
//        content-type：application/ json
//        accept：application/ json
        header.put("content-type", "application/json");
//        header.put("accept","application/json");
        JsonResultUtil jsonResultUtil = new JsonResultUtil(300, "推送失败");

        SysSurveyTask sysSurveyTask1 = sysSurveyTaskService.selectByPrimaryKey(id);

        try {
            String jsonResult = sysSurveyTaskService.getJsonResult(sysSurveyTask1,caseIdsMap);
            if (jsonResult == null) {
                return new JsonResultUtil(300, "众测任务下无用例执行，无法同步！");
            }
            //发送请求
            HttpResponse response = HttpUtils.doPost(host, path, method, header, querys, jsonResult);
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity, "utf-8");
            JSONObject jsonObject = JSON.parseObject(entityString);

            String body = JSON.parseObject(jsonObject.getString("ROOT")).getString("BODY");
            JSONObject bodyJson = JSON.parseObject(body);
            String code = bodyJson.getString("CODE");
            sysSurveyTask1.setCode(code);
            sysSurveyTask1.setFeedback("1");
            sysSurveyTask1.setFeedbackTime(new Date());
            if ("0000".equals(code)) {
                //请求成功
                jsonResultUtil = new JsonResultUtil(200, "推送成功");
            } else if ("9999".equals(code)) {
                //请求失败
                sysSurveyTask1.setMsg(bodyJson.getString("MSG"));
                jsonResultUtil = new JsonResultUtil(300, bodyJson.getString("MSG"));
            }

            sysSurveyTaskService.updateByPrimaryKeySelective(sysSurveyTask1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResultUtil;
    }
}
