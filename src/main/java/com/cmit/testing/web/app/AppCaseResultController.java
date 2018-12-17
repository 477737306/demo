package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCaseResult;
import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.vo.CaseExcResultVO;
import com.cmit.testing.entity.vo.RecordStepVO;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.*;
import com.cmit.testing.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * creat by chenxiaozhang
 * 2018.9.5
 */
@RestController
@RequestMapping("/api/v1/result")
//@Permission
public class AppCaseResultController extends BaseController {
    @Autowired
    private AppCaseResultService appCaseResultService;
    @Autowired
    private AppCaseResultMapper appCaseResultMapper;
    @Autowired
    private  AppCaseService appCaseService;

    /**
     * 执行结果展示,带分页   单个用例查询接口显示用
     *     条件查询
     * @param
     * @return
     */
    @RequestMapping(value = "/listCaseResult",method = RequestMethod.GET)
    public JsonResultUtil listCaseExcResult(PageBean<CaseExcResultVO> pageBean, CaseExcResultVO caseExcResultVO) {

            return new JsonResultUtil(200, "操作成功", appCaseResultService.listCaseExcProcess(pageBean, caseExcResultVO));
        }

    /**
     * 根据cresultId删除当前用例报告记录
     *
     */
    @RequestMapping(value = "/deleteByResultIds",method = RequestMethod.DELETE)
        public JsonResultUtil deleteByResultId(@RequestBody  Map<String, Object> map ){
        List<Integer> ids =(List<Integer>) map.get("testCaseReportId");
        if (ids==null){
            return new JsonResultUtil(300,"参数为空");
        }
        int a=0;

        a = appCaseResultService.deleteByPrimaryKey(ids);

            if (a >= ids.size()) {
                return new JsonResultUtil(200, "操作成功");
            } else {
                return new JsonResultUtil(300, "操作失败");
            }


    }
     /**
     * 测试执行
     */
     @RequestMapping(value = "/testList",method = RequestMethod.GET)
     public JsonResultUtil Testlist(PageBean<CaseExcResultVO> pageBean ,CaseExcResultVO caseExcResultVO){
         return new JsonResultUtil(200,"操作成功",appCaseResultService.TestList( pageBean,caseExcResultVO));
     }
    /**
     * 测试步骤 app端
     */
    @RequestMapping(value = "/findstep",method = RequestMethod.GET)
    public JsonResultUtil findsetp(PageBean<RecordStepVO> pageBean, RecordStepVO recordStepVO){
        return new JsonResultUtil(200,"操作成功",appCaseResultService.findstep(pageBean,recordStepVO));
    }
    /**
     * 删除测试执行
     */
    @RequestMapping(value = "/deltestList",method = RequestMethod.DELETE)
    public JsonResultUtil Testlist(@RequestBody Map<String, Object>map) {
        List<Integer> ids = (List<Integer>)map.get("executeId");
        int a=0;
       if (ids==null) {
           return new JsonResultUtil(300,"参数为空");
       }
        a = appCaseResultService.DelTestList(ids);
        if (a >= ids.size()) {
            return new JsonResultUtil(200, "操作成功");
        } else {
            return new JsonResultUtil(300, "操作失败");
        }
    }
    /**
     * 修改测试结果
     */
    @RequestMapping(value = "/updateResultById",method = RequestMethod.POST)
    public JsonResultUtil updateResultById(@RequestBody CaseExcResultVO caseExcResultVO) {
       if (caseExcResultVO.getTestCaseReportId()==null&&caseExcResultVO.getExcuteResult()==null){
            return new JsonResultUtil(300, "修改失败");
        }
        int a = appCaseResultService.updateByPrimaryKeySelective(caseExcResultVO);
        if (a > 0) {
            return new JsonResultUtil(200, "操作成功");
        }else {
            return new JsonResultUtil(300, "修改失败");
        }
    }
//    /**
//     * 根据勾选的用例报告id和用例id导出数据
//     * @param response
//     * @param
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/exportCaseExecutionResults", method = RequestMethod.GET)
//    public void exportCaseExecutionResults(HttpServletResponse response, CaseExcResultVO caseExcResultVO){
//        List<CaseExcResultVO> appCaseReports =appCaseResultMapper.getAppCaseResultByCaseId(caseExcResultVO);
//        List<CaseExcResultVO> testList=new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
//        for (CaseExcResultVO test : appCaseReports) {
//            if(test.getExecuteTime()!=null){
//                String  excuteTime = sdf.format(test.getExecuteTime());
//                test.setExcuteTimeStr(excuteTime);
//            }
//
//            if(Constants.EXCUTE_RESULT_SUCCESS.equals(test.getPassStatus())){
//                test.setExcuteResultStr("成功");
//            }else if(Constants.EXCUTE_RESULT_FAILED.equals(test.getPassStatus())){
//                test.setExcuteResultStr("失败");
//            }
//
//            if(Constants.PASS_STATE_SUCCESS.equals(test.getExcuteResult())){
//                test.setPassStatusStr("通过");
//            }else if(Constants.PASS_STATE_FAILED.equals(test.getExcuteResult())){
//                test.setPassStatusStr("不通过");
//            }
//            if(StringUtils.isNotEmpty(test.getLog())){
//                byte[] fileBytes = fileStorageOperate.downloadFile(test.getLog());
//                String log =new String(fileBytes);
//                test.setLog(log);
//            testList.add(test);
//        }
//        ExcelJXLSUtil<CaseExcResultVO> excelJXLSUtil=new ExcelJXLSUtil<>();
//        excelJXLSUtil.export(response, "用例执行结果", "excel/AppCaseExecutedResult.xls", testList);
//
//    }
    /**
     * 查询批次号通过用例id
     * @param
     * @return
     */
    @RequestMapping(value = "/getExcuteNumById", method = RequestMethod.GET)
    public JsonResultUtil testCaseNumByByTestCaseId(Integer caseId){
        Map<String,List<Long>> excuteNum = new HashMap<>();
        excuteNum.put("excuteNum", appCaseService.getExcuteNumById(caseId));
        return new JsonResultUtil(excuteNum);
    }
}

