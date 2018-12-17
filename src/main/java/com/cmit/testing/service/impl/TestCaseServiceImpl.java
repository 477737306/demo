package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.*;
import com.cmit.testing.dao.app.AppCaseMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.entity.vo.CommonResultVO;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.quartz.QuartzCronDateUtils;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.AppCaseDeviceService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.DateUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.ThreadUtils;
import com.cmit.testing.utils.ToolUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 用例ServiceImpl
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
@Service
public class TestCaseServiceImpl extends BaseServiceImpl<TestCase> implements TestCaseService {
    @Autowired
    private TestCaseMapper testCaseMapper;
    @Autowired
    private ParameterMapper parameterMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private ModelScriptService modelScriptService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;
    @Autowired
    private TestCaseReportService testCaseReportService;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private ScriptStepService scriptStepService;
    @Autowired
    private RecordStepMapper recordStepMapper;
    @Autowired
    private TimedTaskService timedTaskService;
    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private AppCaseMapper appCaseMapper;
    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;

    @Override
    public List<Map<String, List<TestCase>>> testCaseExecuteReorder(Map<String, List<Integer>> caseMap) {
        List<Map<String ,List<TestCase>>> testCaseList = ToolUtil.reorderByCaseIds(caseMap);
        return testCaseList;
    }

    @Override
    public int batchUpdateTestCase(List<TestCase> list) {
        return testCaseMapper.batchUpdateTestCase(list);
    }

