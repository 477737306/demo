package com.cmit.testing.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.vo.CaseExcResultVO;
import com.cmit.testing.entity.vo.RecordStepVO;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.ToolUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用例 Controller
 *
 * Created by Suviky on 2018/7/25 14:39
 */

@RestController
@RequestMapping("/api/v1/testcase")
@SystemLog("用例模块")
@Permission
public class TestCaseController extends  BaseController{

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TestCaseReportService testCaseReportService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ModelScriptService modelScriptService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private AppCaseResultService appCaseResultService;

    /**
     * 用例执行排序
     * @param map
     * @return
     */
    @RequestMapping(value = "/testCaseExecuteReorder",method = RequestMethod.POST)
    public JsonResultUtil testCaseExecuteReorder(@RequestBody Map<String,Object> map){
        Map<String, List<Integer>> caseMap = (Map<String, List<Integer>>) map.get("map");
        List<Map<String ,List<TestCase>>> testCaseList = ToolUtil.reorderByCaseIds(caseMap);
        return new JsonResultUtil(200,"操作成功",testCaseList);
    }

    /**
     * 批量更新用例依赖
     * @param map
     * @return
     */
    @RequestMapping(value = "/batchUpdateTestCase",method = RequestMethod.PUT)
    public JsonResultUtil batchUpdateTestCase(@RequestBody Map<String,Object> map){
        try {
            List<Map<String,String>> list = (List<Map<String,String>>) map.get("list");
            List<TestCase> webCaseList = new ArrayList<>();
            List<AppCase> appCaseList = new ArrayList<>();
            Pattern p = Pattern.compile("[0-9]]");
            for(Map<String,String> map1 : list){
                if("web".equals(map1.get("type"))){
                    TestCase testCase = new TestCase();
                    testCase.setId(Integer.parseInt(map1.get("id")));
                    String jsonStr = map1.get("followId");
                    Matcher m = p.matcher(jsonStr);
                    // 匹配数字表示有依赖关系，无数字则表示无依赖关系
                    if (!m.find()){
                        testCase.setFollowId(null);
                    }else{
                        testCase.setFollowId(map1.get("followId"));
                    }
                    webCaseList.add(testCase);
                }else{
                    //更新app用例依赖
                    AppCase appCase = new AppCase();
                    appCase.setCaseId(Integer.parseInt(map1.get("id")));
                    String jsonStr = map1.get("followId");
                    Matcher m = p.matcher(jsonStr);
                    // 匹配数字表示有依赖关系，无数字则表示无依赖关系
                    if (m.find())
                    {
                        appCase.setFollowId(jsonStr);
                    }
                    else
                    {
                        appCase.setFollowId(null);
                    }
                    appCaseList.add(appCase);
                }
            }
            if (CollectionUtils.isNotEmpty(appCaseList))
            {
                appCaseService.batchUpdateAppCase(appCaseList);
            }
            if(CollectionUtils.isNotEmpty(webCaseList)){
                testCaseService.batchUpdateTestCase(webCaseList);
            }
            return new JsonResultUtil(200,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResultUtil(300,"更新失败");
        }

    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResultUtil delete(@PathVariable("id")Integer id){
        testCaseService.deleteByPrimaryKey(id);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 获取
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResultUtil get(@PathVariable("id")Integer id){
        return new JsonResultUtil(200,"操作成功", testCaseService.selectByPrimaryKey(id));
    }

    /**
     * 获取业务下的用例(去除当前用例)
     * @param id 用例id
     * @param businessId 业务id
     * @return
     */
    @RequestMapping(value = "/getAllByBusinessId/{id}", method = RequestMethod.GET)
    public JsonResultUtil getAllByBusinessId(@PathVariable("id") Integer id,Integer businessId){
        TestCase testCase = new TestCase();
        testCase.setBusinessId(businessId);
        testCase.setOldTestcaseId(0);
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList = testCaseService.getListByTestCase(testCase);

        //去除当前用例
        for (TestCase aCase : testCaseList) {
            if(aCase.getId().equals(id)){
                testCase = aCase;
                break;
            }
        }
        testCaseList.remove(testCase);
        return new JsonResultUtil(200,"操作成功",testCaseList);
    }

    /**
     * 获取业务下已执行完成状态的用例
     * @return
     */
    @RequestMapping(value = "/getIsFinishByBusinessId/{id}", method = RequestMethod.GET)
    public JsonResultUtil getIsFinishByBusinessId(@PathVariable("id")Integer id,TestCase testCase){
        testCase.setBusinessId(id);
//        List<Integer> list = new ArrayList<>();
//        list.add(1);//执行中
//        list.add(4);//暂停
//        testCase.setUnequalFinshs(list);//查询执行完成装状态
        testCase.setOldTestcaseId(0);
        Map<String,Object> result = new HashMap<>();
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.addAll(testCaseService.getListByTestCase(testCase));
        // add appCase list
        //testCase.setIsFinish(2);
        testCaseList.addAll(appCaseService.getListByAppCase(testCase));
        result.put("testCaseData",testCaseList);
        return new JsonResultUtil(200,"操作成功",result );
    }

    /**
     * 获取业务下已执行完成状态的用例
     * @return
     */
    @RequestMapping(value = "/getTestCaseByBusinessId/{id}", method = RequestMethod.GET)
    public JsonResultUtil getTestCaseByBusinessId(@PathVariable("id")Integer id,TestCase testCase){
        testCase.setBusinessId(id);
        testCase.setOldTestcaseId(0);
        Map<String,Object> result = new HashMap<>();
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.addAll(testCaseService.getListByTestCase(testCase));
        // add appCase list
        testCaseList.addAll(appCaseService.getListByAppCase(testCase));
        result.put("testCaseData",testCaseList);
        return new JsonResultUtil(200,"操作成功",result );
    }

    /**
     * 获取业务下已执行完成状态的用例
     * @return
     */
    @RequestMapping(value = "/getMapByBusinessId/{id}", method = RequestMethod.GET)
    public JsonResultUtil getMapByBusinessId(@PathVariable("id")Integer id,TestCase testCase){
        testCase.setBusinessId(id);
        testCase.setOldTestcaseId(0);
        Business business = businessService.selectByPrimaryKey(id);
        Map<String,Object> result = new HashMap<>();
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.addAll(testCaseService.getListByTestCase(testCase));
        // add appCase list
        testCaseList.addAll(appCaseService.getListByAppCase(testCase));
        result.put("testCaseData",testCaseList);
        result.put("bSysTaskId",business.getSysTaskId());
        return new JsonResultUtil(200,"操作成功",result );
    }

    /**
     * 用例报告
     * @param testCaseReportVO
     * @return
     */
    @RequestMapping(value = "/findAllTestReporter", method = RequestMethod.GET)
    public JsonResultUtil findAllTestReporter( TestCaseReportVO testCaseReportVO){
        return new JsonResultUtil(testCaseReportService.findAll(testCaseReportVO));
    }

    /**
     * 查看步骤详情
     * @param recordStepVO
     * @return
     */
    @RequestMapping(value = "/findAllStep", method = RequestMethod.GET)
    public JsonResultUtil findAllStep( RecordStepVO recordStepVO){
        return new JsonResultUtil(recordService.findAll(recordStepVO));
    }

    /**
     * 查看步骤执行成功率
     * @return
     */
    @RequestMapping(value = "/getStepSuccessRate", method = RequestMethod.GET)
    public JsonResultUtil getStepSuccessRate(TestCaseReport testCaseReport){
        double successRate = testCaseReportService.getStepSuccessRate(testCaseReport);
        return new JsonResultUtil(successRate);
    }
    /**
     * 查询批次号通过用例id
     * @param id
     * @return
     */
    @RequestMapping(value = "/getExcuteNumById", method = RequestMethod.GET)
    public JsonResultUtil testCaseNumByByTestCaseId(Integer id){
        Map<String,List<Long>> excuteNum = new HashMap<>();
        excuteNum.put("excuteNum",testCaseService.getExcuteNumById(id));
        return new JsonResultUtil(excuteNum);
    }

    /**
     * 根据任务id查询用例
     * @param pageBean
     * @param id
     * @return
     */
    @RequestMapping(value="getTestCaseByTaskId" , method = RequestMethod.GET)
    public JsonResultUtil getTestCaseByTaskId(PageBean<TestCase> pageBean,String id){
        return new JsonResultUtil(testCaseService.getTestCaseByTaskId(pageBean,Integer.parseInt(id)));
    }

    /**
     * 删除用例报告
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteByIds" ,method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){

        int t = testCaseReportService.deleteByIds(map);
        if(t > 0){
            return new JsonResultUtil(200,"删除成功");
        }else{
            return new JsonResultUtil(300,"删除失败");
        }
    }

    /**
     * 获取业务下的普通脚本和所有通用脚本
     *
     * @return
     */
    @RequestMapping(value = "/getSelectAllByBusinessId/{id}",method = RequestMethod.GET)
    public JsonResultUtil getSelectAllByBusinessId(@PathVariable("id") Integer id){
        return new JsonResultUtil(200,"操作成功",modelScriptService.getAllByBusinessId(id));
    }

    /**
     * 查询套餐名称
     *
     * @return
     */
    @RequestMapping(value = "/getProductNames",method = RequestMethod.GET)
    public JsonResultUtil getProductNames( String name) {
        return new JsonResultUtil(productService.getProductNames(name));
    }
    
    
 
    /**
     * 根据用例id和手机号查询用例的执行过程 
     * @param testcastId 用例id
     * @param phoneNum   手机号码
     * @return
     */
    @RequestMapping(value = "/getCaseExecutionProcess", method = RequestMethod.GET)
	public JsonResultUtil getCaseExecutionProcess(@QueryParam("testcastId") Integer testcastId,@QueryParam("phoneNum") String phoneNum){
		return new JsonResultUtil(200,"操作成功", recordService.getCaseExecutionProcess(testcastId, phoneNum));
	}

    /**
     * 分页查询执行中的用例
     */
    @RequestMapping(value = "/getAllExecutingCase",method = RequestMethod.GET)
    public JsonResultUtil getAllExecutingCase(PageBean<TestCase> pageBean,TestCaseReportVO testCaseReportVO) {
        return new JsonResultUtil(200, "查询成功", testCaseService.getAllExecutingCase(pageBean,testCaseReportVO));
    }

    /**
     * 修改用例报告
     * @param testCaseReport
     * @return
     */
    @RequestMapping(value = "/updateTestCaseReport",method = RequestMethod.PUT)
    public JsonResultUtil updateTestCaseReport(@RequestBody TestCaseReport testCaseReport) {
        if("web".equals(testCaseReport.getType())){
            testCaseReportService.updateByPrimaryKeySelective(testCaseReport);
            TestCaseReport testCaseReport_ = testCaseReportService.selectByPrimaryKey(testCaseReport.getId());
            TestCase testCase =testCaseService.selectByPrimaryKey(testCaseReport_.getTestcastId());
            testCase.setExcuteNum(testCaseReport_.getExcuteNum());
            testCaseService.updateByPrimaryKeySelective(testCase);
            List<Long> excuteNums = testCaseService.getExcuteNumById(testCase.getOldTestcaseId());
            if(excuteNums.get(0).equals(testCase.getExcuteNum())){
            //TODO:代做更新
            }
        }else {
            CaseExcResultVO caseExcResultVO=new CaseExcResultVO();
            caseExcResultVO.setTestCaseReportId(testCaseReport.getId());
            caseExcResultVO.setExcuteResult(testCaseReport.getPassStatus());
            caseExcResultVO.setDefectDescription(testCaseReport.getDefectDescription());
            caseExcResultVO.setExecuteNum(testCaseReport.getExcuteNum());
           appCaseResultService.updateByPrimaryKeySelective(caseExcResultVO);
            //appCaseResultService.updateBytetestCaseReport(testCaseReport);
        }
        return new JsonResultUtil(200,"操作成功");
    }
//
//    /**
//     * 压缩截图
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/zipDownload", method = RequestMethod.GET)
//    public void downloadZipExcels(HttpServletResponse response, TestCaseReportVO testCaseReportVO ,@RequestParam(defaultValue ="",value = "i")List<Integer> i)  {
//
//        if(null != i && i.size() >0){
//            testCaseReportVO.setTestCaseReportIds(i);
//        }
//
//        List<DownloadFileDto> downloadFileDtoList = testCaseService.downloadZipExcels(testCaseReportVO);
//
//
//        if(downloadFileDtoList.size() ==0){
//           throw new TestSystemException("无截图");
//        }
//        String fileName = "用例执行结果截图压缩包.zip";
//        response.reset();
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        try {
//            //弹出保存框代码
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        if (CollectionUtils.isNotEmpty(downloadFileDtoList)) {
//            try {
//                //压缩
//                byte[] dataByteArr = ToolUtil.zipFile(downloadFileDtoList);
//                response.getOutputStream().write(dataByteArr);
//                response.flushBuffer();
//            } catch (Exception e) {
//                logger.error("压缩zip数据出现异常", e);
//                throw new RuntimeException("压缩zip包出现异常");
//            }
//        }
//    }


    /**
     * 导出用例执行结果
     * @param response
     * @param testCaseReportVO
     * @return
     */
    /*@RequestMapping(value = "/exportCaseExecutionResults", method = RequestMethod.GET)
    public void exportCaseExecutionResults(HttpServletResponse response, TestCaseReportVO testCaseReportVO){
    	List<TestCaseReportVO> testCaseReports =testCaseReportService.findAll(testCaseReportVO);
    	List<TestCaseReportVO> testList=new ArrayList<>();
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
    	for (TestCaseReportVO test : testCaseReports) {
    		if(test.getExcuteTime()!=null){
    			String  excuteTime = sdf.format(test.getExcuteTime());
    			test.setExcuteTimeStr(excuteTime);
    		}
			
			if(Constants.EXCUTE_RESULT_SUCCESS.equals(test.getExcuteResult())){
				test.setExcuteResultStr("成功");
			}else if(Constants.EXCUTE_RESULT_FAILED.equals(test.getExcuteResult())){
				test.setExcuteResultStr("失败");
			}
			
			if(Constants.PASS_STATE_SUCCESS.equals(test.getPassStatus())){
				test.setPassStatusStr("通过");
			}else if(Constants.PASS_STATE_FAILED.equals(test.getPassStatus())){
				test.setPassStatusStr("不通过");
			}
			
		    testList.add(test);
		}
    	ExcelJXLSUtil<TestCaseReportVO> excelJXLSUtil=new ExcelJXLSUtil<>();
		excelJXLSUtil.export(response, "用例执行结果", "excel/webCaseExecutionResults.xls", testList);	
    }*/

}
