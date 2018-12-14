package com.cmit.testing.service.impl;

import com.cmit.testing.dao.*;
import com.cmit.testing.dao.app.AppCaseMapper;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.dao.app.AppScriptMapper;
import com.cmit.testing.service.PerceptionService;
import com.cmit.testing.service.ProjectCountService;
import com.cmit.testing.service.app.DeviceService;
import com.cmit.testing.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计分析
 *
 * @author YangWanLi
 * @date 2018/8/9 18:54
 */
@Service
public class ProjectCountServiceImpl implements ProjectCountService {

    public static Logger logger = LoggerFactory.getLogger(Class.class);


    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private TestCaseMapper testCaseMapper;
    @Autowired
    private ModelScriptMapper modelScriptMapper;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private SimEquipmentMapper simEquipmentMapper;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private PerceptionService perceptionService;
    @Autowired
    private AppCaseMapper appCaseMapper;
    @Autowired
    private AppScriptMapper appScriptMapper;
    @Autowired
    private AppCaseResultMapper appCaseResultMapper;

    @Override
    public Map<String, Object> projectCount() {
        Map<String, Object> result = new HashMap<>();

        //查询项目表相关统计数据
        Map<String, Long> projectCountData = projectMapper.projetCount();
        //查询业务表相关统计数据
        Map<String, Long> businessData = businessMapper.businessCount();
        //查询用例表相关统计数据
        Map<String, Long> caseData = testCaseMapper.testCaseCount();
        //查询脚本表相关统计数据
        Map<String, Long> scriptData = modelScriptMapper.scriptCount();
        //查询项目报告表相关统计数据
        Map<String, Long> reportData = testCaseReportMapper.testcaseReportCount();
        //查询sim卡表相关统计数据
        Map<String, Long> simCardData = simCardMapper.simCardCount();
        //查询sim卡表相关统计数据
//        Map<String, Long> simEquipmentData = simEquipmentMapper.simEquipmentCount();

        // 查询App用例相关的统计数据
        Map<String, Long> appCaseData = appCaseMapper.queryAppCaseCount();
        Long appCaseNum = appCaseData.get("appCaseNum");
        Long appNoDoNum = appCaseData.get("noDoNum");
        Long appDoingNum = appCaseData.get("doingNum");
        Long appDoneNum = appCaseData.get("doneNum");
        // 查询App脚本相关的统计数据
        Integer appScriptCount = appScriptMapper.queryAppScriptCount();
        // 查询App报告总数
        Map<String, Long> appResultData = appCaseResultMapper.queryAppResultCount();
        Long resultCount = appResultData.get("resultCount");

        Map<String, Object> projectdata = new HashMap<>();//项目数据
        Long projectnum = projectCountData.get("projectnum");
        Long casenum = caseData.get("casenum");


        projectdata.put("projectnum", projectnum);//项目总数
        projectdata.put("businessnum", businessData.get("businessnum"));//业务总数
        projectdata.put("casenum", casenum + appCaseNum);//用例总数
        projectdata.put("scriptnum", scriptData.get("scriptnum") + appScriptCount);//脚本总数
        projectdata.put("reportnum", reportData.get("reportnum") + resultCount);//报告总数

        Map<String, Object> projectstatus = new HashMap<>();//项目状态
        Map<String, Object> projectStatuData = new HashMap<>();//项目状态分布
        double pNoexecute = (double)projectCountData.get("noexecute") / projectnum * 100;
        double pExecuting = (double)projectCountData.get("executing") / projectnum * 100;
        double pExecuted = (double)projectCountData.get("executed") / projectnum * 100;

        projectStatuData.put("noexecute", ToolUtil.numericalPrecision(pNoexecute,2));//未执行
        projectStatuData.put("executing",ToolUtil.numericalPrecision(pExecuting,2) );//执行中
        projectStatuData.put("executed",ToolUtil.numericalPrecision(pExecuted,2));//已执行
        projectstatus.put("projectStatuData",projectStatuData);
        projectstatus.put("filed", projectCountData.get("filed"));//归档
        projectstatus.put("unfiled", projectCountData.get("unfiled"));//未归档

        Map<String, Object> casestatus = new HashMap<>();//用例状态分布
        double cNoexecute = (double)caseData.get("noexecute") / projectnum * 100;
        double cExecuting = (double)caseData.get("executing") / projectnum * 100;
        double cExecuted = (double)caseData.get("executed") / projectnum * 100;
        casestatus.put("noexecute", ToolUtil.numericalPrecision(cNoexecute,2));//未执行
        casestatus.put("executing", ToolUtil.numericalPrecision(cExecuting,2));//执行中
        casestatus.put("executed", ToolUtil.numericalPrecision(cExecuted,2));//已执行

        Map<String, Object> scriptstatus = new HashMap<>();//脚本类型分布
        scriptstatus.put("webnum", scriptData.get("webnum"));//web
        scriptstatus.put("appnum", scriptData.get("appnum") + appScriptCount);//app

        Map<String, Object> simData = new HashMap<>();//sim卡数据
        simData.put("free", simCardData.get("free"));//空闲
        simData.put("takeUp", simCardData.get("takeUp"));//占用
        simData.put("normal", simCardData.get("normal"));//正常
        simData.put("shutdown", simCardData.get("shutdown"));//停机

        Map<String, Integer> terminalData = deviceService.getDeviceData();//终端数据

//        DataCountVo basicData = perceptionService.getBasicData();//基本数据

        result.put("projectdata", projectdata);
        result.put("projectstatus", projectstatus);
        result.put("casestatus", casestatus);
        result.put("scriptstatus", scriptstatus);

        result.put("simData", simData);
        result.put("terminalData", terminalData);

        return result;
    }
}