    @Override
    public Integer saveTestCase(Map<String, Object> map, Integer id) {
        String careatBy = getShiroUser().getName();
        Integer executionType = Integer.parseInt(map.get("executionType").toString());
        String  executionDate = (String) map.get("executionDate");
        String cron = executionDate;
        String serialNumber = (String) map.get("serialNumber");
        Integer businessId = (Integer) map.get("businessId");
        if(executionType.equals(1)|| executionType.equals(2)) {
            if(executionType.equals(1)) {
                Date date = DateUtil.parse(cron, "yyyy-MM-dd HH:mm:ss");
                cron = QuartzCronDateUtils.getCron(date);
            }
            String regEx = "(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7])|([1-7])|(\\?)|(\\*)|(([1-7]L)|([1-7]\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7] )|([1-7] )|(\\? )|(\\* )|(([1-7]L )|([1-7]\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\-(19[789][0-9]|20[0-9][0-9])))";
            if(!Pattern.matches(regEx, cron)){
                throw new TestSystemException("cron表达式有误！");
            }
        }
        Business business = businessService.selectByPrimaryKey(businessId);
        if(isExistSerialNumber(business.getProjectId(),id,serialNumber)){
            throw new TestSystemException("用例编号已存在！");
        }
        TestCase testCase = new TestCase();
        testCase.setSerialNumber(serialNumber);
        if (null != id) {

            if (isFinish(id)) {
                throw new TestSystemException("该用例正在执行，不能更改！");
            }
            testCase = testCaseMapper.selectByPrimaryKey(id);
            testCase.setUpdateBy(careatBy);
            testCase.setUpdateTime(new Date());
            int isMerge = (int) map.get("isMerge");
            testCase.setIsMerge(isMerge);
        } else {
            testCase.setBusinessId(businessId);
            testCase.setCreateBy(careatBy);
            testCase.setUpdateBy(careatBy);
            testCase.setUpdateTime(new Date());
            testCase.setCreateTime(new Date());
            testCase.setMergeNum(0);
            testCase.setIsMerge(0);
            testCase.setSuccessNum(0);
            testCase.setFailureNum(0);
            testCase.setIsFinish(2);
            testCase.setExcuteNum(0);
            testCase.setOldTestcaseId(0);
            testCase.setSuccessRate(0.0);

        }
        testCase.setScriptId((Integer) map.get("scriptId"));
        testCase.setRetry((Integer) map.get("retry"));
        testCase.setInformation(map.get("information").toString());
        testCase.setIsRecord((Integer) map.get("isRecord"));
        testCase.setExecutionType(executionType);
        testCase.setExecutionDate(executionDate);
        testCase.setCode((Integer) map.get("code"));
        String followId = map.get("followId") == null ? null : map.get("followId").toString();
        // 如果依赖Id不存在却是这种形式：{"web":[],"app":[]}，则设置依赖Id为空
        if (ToolUtil.isNotEmpty(followId)) {
        	JSONObject jsonfollowId = JsonUtil.parseJsonObject(followId);
        	JSONArray webId = (JSONArray) jsonfollowId.get("web");
        	JSONArray appId = (JSONArray) jsonfollowId.get("app");
        	if (webId.size() == 0 && appId.size() == 0) {
        		followId = null;
        	}
        }
        testCase.setFollowId(followId);
        testCase.setName(map.get("name").toString());
        List<ScriptStep> scriptStepList = scriptStepService.selectByScriptId(testCase.getScriptId());

        List<String> multiParams = new ArrayList<>();//多参
        List<String> globalParams = new ArrayList<>();//全局
        for (ScriptStep scriptStep : scriptStepList) {
            if (scriptStep.getParamType().equals(1)) {
                multiParams.add(scriptStep.getParamKey());
            } else if (scriptStep.getParamType().equals(2)) {
                globalParams.add(scriptStep.getParamKey());
            }

        }
        if (null != id) {
            testCaseMapper.updateByPrimaryKey(testCase);
            //删除的参数记录
            parameterMapper.deleteByTestCaseId(testCase.getId());
            //删除之前的定时任务
            TimedTask timedTask = new TimedTask();
            timedTask.setTestCaseType("web");
            timedTask.setTestCaseId(id);
            timedTaskService.deleteTimedTask(timedTask);
        } else {
            testCaseMapper.insert(testCase);
        }
        List<Map<String, Object>> params = (List<Map<String, Object>>) map.get("params");
        for (Map<String, Object> map1 : params) {
            Parameter parameter = new Parameter();
            String key = map1.get("param").toString();
            String vaue = map1.get("value").toString();
            parameter.setParam(key);
            parameter.setValue(vaue);
            parameter.setTestcaseId(testCase.getId());
            if (multiParams.contains(key)) {
                parameter.setMultiFlag(1); //多参变量
            } else if (globalParams.contains(key)) {
                parameter.setMultiFlag(2); //全局
            }
            parameterMapper.insert(parameter);
        }
        int testCaseId = testCase.getId();
        //如果执行类型是0-立即执行,就立即执行用例
        if (testCase.getExecutionType().equals(0)) {
        	// 生成用例副本
        	TestCase t1 = scriptStepService.executeTestCaseBefore(testCase, false);
        	// 执行用例副本
        	scriptStepService.executeTestCase(t1);
        	// 判断用例是否执行结束
        	while (2 != ThreadUtils.getTestcaseExecuteStatus(t1.getId())) {
        		Thread.yield();
        	}
        	// 更改用例与用例副本的状态
        	scriptStepService.executeTestCaseAfter(t1);
        } else if (testCase.getExecutionType().equals(1) || testCase.getExecutionType().equals(2)) { //添加定时任务
            TimedTask timedTask = new TimedTask();
            timedTask.setTestCaseType("web");
            timedTask.setTestCaseId(testCase.getId());
            Integer type = 1; //0指定时间，1周期任务
            if (testCase.getExecutionType().equals(1)) {
                type = 0;
            }
            timedTask.setCron(cron);
            timedTask.setType(type);
            timedTaskService.addTimedTask(timedTask);
        }
        return testCaseId;
    }

