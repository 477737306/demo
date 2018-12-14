package com.cmit.testing.service.app.impl;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.RecordStepMapper;
import com.cmit.testing.dao.SysSurveyTaskMapper;
import com.cmit.testing.dao.app.AppCaseDeviceMapper;
import com.cmit.testing.dao.app.AppCaseMapper;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.dao.app.AppRecordStepMapper;
import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.app.AppCaseResult;
import com.cmit.testing.entity.app.AppRecordStep;
import com.cmit.testing.entity.vo.*;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.app.AppCaseResultService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppCaseResultServiceImp extends BaseServiceImpl<AppCaseResult> implements AppCaseResultService {
    @Autowired
    private AppCaseResultMapper appCaseResultMapper;
    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private RecordStepMapper recordStepMapper;
    @Autowired
    private AppCaseDeviceMapper appCaseDeviceMapper;
    @Autowired
    private AppRecordStepMapper appRecordStepMapper;
    @Autowired
    private SysSurveyTaskMapper sysSurveyTaskMapper;

    @Autowired
    private AppCaseMapper appCaseMapper;


    @Override
    public int insert(AppCaseResultService record) {
        return 0;
    }




    /***
     * 修改执行结果
     * @param
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(CaseExcResultVO caseExcResultVO) {
        int a= appCaseResultMapper.updateByPrimaryKeySelectiveVO(caseExcResultVO);
        return a;
    }
    @Override
    public int  updateBytetestCaseReport(TestCaseReport testCaseReport) {
        int a= appCaseResultMapper.updateBytetestCaseReport(testCaseReport);
        return a;
    }


    @Override
    public int updateByPrimaryKey(Integer id) {
        return 0;
    }

    /**
     * 根据CaseId删除结果相关的数据
     */
    @Override
    public int deleteResultByCaseId(Integer caseId) {
        return appCaseResultMapper.deleteResultByCaseId(caseId);
    }

    /**
     * 根据caseId查询结果ID集合
     */
    @Override
    public List<AppCaseResult> getResultIdsByCaseId(Integer caseId) {

        return appCaseResultMapper.getResultIdsByCaseId(caseId);
    }

    /**
     * @param pageBean
     * @param
     * @return用例执行结果查询  通过对象查执行过程 带分页
     */
    @Override
    public PageBean<CaseExcResultVO> listCaseExcProcess(PageBean<CaseExcResultVO> pageBean, CaseExcResultVO caseExcResultVO) {
        PageBean<CaseExcResultVO> page = new PageBean<CaseExcResultVO>();
        caseExcResultVO.setType("app");
        List<CaseExcResultVO> list = appCaseResultMapper.getAppCaseResultByCaseId(caseExcResultVO);
       for (CaseExcResultVO Case:list)
       {
           if (Case.getSnapshotLocation()!=null)
           {
               String[] erro = Case.getSnapshotLocation().split(",");
               Case.setErrorscreenshot(erro[erro.length - 1]);
           }
       }
        page.setTotalNum(list.size());
        page.setItems(list);
        return page;
    }


    @Override
    @Transactional
    /**
     * 批量删除 用例执行结果
     */
    public int deleteByPrimaryKey(List<Integer> ids) {
       // List<Integer> ids = (List<Integer>)map.get("testCaseReportId");
        int a=0;
           appCaseDeviceMapper.deletByReustId(ids);
        List<AppRecordStep> list =appRecordStepMapper.selectByExecuteId(ids);
        for (AppRecordStep step:list){
            if (StringUtils.isNotEmpty(step.getScreenShotUrl())){
                fileStorageOperate.deleteFile(step.getScreenShotUrl());
            }

        }
        appRecordStepMapper.deleteStepByExecuteId(ids);
        for(Integer id :ids) {
            AppCaseResult appCaseResult=appCaseResultMapper.selectByPrimaryKey(id);
            if(StringUtils.isNotEmpty(appCaseResult.getLogUrl()))
            {
            fileStorageOperate.deleteFile(appCaseResult.getLogUrl());
            }
            if (StringUtils.isNotEmpty(appCaseResult.getRecordVideoUrl())){
            fileStorageOperate.deleteFile(appCaseResult.getRecordVideoUrl());
            }
            if (StringUtils.isNotEmpty(appCaseResult.getResultUrl())){
            fileStorageOperate.deleteFile(appCaseResult.getResultUrl());
            }
            if (StringUtils.isNotEmpty(appCaseResult.getScreenshotUrl())){
            fileStorageOperate.deleteFile(appCaseResult.getScreenshotUrl());
            }
            a+= appCaseResultMapper.deleteByPrimaryKey(id);

         }

        return a;
    }

    @Override
    public int insert(AppCaseResult record) {

        return 0;
    }

    @Override
    public int insertSelective(AppCaseResult record) {
        return appCaseResultMapper.insertSelective(record);
    }

    @Override
    public AppCaseResult selectByPrimaryKey(Integer id) {
        return  new AppCaseResult() ;
    }

    @Override
    @Transactional
    public int updateByPrimaryKeySelective(AppCaseResult record) {

        return appCaseResultMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AppCaseResult record) {
        return 0;
    }

    /**
     * 传caseId
     * 根据用例list生成任务报告date  给web的接口
     * @param list
     * @return
     */
    @Override
    public List<Map<String,Object>> getAppCaseReportByCaseId(List<AppCase> list,Integer number) {
        List<Map<String,Object>> listMap = new ArrayList<>();
        String phone = null;
        String province = null;
        for (AppCase appCase : list)
        {
            List<CommonResultVO> reportList = new ArrayList<>();
            if (number < 0)
            {
                // 依赖执行后自动生成报告(一个业务下只保存这一份报告)
                reportList = appCaseResultMapper.getAppResultByCaseIdAndStatus(appCase.getCaseId(), null);
            }
            else
            {
                // 指定轮次生成报告
                reportList = appCaseResultMapper.getCaseReportByCaseIdAndExecuteNum(appCase.getCaseId(),number);
            }

            for (CommonResultVO resultVO : reportList)
            {
                Map<String ,Object> map = new HashMap<>();
                Map<String ,String> map1 = new HashMap<>();
                List<Map<String,String>> listIn = new ArrayList<>();
                if (number < 0)
                {
                    map1.put("testCaseId",appCase.getOldCaseId()+"");
                }
                else
                {
                    map1.put("testCaseId",appCase.getCaseId()+"");
                }
                map1.put("type", "app");
                map1.put("businessId",appCase.getBusinessId()+"");
                map1.put("passStatus",resultVO.getPassStatus());
                phone = resultVO.getTelNum();
                province = resultVO.getProvince();
                if(CollectionUtils.isNotEmpty(listMap))
                {
                    boolean flag = false;
                    for(Map<String,Object> map2 : listMap)
                    {
                        if(province.equals(map2.get("province")))
                        {
                            listIn = (List<Map<String,String>>)map2.get("caseAll");
                            listIn.add(map1);
                            map2.put("caseAll",listIn);
                            flag = true;
                            break;
                        }
                    }
                    if(!flag)
                    {
                        listIn.add(map1);
                        map.put("caseAll",listIn);
                        map.put("phone",phone);
                        map.put("province",province);
                        listMap.add(map);
                    }
                }
                else
                {
                    listIn.add(map1);
                    map.put("caseAll",listIn);
                    map.put("phone",phone);
                    map.put("province",province);
                    listMap.add(map);
                }
            }
        }
        return listMap;
    }
    @Override
    /**
     * 查询测试执行 带分页 自己app端用
     */
    public PageBean<CaseExcResultVO> TestList(PageBean<CaseExcResultVO> pageBean,CaseExcResultVO caseExcResultVO) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        caseExcResultVO.setType("app");
        List<CaseExcResultVO> list = appCaseResultMapper.TestList(caseExcResultVO);
        PageBean<CaseExcResultVO> pagetwo = new PageBean<>(pageBean.getCurrentPage(),pageBean.getPageSize(),(int)page.getTotal());
        pagetwo.setItems(list);
        return pagetwo;
    }

    /**
     *查询测试步骤  带分页
     * @param pageBean
     * @param
     * @return
     */
    @Override
    public PageBean<RecordStepVO> findstep(PageBean<RecordStepVO> pageBean, RecordStepVO recordStepVO) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<RecordStepVO> list = appCaseResultMapper.findstep(recordStepVO);
        PageBean<RecordStepVO> pagetwo = new PageBean<>(pageBean.getCurrentPage(),pageBean.getPageSize(),(int)page.getTotal());
        pagetwo.setItems(list);
        return pagetwo;
    }
    /**
     *查询测试步骤  不带分页 给web端的接口
     * @param
     * @param
     * @return
     */
    @Override
    public List<RecordStepVO> findstep( RecordStepVO recordStepVO) {


            List<RecordStepVO> list = appCaseResultMapper.findstep(recordStepVO);
            return list;

    }

    /**
     * 查询用例执行报告  不带分页  给web端的接口
     * @param testCaseReportVO
     * @return
     */
    @Override
    public List<TestCaseReportVO> CaseResults(TestCaseReportVO testCaseReportVO)
    {
        List<TestCaseReportVO> list=appCaseResultMapper.CaseResults(testCaseReportVO);

        for (TestCaseReportVO testCase:list)
        {
            if (1==testCase.getExcuteResult())
            {
                testCase.setExcuteResult(0);
            }
            else if (0==testCase.getExcuteResult())
            {
                testCase.setExcuteResult(1);
            }
        }
        return list;
    }

    /***
     * 删除测试执行
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int DelTestList(List<Integer> ids) {
        int a=0;
        a+=appCaseDeviceMapper.deletByExecuteId(ids);
        List<AppRecordStep> list =appRecordStepMapper.selectByExecuteId(ids);
        for (AppRecordStep step:list){
            if (StringUtils.isNotEmpty(step.getScreenShotUrl())){
                fileStorageOperate.deleteFile(step.getScreenShotUrl());
            }
        }
         appRecordStepMapper.deleteStepByExecuteId(ids);
        for(Integer id :ids) {
            int i = appCaseResultMapper.countByExecuteId(id);
            if (i > 0) {
                AppCaseResult appCaseResult = appCaseResultMapper.selectByExecuteId(id);

                if (StringUtils.isNotEmpty(appCaseResult.getLogUrl())) {
                    fileStorageOperate.deleteFile(appCaseResult.getLogUrl());
                }
                if (StringUtils.isNotEmpty(appCaseResult.getRecordVideoUrl())) {
                    fileStorageOperate.deleteFile(appCaseResult.getRecordVideoUrl());
                }
                if (StringUtils.isNotEmpty(appCaseResult.getResultUrl())) {
                    fileStorageOperate.deleteFile(appCaseResult.getResultUrl());
                }
                if (StringUtils.isNotEmpty(appCaseResult.getScreenshotUrl())) {
                    fileStorageOperate.deleteFile(appCaseResult.getScreenshotUrl());
                }
                appCaseResultMapper.deleteResultByExecuteId(id);

            }
        }

        return a;
    }
    @Override
    public List<StepReportVO> caseStepReport(Integer caseId, Integer surveyTaskId, String province) {
        return appCaseResultMapper.appCaseStepReport(caseId,surveyTaskId,province);
    }

    @Override
    public List<SurveyTaskReportVO> appCaseReportList(Integer surveyTaskId, String province) {
        //解析APP端 用例Id
        SysSurveyTask sysSurveyTask = sysSurveyTaskMapper.selectByPrimaryKey(surveyTaskId);
        if (sysSurveyTask == null) {
            return Collections.emptyList();
        }
        String json = sysSurveyTask.getCaseIds();
        JSONObject jsonObject = JsonUtil.parseJsonObject(json);
        List<Integer> testcaseIdList = (List<Integer>) jsonObject.get("app");
        if (testcaseIdList.size() == 0) {

            return Collections.emptyList();
        }
        for (int i=0;i<testcaseIdList.size();i++) {
            System.out.print(testcaseIdList.get(i));
        }
        List<SurveyTaskReportVO> surveyTaskReportVO=appCaseResultMapper.appCaseReportList(testcaseIdList, surveyTaskId, province);
            //不分省
            return surveyTaskReportVO;


    }
}
