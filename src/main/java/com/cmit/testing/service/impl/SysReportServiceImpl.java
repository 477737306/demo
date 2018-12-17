package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.BusinessMapper;
import com.cmit.testing.dao.ReportBusCaseMapper;
import com.cmit.testing.dao.SysReportMapper;
import com.cmit.testing.dao.TestCaseMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.SysReportService;
import com.cmit.testing.service.SysTaskService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysReportServiceImpl extends BaseServiceImpl<SysReport> implements SysReportService {

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private SysReportMapper sysReportMapper;

    @Autowired
    private ReportBusCaseMapper reportBusCaseMapper;
    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private AppCaseResultService appCaseResultService;

    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public Map<String, Object> add(SysReport sysReport, Map<String,Object> sysmap) {
        List<Map<String,Object>> list = (List<Map<String,Object>>)sysmap.get("businessList");//查询所有业务
        int number = Integer.parseInt(String.valueOf(sysmap.get("number")));
        Integer sysTaskId = null;
        if (number < 0)
        {
            sysTaskId =  Integer.parseInt(String.valueOf(sysmap.get("sysTaskId")));
        }
        if(null != list && list.size()>0){
            Business business = businessMapper.selectByPrimaryKey(new Integer(list.get(0).get("businessId").toString()));
            sysReport.setProjectId(business.getProjectId());
        }
        sysReportMapper.insert(sysReport);
        int reportResult = 0;
//        创建用例集合, 创建两个, 一个存放App用例list  一个存放web用例list
        List<AppCase> appCaseList = new ArrayList<>();
        List<TestCase> testCaseList = new ArrayList<>(); //用例列表
        for (Map<String , Object> map : list) {
            //  遍历业务
            String businessId = (String)map.get("businessId");
            List<Map<String ,String>> casesList= (List<Map<String ,String>>)map.get("testCases");//  获取用例的集合
            Map<String, List> caseIdsMap = new HashMap<>();
            List<Integer> webCaseIds = new ArrayList<>();
            List<Integer> appCaseIds = new ArrayList<>();
//            List<Map<String,String>> casesList2 = new ArrayList<>(); //用例的头信息集合
            for(Map<String ,String > casesMap : casesList){
                Map<String,String> caseMap = new HashMap<>();// 装当前用例的头信息
                //判断当前用例是App用例还是web用例
                //这里进行testCase的判断,是App还是web的用例, 并分别存入不 同的集合

                String type = (String) casesMap.get("type");

                if("web".equals(type)){
                    String caseid = casesMap.get("testCaseId");  //获取用例id
                    TestCase testCase ;
                    if(number < 0){
//                        testCase = testCaseMapper.getTestCaseByCaseId(Integer.parseInt(caseid));
                        List<TestCase> caseList  = testCaseService.getTestCasesByOldTestcaseIdAndSysTaskId(Integer.parseInt(caseid),sysTaskId);
                        if(caseList != null && caseList.size() >0 ){
                            testCase = caseList.get(0);
                            testCaseList.add(testCase);  //将查询的用例存入集合
                            webCaseIds.add(testCase.getId());
                        }
                    }else{
                        testCase = testCaseMapper.selectByPrimaryKey(Integer.parseInt(caseid));
                        testCaseList.add(testCase);  //将查询的用例存入集合
                        webCaseIds.add(testCase.getId());
                    }
                }
                if("app".equals(type)){
                    String appTestCaseId = casesMap.get("testCaseId");
                    AppCase appCase = null;
                    if (number < 0)
                    {
                        Integer oldCaseId = Integer.parseInt(appTestCaseId);
                        //获取副本用例
                        List<AppCase> copyCaseList = appCaseService.getAppCaseTaskIdAndCaseId(sysTaskId, oldCaseId);
                        if (CollectionUtils.isNotEmpty(copyCaseList))
                        {
                            appCase = copyCaseList.get(0);
                        }
                    }
                    else
                    {
                        //改成查询原用例
                        appCase = appCaseService.getAppCaseById(Integer.parseInt(appTestCaseId));
                    }
                    appCaseIds.add(appCase.getCaseId());
                    appCaseList.add(appCase);
                }
            }
//            map.put("testCases",casesList2);
            caseIdsMap.put("web", webCaseIds);
            caseIdsMap.put("app", appCaseIds);

           /* if(testCaseIds != null && !"".equals(testCaseIds.trim())){
                testCaseIds = testCaseIds.substring(0,testCaseIds.length()-1);
            }*/

          /*  if(appCaseIds != null && !"".equals(appCaseIds.trim())){
                appCaseIds = appCaseIds.substring(0,appCaseIds.length()-1);
            }*/

            //存储数据到中间表
            ReportBusCase reportBusCase = new ReportBusCase();
            reportBusCase.setReportId(sysReport.getId());
            reportBusCase.setBusinessId(Integer.parseInt(businessId));
            reportBusCase.setTestcaseId(JSON.toJSONString(caseIdsMap));
            reportResult += reportBusCaseMapper.insert(reportBusCase);
        }

        List<Map<String, Object>> listMap = testCaseService.getTestCaseReportByTaskId(testCaseList,number); //遍历出来的所有用例生成报告

//        调用App报告生成接口生成报告
        List<Map<String, Object>> applistMap = appCaseResultService.getAppCaseReportByCaseId(appCaseList,number);

        listMap = mergerWebAndApp(listMap, applistMap);

        Map<String,Object> reportMap = new HashMap<>();
        reportMap.put("reportData",listMap);
        reportMap.put("reportHeader",sysmap);
        sysReport.setData(JSONObject.toJSONString(reportMap));
        if(number < 0){
            sysReport.setSysTaskId(sysTaskId);
        }
        sysReportMapper.updateByPrimaryKeySelective(sysReport);
        return reportMap;
    }

    @Override
    public PageBean<SysReport> getReportAll(PageBean<SysReport> pageBean, SysReport sysReport) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<SysReport> list = sysReportMapper.getReportAll(sysReport);
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public List<SysReport> getAll(SysReport sysReport) {
        return sysReportMapper.getReportAll(sysReport);
    }

    /**
     * 根据用例ids生成报告数据
     * @param ids
     * @return
     */
    public List<Map<String, Object>> getTestCaseReportByCaseIds(String ids) {
        String[] arr = ids.split(",");
        List<TestCase> list = new ArrayList<>();
        for(int i = 0 ; i < arr.length ; i++){
            TestCase testCase = testCaseMapper.getTestCaseByCaseId(Integer.parseInt(arr[i]));
            list.add(testCase);
        }
        return testCaseService.getTestCaseReportByTaskId(list,null);
    }

    private List<Map<String,Object>> mergerWebAndApp(List<Map<String,Object>> listMap, List<Map<String,Object>> applistMap) {
        for (Map<String, Object> map : listMap) {
            String province = (String) map.get("province");
            List<Map<String, String>> listin = (List<Map<String, String>>) map.get("caseAll");
            for (Map<String, Object> appMap : applistMap) {
                String appProvince = (String) appMap.get("province");
                List<Map<String, String>> appListin = (List<Map<String, String>>) appMap.get("caseAll");
                if(province.equals(appProvince)){
                    for (Map<String, String> appIn : appListin) {
                        listin.add(appIn);
                    }
                }
            }
        }

        for (Map<String, Object> appMap : applistMap) {
            boolean flag = false;
            String appProvince = (String) appMap.get("province");
            for (Map<String, Object> map : listMap) {
                String province = (String) map.get("province");
                //原listMap中有电话号码了
                if(appProvince.equals(province)){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                listMap.add(appMap);
            }
        }
        return listMap;
    }

}