    /**
     * 判断同一项目下用例编号是否重复
     * @param projectId 项目id
     * @param id 用例id
     * @param serialNumber 用例编号
     * @return
     */
    @Override
    public boolean isExistSerialNumber(Integer projectId,Integer id,String serialNumber){
        List<Integer> list = new ArrayList<>();
        list.addAll(testCaseMapper.getSerialNumber(projectId,id,serialNumber));
        list.addAll(appCaseMapper.checkCaseNumUnique(projectId, serialNumber, id));
        if(list!=null && list.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 是否正在执行 true(正在执行)，flase(未执行状态)
     *
     * @param testCaseId
     * @return
     */
    public Boolean isFinish(Integer testCaseId) {
        TestCase testCase = testCaseMapper.selectByPrimaryKey(testCaseId);
        return "1".equals(testCase.getIsFinish());
    }

    @Override
    public List<TestCase> getTestCasesBySurveyTaskId(Integer surveyTaskId) {

//        List<TestCase> list = testCaseMapper.getTestCasesBySurveyTaskId(surveyTaskIds);
//        SysSurveyTask sysSurveyTask = sysSurveyTaskService.selectByPrimaryKey(surveyTaskId);
//        return list;
        return null;
    }

    /**
     * 根据条件查询
     *
     * @param testCase
     * @return
     */
    @Override
    public List<TestCase> getListByTestCase(TestCase testCase) {
        return testCaseMapper.getListByTestCase(testCase);
    }

    /**
     * 脚本关联的用例分页
     *
     * @param testCase
     * @return
     */
    @Override
    public PageBean<TestCase> getPageByScriptId(PageBean<TestCase> pageBean, TestCase testCase) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        page.setOrderBy("code");
        pageBean.setItems(testCaseMapper.getListByTestCase(testCase));
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;
    }


    @Override
    public int deleteByIds(List<Integer> ids) {
        List<Integer> followIds = testCaseMapper.getIdsByFollowId(ids);

        if (followIds.size() > 0 && !ids.containsAll(followIds)) {
            throw new TestSystemException("用例被其他用例所依赖不能删除");
        }
        List<Integer> ids_ = testCaseMapper.getIdsByOldIds(ids);
        if (ids_.size() > 0) {
            List<TestCaseReport> testCaseReports = testCaseReportMapper.getIdsByTestCaseIds(ids_);
            List<Integer> reportIds = new ArrayList<>();
            if(testCaseReports.size()>0) {
                for (TestCaseReport testCaseReport : testCaseReports) {
                    reportIds.add(testCaseReport.getId());
                    if(StringUtils.isNotEmpty(testCaseReport.getSnapshotLocation())) {
                        fileStorageOperate.deleteFile(testCaseReport.getSnapshotLocation());//删除fastdfs文件服务器中的图片
                    }
                    if(StringUtils.isNotEmpty(testCaseReport.getVideoLocation())) {
                        fileStorageOperate.deleteFile(testCaseReport.getVideoLocation());//删除fastdfs文件服务器中的视频
                    }
                }
                recordStepMapper.deleteByTestcaseReportIds(reportIds);//删除用例执行记录
            }

            testCaseReportMapper.deleteByTestcastIds(ids_);//删除用例报告
            for (Integer id : ids) { //删除定时任务
                TimedTask timedTask = new TimedTask();
                timedTask.setTestCaseType("web");
                timedTask.setTestCaseId(id);
                timedTaskService.deleteTimedTask(timedTask);
            }
        }
        parameterMapper.deleteByTestCaseIds(ids);//删除参数表
        int a = testCaseMapper.deleteByIds(ids);//删除原用例和副本

        return a;
    }

    @Override
    public int updateByPrimaryKey(TestCase record) {
        return testCaseMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<PbtVO> findByPage(String name, Integer businessId) {
        return testCaseMapper.findByPage(name, businessId);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return testCaseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TestCase selectByPrimaryKey(Integer id) {
        TestCase testCase = testCaseMapper.selectByPrimaryKey(id);
        if (testCase != null) {
            List<Parameter> parameters = parameterMapper.getByTaseCaseId(id);
            for (Parameter parameter : parameters) {
                if ("phone".equals(parameter.getParam())) {
                    String[] phones = parameter.getValue().split(",");
                    testCase.setPhoneNumber(phones.length);
                    String provice = "";
                    int a = 0;
                    for (String phone : phones) {
                        SimCard simCard = simCardMapper.getByPhone(phone);
                        if (simCard == null) {
                            break;
                        }
                        if (phones.length > 0 && a < phones.length - 1) {
                            provice += simCard.getProvince() + ",";
                        } else {
                            provice += simCard.getProvince();
                        }
                        a++;
                    }
                    parameter.setProvince(provice);
                }
            }
            testCase.setParams(parameters);
        }

        return testCase;
    }

    @Override
    public boolean copy(List<SysPermission> list, int menuParentId) {
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        int result = 0;
        for (SysPermission sysPermission : list) {
            TestCase testCase = this.selectByPrimaryKey(sysPermission.getDataId());
            ModelScript modelScript = modelScriptService.getModelScriptById(testCase.getScriptId());
            int scriptId = modelScriptService.copyData(modelScript);
            testCase.setId(null);
            testCase.setScriptId(scriptId);
            testCase.setIsMerge(0);
            testCase.setMergeNum(0);
            testCase.setSerialNumber(null);
            testCase.setName(sysPermission.getName());
//            Business business = businessService.selectByPrimaryKey(testCase.getBusinessId());
            testCase.setBusinessId(parentSysPermission.getDataId());
            testCaseMapper.insert(testCase);
            sysPermission.setId(null);
            sysPermission.setDataId(testCase.getId());
            sysPermission.setParentId(menuParentId);
            sysPermissionService.saveProjectPermission(sysPermission);

        }
        return result > 0;
    }

    @Override
    public boolean shear(List<SysPermission> list, int menuParentId) {
        int caseResult = 0;
        int sysResult = 0;
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission : list) {
            //更新菜单
            sysPermission.setParentId(menuParentId);
            sysResult += sysPermissionService.updateByPrimaryKey(sysPermission);
            //更新用例数据
            TestCase testCase = testCaseMapper.selectByPrimaryKey(sysPermission.getDataId());
            testCase.setBusinessId(parentSysPermission.getDataId());
            testCase.setCode(parentSysPermission.getCode());
//            Business business = businessService.selectByPrimaryKey(testCase.getBusinessId());
            caseResult += testCaseMapper.updateByPrimaryKey(testCase);
        }
        return sysResult > 0 && caseResult > 0;
    }

    @Override
    public Map<String, Integer> getTestCaseMaxExcuteNum(Integer id) {
        return testCaseMapper.getTestCaseMaxExcuteNum(id);
    }


    /**
     * 根据任务id查询用例
     *
     * @param taskId
     * @return
     */
    @Override
    public PageBean<TestCase> getTestCaseByTaskId(PageBean<TestCase> pageBean, Integer taskId) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        page.setOrderBy("code");
        pageBean.setItems(testCaseMapper.getTestCaseByTaskId(taskId));
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;

    }

    /**
     * 通过业务查询用例
     */
    @Override
    public List<TestCase> selectTastCaseTive(TestCase testCase) {
        return testCaseMapper.selectTastCaseTive(testCase);
    }

    /**
     * 获取批次号
     *
     * @param id
     * @return
     */
    @Override
    public List<Long> getExcuteNumById(Integer id) {
        return testCaseMapper.getExcuteNumById(id);
    }

    /**
     * 根据用例list生成任务报告
     *
     * @param list
     * @return
     */
    @Override
    public List<Map<String, Object>> getTestCaseReportByTaskId(List<TestCase> list ,Integer number) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        String phone = null;
        String province = null;
        for (TestCase testCase : list) {  //遍历用例,根据用例id获得报告列表
            List<TestCaseReport> reportList = new ArrayList<>();
            if(number < 0){
                reportList = testCaseReportService.getTestCaseReportByCaseId(testCase.getId(),null);
            }else{
                reportList = testCaseReportService.getCaseReportByCaseIdAndExcuteNum(testCase.getId(),number);
            }

            for (TestCaseReport testCaseReport : reportList) {//遍历报告
                Map<String, Object> map = new HashMap<>();
                Map<String, String> map1 = new HashMap<>();
                List<Map<String, String>> listin = new ArrayList<>();

                map1.put("testCaseId", testCaseReport.getOldTestcaseId() + "");
                map1.put("type", "web");
                map1.put("businessId", testCase.getBusinessId() + "");
//                map1.put("testCaseName",testCase.getName());
                map1.put("passStatus", testCaseReport.getPassStatus() + "");
                phone = testCaseReport.getPhoneNum();
                province = testCaseReport.getProvince();
                if (listMap.size() > 0) {
                    boolean flag = false;
                    for (Map<String, Object> map2 : listMap) {
                        if (map2.get("province").equals(province)) {
                            //如果下一个报告的手机号等于已经存入的map的手机号，就再添加map1，并且重新存入集合
                            listin = (List<Map<String, String>>) map2.get("caseAll");
                            listin.add(map1);
                            map2.put("caseAll", listin);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        //手机号不相同
                        listin.add(map1);
                        map.put("caseAll", listin);
                        map.put("phone", phone);
                        map.put("province", province);
                        listMap.add(map);
                    }
                } else {
                    //添加第一个
                    listin.add(map1);
                    map.put("caseAll", listin);
                    map.put("phone", phone);
                    map.put("province", province);
                    listMap.add(map);
                }
            }
        }
        return listMap;
    }

    /**
     * 统计业务下用例的成功数与失败数
     * @param businessId
     * @return
     */
    @Override
    public Map<String, Long> testCaseCountNumber(Integer businessId) {
        return testCaseMapper.testCaseCountNumber(businessId);
    }

    @Override
    public List<TestCase> getTestCaseBySurTaskIdAndCaseId(Integer surveyTaskId, Integer id) {
        return testCaseMapper.getTestCaseBySurTaskIdAndCaseId(surveyTaskId, id);
    }

    /**
     * 更改原用例的成功、失败、执行状态等数据
     *
     * @param newId（新的用例id）
     * @return
     */
    @Override
    public void updateTestcaeData(Integer newId) {
        TestCase testCase = testCaseMapper.selectByPrimaryKey(newId);
        TestCase testCase1 = testCaseMapper.selectByPrimaryKey(testCase.getOldTestcaseId());
        Map<String, Long> map = testCaseReportMapper.getSuccessNumAndFailureNumByTestcastId(newId);

        Long successNum = map.get("successNum");
        Long failureNum = map.get("failureNum");

        testCase1.setIsFinish(0);
        testCase1.setSuccessRate(successNum / (successNum + failureNum) * 1d);
        testCase1.setFailureNum(failureNum.intValue());
        testCase1.setSuccessNum(successNum.intValue());
        testCaseMapper.updateByPrimaryKey(testCase1);
    }

    /**
     * 分页查询所有执行中的用例
     *
     * @param pageBean
     * @param testCaseReportVO
     * @return
     */
    @Override
    public PageBean<TestCase> getAllExecutingCase(PageBean<TestCase> pageBean, TestCaseReportVO testCaseReportVO) {
        TestCase testCase = testCaseMapper.selectByPrimaryKey(testCaseReportVO.getTestcastId());
        if(testCase != null){
            testCaseReportVO.setSerialNumber(testCase.getSerialNumber());
        }
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<TestCase> allExecutingCase = testCaseMapper.getAllExecutingCase(testCaseReportVO);
        PageBean<TestCase> page_ = new PageBean<>(pageBean.getCurrentPage(), pageBean.getPageSize(), (int) page.getTotal());
        page_.setItems(allExecutingCase);
        return page_;
    }

    /**
     * 根据执行类型查询用例父模板
     *
     * @param executionType 执行类型 0立即执行，1指定时间执行，2指定周期执行，3不执行
     * @return
     */
    @Override
    public List<TestCase> getTestCaseByExeType(Integer executionType) {
        return testCaseMapper.getTestCaseByExeType(executionType);
    }

    @Override
    public List<DownloadFileDto> downloadZipScreenshots(TestCaseReportVO testCaseReport) {
        List<TestCaseReportVO> testCaseReports = testCaseReportService.getTestCaseReport4Zip(testCaseReport);
        if("app".equals(testCaseReport.getType())){
            testCaseReports = new ArrayList<>();
        }
        List<DownloadFileDto> downloadFileDtoList =new ArrayList<>();
        List<String> fileNameList =new ArrayList<>();
        Map<String,Integer> countMap = new HashMap<>();
        for (TestCaseReportVO caseReport : testCaseReports) {
                //获取fastDFS服务器的截图
                    if(StringUtils.isNotBlank(caseReport.getSnapshotLocation())){
                    DownloadFileDto downloadFileDto =new DownloadFileDto();
                    byte[] data = fileStorageOperate.downloadFile(caseReport.getSnapshotLocation());
                    if(null == data || data.length<1)
                        data = new byte[1];
                    downloadFileDto.setByteDataArr(data);
                    //去除压缩文件名称相同的情况(根据查询条件拼接文件名)
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("/"+caseReport.getProjectName());
                    stringBuffer.append("/第"+caseReport.getExcuteNum()+"轮");
                    stringBuffer.append("/"+caseReport.getBusinessName());
                    stringBuffer.append("/"+caseReport.getProvince());
                    stringBuffer.append("/"+caseReport.getTestcastName()+"-"+caseReport.getProvince());
                    if(null != caseReport.getExcuteResult() && 1 ==caseReport.getExcuteResult())
                        stringBuffer.append("-不通过");
                    //同一用例一省多报告
                    for (DownloadFileDto fileDto : downloadFileDtoList) {
                        if(fileDto.getFileName().equals(stringBuffer+".jpg"))
                            fileDto.setFileName(fileDto.getFileName().replace(".jpg","(1).jpg"));
                    }
                    //序号区分
                    if(fileNameList.contains(stringBuffer.toString())){
                        countMap = ToolUtil.listCount(countMap,fileNameList.toArray());
                        stringBuffer.append("("+countMap.get(stringBuffer.toString())+")");
                    }
                    downloadFileDto.setFileName(stringBuffer+".jpg");
                    downloadFileDtoList.add(downloadFileDto);
                    fileNameList.add(stringBuffer.toString());
                }
        }

        // add app 相关截图信息
        if (testCaseReport != null)
        {
            if (!"web".equals(testCaseReport.getType()))
            {
                List<Integer> list = testCaseReport.getAppTestCaseReportIds();
                if (CollectionUtils.isNotEmpty(list))
                {
                    CommonResultVO commonResultVO = new CommonResultVO();
                    commonResultVO.setIds(list);
                    downloadFileDtoList.addAll(appCaseService.downloadCompressPicZip(commonResultVO));
                }
                else
                {
                    CommonResultVO commonResultVO = new CommonResultVO();
                    commonResultVO.setCaseName(testCaseReport.getTestcastName());
                    commonResultVO.setExecuteNum(testCaseReport.getExcuteNum());
                    commonResultVO.setBusinessName(testCaseReport.getBusinessName());
                    commonResultVO.setProvince(testCaseReport.getProvince());
                    // 0失败，1成功
                    String executeResult = testCaseReport.getExcuteResult() == null ? null : testCaseReport.getExcuteResult().toString();
                    commonResultVO.setExecuteResult(executeResult);
                    downloadFileDtoList.addAll(appCaseService.downloadCompressPicZip(commonResultVO));
                }
            }
        }

        return downloadFileDtoList;
    }

    @Override
    public Integer getExecutingCountByBusiness(Integer businessId) {
        return testCaseMapper.getExecutingCountByBusiness(businessId);
    }

	@Override
	public List<TestCase> getTestCasesByOldTestcaseIdAndSysTaskId(Integer oldTestcaseId, Integer sysTaskId) {
		return testCaseMapper.getTestCasesByOldTestcaseIdAndSysTaskId(oldTestcaseId, sysTaskId);
	}

    /**
     * 根据用例对象的属性-依赖id,获取用例的所有依赖用例(且执行成功)的手机号
     * @param followIds 用例对象的属性-依赖id
     * @return
     */
    public List<String> getPhoneListByFollowIds(String followIds) {
        List<String> list = new ArrayList<>();
        Map<String, List<Integer>> caseMap = (Map<String, List<Integer>>) JSON.parse(followIds);
        List<Integer> webList = caseMap.get("web");
        List<Integer> appList = caseMap.get("app");
        for (Integer followId : webList) {
            TestCase te = testCaseMapper.getTestCaseByCaseId(followId);
            List<TestCaseReport> temp = testCaseReportService.getTestCaseReportByCaseId(te.getId(), 0);
            if (temp != null) {
                List<String> phoneList = new ArrayList<>();
                for (TestCaseReport tcr : temp) {
                    phoneList.add(tcr.getPhoneNum());
                }
                if (list == null || list.size() == 0) {
                    list = phoneList;
                } else {
                    list.retainAll(phoneList);
                }
            }
        }
        for (Integer appFollowId : appList) {
            List<String> phoneNumList = appCaseDeviceService.getPhoneNumList(appFollowId, "0", null);
            if (list == null || list.size() == 0) {
                list = phoneNumList;
            } else {
                list.retainAll(phoneNumList);
            }
        }
        return list;
    }

    /**
     * 获取用例的手机号
     * @param testCase
     * @return
     */
    @Override
    public List<String> getPhoneListByTestCase(TestCase testCase) {
    	List<String> list = new ArrayList<>();
    	List<Parameter> mList = new ArrayList<>();
    	// 设置用例参数为1-多参，再从参数表中获取该用例的手机号
        Parameter param = new Parameter();
        param.setTestcaseId(testCase.getOldTestcaseId());
        param.setMultiFlag(1);// 1-多参
        mList = parameterMapper.findParameter(param);// 多参变量
        // 如果参数表没有该用例手机号,则获取该用例的依赖用例的手机号
        if (mList == null || mList.size() == 0) {
            if (StringUtils.isNotEmpty(testCase.getFollowId())) {
                list = getPhoneListByFollowIds(testCase.getFollowId());
                String phoneList = "";
                if (null != list && list.size() > 0) {
                	for (String phone : list) {
                		phoneList += phone + ",";
                	}
                	phoneList = phoneList.substring(0, phoneList.length() - 1);
                	param.setValue(phoneList);
                	param.setParam("phone");
                	mList.add(param);
                }
            }
        }
        if (mList != null && mList.size() > 0) {
        	if (ToolUtil.isNotEmpty(mList.get(0).getValue())) {
        		String[] pList = mList.get(0).getValue().split(",");
        		list = Arrays.asList(pList);
        	}
        }
		return list;
    }
}
