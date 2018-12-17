package com.cmit.testing.service.impl;

import com.cmit.testing.dao.ModelScriptMapper;
import com.cmit.testing.dao.ProjectMapper;
import com.cmit.testing.dao.TestCaseMapper;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.service.PerceptionService;
import com.cmit.testing.service.app.AppCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PerceptionServiceImpl implements PerceptionService {

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    ModelScriptMapper scriptMapper;

    @Autowired
    TestCaseReportMapper testCaseReportMapper;

    @Autowired
    TestCaseMapper testCaseMapper;


    @Autowired
    AppCaseService appCaseService;

    @Override
    public Map<String, Object> getBasicData() {

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> resultRoot = new HashMap<>();
        Map<String,Object> resultBody = new HashMap<>();

        try {
            //app端
            Map<String, Integer> appCount = appCaseService.caseScriptCount();
            Integer appDefectsCoun = appCount.get("ceseBugCount");//缺陷数
            Integer appScriptCount = appCount.get("appScriptCount");//脚本个数
            Integer appExecuteCount = appCount.get("executeCount");//执行次数


            //web端
            Integer projectCount = projectMapper.getProjectSum();
            Integer scriptCount = scriptMapper.getScriptCount();
            Integer defectsCount = testCaseReportMapper.getDefectsCount();
            Integer excuteCount =testCaseMapper.getMaxExcuteNum();
            if (excuteCount == null)
                excuteCount =0;
            if (projectCount == null)
                excuteCount =0;
            if (defectsCount == null)
                defectsCount =0;
            if (excuteCount == null)
                excuteCount =0;


            resultBody.put("PRO_NUM",projectCount+"");
            resultBody.put("SCRIPTS_NUM",scriptCount+appScriptCount+"");
            resultBody.put("DEFECT_NUM",defectsCount+appDefectsCoun+"");
            resultBody.put("EXC_NUM",excuteCount+appExecuteCount+"");
            resultBody.put("CODE","0000");
            resultRoot.put("BODY",resultBody);
            result.put("ROOT",resultRoot);

        } catch (Exception e) {
            e.printStackTrace();
            resultBody.clear();
            resultBody.put("CODE","9999");
            resultBody.put("MSG",e.getMessage());
            resultRoot.put("BODY",resultBody);
            result.put("ROOT",resultRoot);
        }
        return result;
    }
}
