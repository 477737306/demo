package com.cmit.testing.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.dao.SysSurveyTaskMapper;
import com.cmit.testing.dao.SysTaskMapper;
import com.cmit.testing.dao.app.*;
import com.cmit.testing.entity.*;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.CmdParamStartTask;
import com.cmit.testing.entity.proxy.StepDetails;
import com.cmit.testing.entity.proxy.TaskBody;
import com.cmit.testing.entity.proxy.TestResultInfo;
import com.cmit.testing.entity.vo.*;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.listener.msg.DeviceMessage;
import com.cmit.testing.listener.websocket.DeviceListWebSocketServer;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.quartz.QuartzCronDateUtils;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.*;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.*;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.utils.file.ZipUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/28 0028 下午 2:28.
 */
@Service("appCaseService")
public class AppCaseServiceImpl extends BaseServiceImpl<AppCase> implements AppCaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppCaseServiceImpl.class);

    @Autowired
    private AppCaseMapper appCaseMapper;
    @Autowired
    private AppScriptMapper appScriptMapper;
    @Autowired
    private AppCaseDeviceMapper appCaseDeviceMapper;
    @Autowired
    private AppCaseResultMapper appCaseResultMapper;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AppScriptService appScriptService;
    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;
    @Autowired
    private AppRecordStepMapper appRecordStepMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProjectMenuService projectMenuService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private AppCaseResultService appCaseResultService;
    @Autowired
    private AppRecordStepService appRecordStepService;
    @Autowired
    private AppScriptStepMapper stepMapper;
    @Autowired
    private SysSurveyTaskMapper sysSurveyTaskMapper;
    @Autowired
    private SysSurveyTaskService sysSurveyTaskService;
    @Autowired
    private SysTaskMapper sysTaskMapper;
    @Autowired
    private TimedTaskService timedTaskService;
    @Autowired
    private TestCaseService testCaseService;

    @Override
    public Integer deleteByIds(List<Integer> ids) {

        /*List<Integer> caseIdList = appCaseMapper.getIdsByFollowIds(ids);
        if (CollectionUtils.isNotEmpty(caseIdList) && !ids.containsAll(caseIdList))
        {
            throw new TestSystemException("当前用例被其他用例所依赖不能删除");
        }*/
        for (Integer caseId : ids)
        {
            AppCase appCase = appCaseMapper.getAppCaseById(caseId);
            if (appCase == null)
            {
                continue;
            }
            if ("1".equals(appCase.getIsFinish().toString()))
            {
                throw new TestSystemException("执行中的用例不能删除");
            }
        }

        // 用于存储文件服务器上文件的路径，方便于删除
        List<String> fileList = new ArrayList<>();
        // 用于存储结果ID 方便于删除步骤记录表数据
        List<Integer> resultIds = new ArrayList<>();

        List<Integer> copyIdList_ = appCaseMapper.getIdsByOldCaseIds(ids);
        if (CollectionUtils.isNotEmpty(copyIdList_))
        {
            // 删除副本相关联的数据
            List<AppCase> copyCaseList = appCaseMapper.getCaseListByIds(copyIdList_);
            for (AppCase appCase : copyCaseList)
            {
                if (StringUtils.isNotEmpty(appCase.getTaskZipPath()))
                {
                    fileList.add(appCase.getTaskZipPath().trim());
                }
                if (StringUtils.isNotEmpty(appCase.getResultFilePath()))
                {
                    fileList.add(appCase.getResultFilePath().trim());
                }
                // 删除app_case_device、app_case_result、app_record_step
                List<AppCaseResult> resultList = appCaseResultService.getResultIdsByCaseId(appCase.getCaseId());
                for (AppCaseResult appCaseResult : resultList)
                {
                    resultIds.add(appCaseResult.getResultId());

                    if (StringUtils.isNotEmpty(appCaseResult.getLogUrl()))
                    {
                        fileList.add(appCaseResult.getLogUrl().trim());
                    }
                    if (StringUtils.isNotEmpty(appCaseResult.getRecordVideoUrl()))
                    {
                        fileList.add(appCaseResult.getRecordVideoUrl().trim());
                    }
                    if (StringUtils.isNotEmpty(appCaseResult.getResultUrl()))
                    {
                        fileList.add(appCaseResult.getResultUrl().trim());
                    }
                    if (StringUtils.isNotEmpty(appCaseResult.getScreenshotUrl()))
                    {
                        List<String> screenShotUrls = Arrays.asList(appCaseResult.getScreenshotUrl().split(","));
                        for (String url : screenShotUrls)
                        {
                            if (StringUtils.isNotEmpty(url))
                            {
                                fileList.add(url.trim());
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(resultIds))
            {
                List<AppRecordStep> recordStepList = appRecordStepMapper.getAllStepByResultIds(resultIds);
                for (AppRecordStep recordStep : recordStepList)
                {
                    if (StringUtils.isNotEmpty(recordStep.getScreenShotUrl()))
                    {
                        fileList.add(recordStep.getScreenShotUrl().trim());
                    }
                }
            }

            // 开始删除数据
            if (CollectionUtils.isNotEmpty(resultIds))
            {
                appRecordStepMapper.deleteByCaseResultIds(resultIds);
                appCaseResultMapper.deleteResultByIds(resultIds);
            }
            appCaseDeviceMapper.deleteDeviceByCaseIds(copyIdList_);
        }

        // 删除定时任务
        for(Integer caseId : ids)
        {
            TimedTask timedTask = new TimedTask();
            timedTask.setTestCaseType("app");
            timedTask.setTestCaseId(caseId);
            timedTaskService.deleteTimedTask(timedTask);
        }
        // 删除原用例相关信息
        appCaseDeviceMapper.deleteDeviceByCaseIds(ids);
        for (Integer caseId : ids)
        {
            AppCase appCase = appCaseMapper.getParentCaseByCaseId(caseId);
            if (appCase != null)
            {
                if (StringUtils.isNotEmpty(appCase.getTaskZipPath()))
                {
                    fileList.add(appCase.getTaskZipPath().trim());
                }
                if (StringUtils.isNotEmpty(appCase.getResultFilePath()))
                {
                    fileList.add(appCase.getResultFilePath().trim());
                }
            }

        }
        int i = appCaseMapper.deleteByIds(ids);

        // 删除FastDFS文件服务器上的文件
        for (String fileId : fileList)
        {
            fileStorageOperate.deleteFile(fileId);
        }

        return i;
    }

    /**
     * 更新保存用例
     */
    @Override
    public Integer saveAppCase(Map<String, Object> map, Integer id)
    {
        Integer createPerson = ShiroKit.getUser().getId();
        AppCase ac = new AppCase();
        Date currentTime = new Date();
        // 用例执行类型：0-立即执行，1-指定时间执行，2-指定周期执行，3-不执行
        Integer exeType = (Integer) map.get("executeType");
        if (null != id)
        {
            // 更新数据
            ac = appCaseMapper.selectByPrimaryKey(id);
            if ("1".equals(ac.getIsFinish()))
            {
                throw new TestSystemException("该用例正在执行，不能修改！");
            }
            ac.setUpdatePerson(createPerson);
            ac.setUpdateTime(currentTime);
            //修改用例时，根据勾选来确定是否开启下一轮测试(0-不开启；1-开启)
            // 开启下一轮测试已被废弃 add by 2018-11-7
            //ac.setIsMerge((Integer) map.get("isMerge"));
            ac.setIsMerge(0);

            if ((Constants.EXECUTE_TYPE_DOTNOT.equals(exeType) || Constants.EXECUTE_TYPE_IMMEDIATE.equals(exeType)) &&
                    (Constants.EXECUTE_TYPE_TIMING.equals(ac.getExecuteType()) || Constants.EXECUTE_TYPE_CYCLE.equals(ac.getExecuteType())))
            {
                // 不执行  若是定时任务或周期任务改成不执行，则需将对应的定时任务从任务队列中移除
                TimedTask timedTask = new TimedTask();
                timedTask.setTestCaseId(ac.getCaseId());
                timedTask.setTestCaseType("app");
                timedTaskService.deleteTimedTask(timedTask);

                ac.setExecuteTime(null);
                ac.setCronTime(null);
            }

        }
        else
        {
            // 新增数据 dataId为当前用例所属业务的节点id(sys_permission表的主键)
            Integer permissionId = (Integer) map.get("dataId");
            SysPermission sysPermission = new SysPermission();
            sysPermission = sysPermissionService.selectByPrimaryKey(permissionId);
            Integer businessId = sysPermission.getDataId();
            ac.setBusinessId(businessId);
            ac.setCreateTime(currentTime);
            ac.setCreatePerson(createPerson);
            ac.setUpdateTime(currentTime);
            ac.setUpdatePerson(createPerson);
            // 新建用例时，默认不开启下一轮测试
            ac.setIsMerge(0);
        }
        Business business = businessService.selectByPrimaryKey(ac.getBusinessId());
        String caseNum = (String) map.get("caseNum");
        if (testCaseService.isExistSerialNumber(business.getProjectId(), id, caseNum))
        {
            throw new TestSystemException("当前用例编号已存在");
        }
        ac.setCaseName((String) map.get("caseName"));
        ac.setCaseNum(caseNum);
        ac.setCaseDesc((String) map.get("caseDesc"));
        ac.setScriptId((Integer) map.get("scriptId"));
        ac.setIsRecord((Integer) map.get("isRecord"));

        ac.setIsFinish(2);
        // 执行次数，页面展示的默认为1
        ac.setRetryCount((Integer) map.get("retryCount"));
        // 执行轮次 默认为0，即是批次号
        ac.setExecuteCount(0);
        // 原用例ID，默认为0
        ac.setOldCaseId(0);

        if (Constants.EXECUTE_TYPE_TIMING.equals(exeType))
        {
            // 定时执行任务
            String t = (String) map.get("cronTime");

            if (StringUtils.isEmpty(t))
            {
                throw new TestSystemException("未设置指定执行时间");
            }

            Date date = DateUtil.parse(t, "yyyy-MM-dd HH:mm:ss");
            String cronTime = QuartzCronDateUtils.getCron(date);
            ac.setCronTime(cronTime);
            ac.setExecuteTime(date);
            if (null != id && Constants.EXECUTE_TYPE_CYCLE.equals(ac.getExecuteType()))
            {
                // 周期任务改成定时任务，先将周期任务移除任务队列
                TimedTask timedTask = new TimedTask();
                timedTask.setTestCaseId(ac.getCaseId());
                timedTask.setTestCaseType("app");
                timedTask.setType(1);
                timedTaskService.deleteTimedTask(timedTask);
            }
        }
        else if (Constants.EXECUTE_TYPE_CYCLE.equals(exeType))
        {
            String cronTime = (String) map.get("cronTime");

            if (StringUtils.isEmpty(cronTime))
            {
                throw new TestSystemException("未设置周期");
            }
            if(!CronExpression.isValidExpression(cronTime))
            {
                throw new TestSystemException("CRON表达式不合法！");
            }
            ac.setCronTime(cronTime);

            if (null != id && Constants.EXECUTE_TYPE_TIMING.equals(ac.getExecuteType()))
            {
                // 定时任务改成周期任务，先将定时任务移除任务队列
                TimedTask timedTask = new TimedTask();
                timedTask.setTestCaseId(ac.getCaseId());
                timedTask.setTestCaseType("app");
                timedTask.setType(0);
                timedTaskService.deleteTimedTask(timedTask);
            }
            ac.setExecuteTime(null);
        }

        ac.setSuccessCount(0);
        ac.setFailureCount(0);
        ac.setSuccessRate("0.0");

        if (map.get("orderCode") != null && StringUtils.isNotEmpty(map.get("orderCode").toString()))
        {
            ac.setOrderCode(Integer.parseInt(map.get("orderCode").toString()));
        }

        ac.setExecuteType(exeType);
        if (null != id)
        {
            appCaseMapper.updateByPrimaryKeySelective(ac);
        }
        else
        {
            appCaseMapper.saveAppCase(ac);
        }

        int appCaseId = ac.getCaseId();

        // 没有设置依赖关系，保存用例关联的设备
        //if (StringUtils.isEmpty(ac.getFollowId()))
        //{
        List<AppCaseDevice> caseDevices = new ArrayList<AppCaseDevice>();
        List<Map> deviceList = (List<Map>) map.get("deviceList");
        for (Map m : deviceList)
        {
            AppCaseDevice caseDevice = new AppCaseDevice();
            caseDevice.setCaseId(appCaseId);
            caseDevice.setDeviceId(Integer.valueOf(m.get("deviceId").toString()));
            // 设置轮次，母本用例的轮次为0
            caseDevice.setExecuteNum(0);
            caseDevice.setParamType("2");
            caseDevice.setPassStatus(2);
            caseDevice.setExecuteStatus(0);
            caseDevice.setIsPush(0);
            caseDevice.setProvince(m.get("province").toString());
            caseDevice.setTelNum(m.get("telNum").toString());

            caseDevices.add(caseDevice);
        }
        // 通过原用例ID删除app_case_device表中的数据，原用例关联的device数据不用于执行
        appCaseDeviceService.deleteByCaseId(appCaseId);
        if (CollectionUtils.isNotEmpty(caseDevices))
        {
            appCaseDeviceService.insertList(caseDevices);
        }
        //}

        // 新增用例, 需要给用例打包一个任务zip包
        buildZipByCase(ac);

        if (Constants.EXECUTE_TYPE_IMMEDIATE.equals(ac.getExecuteType()))
        {
            ExecuteCaseBO caseBO = new ExecuteCaseBO();
            caseBO.setCaseId(ac.getCaseId());
            caseBO.setType(AppConstants.EXECUTE_TYPE_SINGLECASE);
            caseBO.setRealExecuteNum(1);
            taskService.commonExecute(caseBO);
        }
        else if (Constants.EXECUTE_TYPE_TIMING.equals(ac.getExecuteType()) || Constants.EXECUTE_TYPE_CYCLE.equals(ac.getExecuteType()))
        {
            // 定时任务和周期任务， 添加定时任务或周期任务
            TimedTask timedTask = new TimedTask();
            timedTask.setTestCaseId(appCaseId);
            timedTask.setTestCaseType("app");
            //0-指定时间，1-周期任务
            Integer type = 1;
            if(Constants.EXECUTE_TYPE_TIMING.equals(ac.getExecuteType()))
            {
                type = 0;
            }
            timedTask.setCron(ac.getCronTime());
            timedTask.setType(type);
            timedTaskService.addTimedTask(timedTask);
        }

        return appCaseId;
    }

    @Override
    public AppCase getAppCaseById(Integer id) {

        List<AppCaseDevice> deviceList = appCaseDeviceService.getListByCaseId(id);
        List<AppDeviceVO> deviceVOList = new ArrayList<>();
        for (AppCaseDevice d : deviceList)
        {
            AppDeviceVO deviceVO = new AppDeviceVO();
            deviceVO.setDeviceId(d.getDeviceId());
            deviceVO.setTelNum(d.getTelNum());
            deviceVO.setProvince(d.getProvince());
            deviceVOList.add(deviceVO);
        }
        AppCase appCase = appCaseMapper.getAppCaseById(id);
        if (appCase != null)
        {
            appCase.setDeviceList(deviceVOList);
            appCase.setDeviceNum(deviceVOList.size());
            if (appCase.getExecuteType().equals(1))
            {
                // 定時調度任務
                String date = DateUtil.formatDate(appCase.getExecuteTime(), "yyyy-MM-dd HH:mm:ss");
                appCase.setCronTime(date);
            }
        }
        return appCase;
    }

    /**
     * 根据众测任务ID 和 CaseId 获取众测任务关联用例的副本信息
     * @param surveyTaskId 众测任务ID
     * @param caseId  原用例ID
     * @return  用例副本信息
     */
    @Override
    public List<AppCase> getAppCaseBySurveyTaskIdAndCaseId(Integer surveyTaskId, Integer caseId) {
        List<AppCase> copyCaseList = appCaseMapper.getAppCaseBySurveyTaskIdAndCaseId(surveyTaskId, caseId);
        return  copyCaseList;
    }

    @Override
    public List<AppCase> getAppCaseTaskIdAndCaseId(Integer sysTaskId, Integer caseId) {

        return appCaseMapper.getAppCaseTaskIdAndCaseId(sysTaskId, caseId);
    }

    @Override
    public List<AppCase> getList(AppCase ac) {
        return appCaseMapper.getList(ac);
    }

    @Override
    public Map<String, String> buildZipByCaseId(Integer caseId)
    {
        AppCase parentAppCase = appCaseMapper.getParentCaseByCaseId(caseId);
        if (parentAppCase != null)
        {
            Map<String, String> map = new HashMap<>();
            buildZipByCase(parentAppCase);
            map.put("md5Code", parentAppCase.getZipMd5Code());
            map.put("taskZipFile", parentAppCase.getTaskZipPath());
            return map;
        }
        return null;
    }

    @Override
    public Map<String, String> buildZipByCase(AppCase parentAppCase) {

        String md5Code = "";
        String taskZipFile = "";

        String zipBasePath = ConstantUtil.TASK_TEMP_URL + System.currentTimeMillis() + File.separator + parentAppCase.getCaseId();
        FolderUtils.createFolder(zipBasePath);

        String taskTxt = zipBasePath + File.separator + "task.txt";
        if (parentAppCase.getScriptId() == null)
        {
            throw new TestSystemException("无关联的脚本，无法执行");
        }
        AppScriptVO scriptVo = appScriptService.getAppScriptByScriptId(parentAppCase.getScriptId());

        TaskBody tb = new TaskBody();

        tb.setAppCaseId(parentAppCase.getCaseId());
        tb.setRetryCount(parentAppCase.getRetryCount());
        tb.setIsRecord(parentAppCase.getIsRecord());
        tb.setIsInstall(scriptVo.getIsInstall());
        tb.setIsUninstall(scriptVo.getIsUninstall());
        tb.setExecOrder(1);

        // 由于目前支持单个脚本只有单个文件，故可以如下写法
        AppScriptFileVO fileVo = scriptVo.getFileVoList().get(0);
        try
        {
            byte[] fileBytes = fileStorageOperate.downloadFile(fileVo.getExecuteJarUrl());
            if (fileBytes == null)
            {
                FolderUtils.deleteDirectory(new File(zipBasePath).getParent());
                throw new TestSystemException("没有找到对应的脚本Jar文件");
            }
            String jarFilePath = "scripts" + File.separator + fileVo.getScriptFileId() + ".jar";
            File jarFile = new File(zipBasePath + File.separator + jarFilePath);
            FileUtils.writeByteArrayToFile(jarFile, fileBytes);
            String jarFileMd5Code = Md5Utils.getFileMD5Str(jarFile.getPath());

            if (!jarFileMd5Code.equalsIgnoreCase(fileVo.getJarMd5Code()))
            {
                FolderUtils.deleteDirectory(new File(zipBasePath).getParent());
                throw new TestSystemException("Jar文件的MD5值异常");
            }

            tb.setJarPath(jarFilePath);

            byte[] apkBytes = fileStorageOperate.downloadFile(fileVo.getAppUrl());
            if (apkBytes == null)
            {
                FolderUtils.deleteDirectory(new File(zipBasePath).getParent());
                throw new TestSystemException("没有找到对应的APK文件");
            }
            String apkFilePath = "apk" + File.separator + fileVo.getAppId() + ".apk";
            File apkFile = new File(zipBasePath + File.separator + apkFilePath);
            FileUtils.writeByteArrayToFile(apkFile, apkBytes);
            String apkFileMd5Code = Md5Utils.getFileMD5Str(apkFile.getPath());
            if (!apkFileMd5Code.equals(fileVo.getAppMd5Code()))
            {
                FolderUtils.deleteDirectory(new File(zipBasePath).getParent());
                throw new TestSystemException("APK文件的MD5值异常");
            }
            tb.setApkPath(apkFilePath);

            List<TaskBody> caseList = new ArrayList<>();
            Map<String, Object> map = new HashMap<String, Object>();

            caseList.add(tb);

            map.put("caseList", caseList);
            File taskFile = new File(taskTxt);
            FileUtils.writeStringToFile(taskFile, JSONObject.toJSONString(map), "UTF-8");

            // 压缩zip包，并上传到文件服务器
            File zipFile = new File(zipBasePath + ".zip");
            ZipUtils.zipFiles(zipFile, zipBasePath);

            md5Code = Md5Utils.getFileMD5Str(zipFile.getPath());
            taskZipFile = fileStorageOperate.fileStorageUploadFile(zipFile);

            if (StringUtils.isNotEmpty(taskZipFile))
            {
                FolderUtils.deleteDirectory(zipFile.getParent());
            }
            else
            {
                throw new TestSystemException("任务zip包文件上传失败");
            }
        }
        catch (Exception e)
        {
            if (StringUtils.isNotEmpty(parentAppCase.getTaskZipPath()))
            {
                fileStorageOperate.deleteFile(parentAppCase.getTaskZipPath());
            }
            FolderUtils.deleteDirectory(new File(zipBasePath).getParent());
            throw new TestSystemException("执行失败:" + e.getMessage());
        }

        if (StringUtils.isNotEmpty(taskZipFile))
        {
            parentAppCase.setZipMd5Code(md5Code);
            parentAppCase.setTaskZipPath(taskZipFile);
            appCaseMapper.updateByPrimaryKeySelective(parentAppCase);
        }
        return null;
    }

    /**
     * 提供web侧使用：获取app用例相关的数据接口
     * @param caseIds
     * @return
     */
    @Override
    public List<Map<String, Object>> getAppCaseByCaseIds(List<Integer> caseIds) {
        List<Map<String, Object>> caseList = appCaseMapper.getAppCaseMapByCaseIds(caseIds);
        if (CollectionUtils.isNotEmpty(caseList))
        {
            return caseList;
        }
        else
        {
            return Collections.emptyList();
        }

    }

    /**
     * 提供给web侧使用： 获取指定用例的执行完成情况
     * @param appCaseId  副本用例ID
     * @return  true - 执行完成 false - 未完成
     */
    @Override
    public boolean getAppCaseFinishStatus(Integer appCaseId)
    {
        Map<String, Object> appExecuteMap = (Map<String, Object>) RedisUtil.getObject("appCaseId_" + appCaseId);
        if (appExecuteMap != null)
        {
            // 0-执行完成  1-执行中
            String finishStatus = appExecuteMap.get("finishStatus") == null ? "1" : appExecuteMap.get("finishStatus").toString();
            return "0".equals(finishStatus);
        }
        return false;
    }

    /**
     * 根据业务ID查询该业务下所有的APP用例
     * @param appCase
     * @return
     */
    @Override
    public List<AppCase> getAllByAppCase(AppCase appCase) {

        return appCaseMapper.getAllByAppCase(appCase);
    }

    /**
     * 下发用例后，Proxy给的响应信息
     * 根据响应信息更新用例相关的一些状态
     */
    @Override
    public void updateTestCase(CmdParamStartTask cmdParamStartTask, String proxyIpMac) {
        AppCase appCase = null;
        Integer appCaseId = cmdParamStartTask.getAppCaseId();
        if (appCaseId != null)
        {
            appCase = appCaseMapper.getAppCaseById(appCaseId);
        }
        if (appCase == null)
        {
            throw new TestSystemException("未找到对应的用例信息");
        }

        String[] proxy = proxyIpMac.split("_");

        List<Integer> devIds = deviceService.getDeviceIdByProxyAndCaseId(appCaseId, proxy[0], proxy[1]);
        AppCaseDevice cd = new AppCaseDevice();
        cd.setCaseId(appCaseId);
        cd.setIsPush(Constants.IS_PUSH_RECEIVE);
        cd.setPushed(1);
        cd.setDeviceIds(devIds);


        String resultCode = cmdParamStartTask.getResult().toString();

        LOGGER.info("Proxy已经接收到了消息: " + proxyIpMac + "。当前Proxy下连接的终端有：" + devIds.size() + "台。 resultCode = " + resultCode);

        if ("0".equals(resultCode))
        {
            // 成功下载zip包，并解析成开始执行用例
            cd.setExecuteStatus(Constants.EXECUTE_STATUS_DOING);
            cd.setExecuteTime(new Date());
            appCaseDeviceMapper.batchUpdateCaseDevice(cd);

            // 将Proxy关联的设备(预占用的设备)改为占用状态
            DeviceDTO dto = new DeviceDTO();
            dto.setDeviceList(devIds);
            dto.setUseStatus(Constants.USE_STATUS_OCCUPY.toString());
            dto.setPreOccupancy("2");
            deviceService.updateDeviceByDto(dto);
        }
        else
        {
            // 执行失败
            cd.setExecuteStatus(Constants.EXECUTE_STATUS_DONE);
            cd.setPassStatus(Constants.PASS_STATUS_FAILED);
            if ("1".equals(resultCode))
            {
                cd.setFailureReason("下载任务zip包异常");
            }
            else if ("2".equals(resultCode))
            {
                cd.setFailureReason("任务zip包解析异常");
            }
            else if ("3".equals(resultCode))
            {
                cd.setFailureReason("任务Zip包文件MD5校验异常");
            }
            else if ("4".equals(resultCode))
            {
                cd.setFailureReason("设备状态异常");
            }
            else if ("5".equals(resultCode))
            {
                cd.setFailureReason("其他异常");
            }
            appCaseDeviceMapper.batchUpdateCaseDevice(cd);

            List<String> deviceSnList = new ArrayList<>();
            for (Integer deviceId : devIds)
            {
                Device d = deviceService.selectByPrimaryKey(deviceId);
                if (d != null)
                {
                    deviceSnList.add(d.getDeviceSn());
                }
            }
            // 检查用例是否执行完成
            checkCaseIsFinish(appCase, deviceSnList);
        }
    }

    /**
     * 读取用例执行结果，并入库
     *
     * 1. 结果数据入库
     *      app_case_result: input_num、click_num、consume_time
     *      app_record_step
     * 2. 执行状态和结果更新
     *      app_case_device : pass_status(0-未通过；1-通过；2-未执行)、execute_status(0-未执行，1-执行中，2-执行完成)
     *      app_case : is_finish(0-执行完成，1-执行中，2:未执行)
     *      app_device: use_status(0-空闲；1-占用；2-预占用)
     *
     * @param testResultInfo
     */
    @Override
    public void readTestResult(TestResultInfo testResultInfo) {
        AppCase appCase = null;
        Integer appCaseId = testResultInfo.getAppCaseId();
        if (null != appCaseId)
        {
            // 副本用例
            appCase = appCaseMapper.getAppCaseById(appCaseId);
        }

        if (appCase == null)
        {
            LOGGER.error("未找到对应的用例信息");
            return;
        }
        // 根据deviceSn查询当前用例测试终端的信息
        AppCaseDevice cd = new AppCaseDevice();
        cd.setDeviceSn(testResultInfo.getDeviceSn());
        cd.setCaseId(testResultInfo.getAppCaseId());

        cd = appCaseDeviceService.getListByCondition(cd).get(0);

        AppCaseResult appCaseResult = new AppCaseResult();
        appCaseResult.setCaseId(appCaseId);
        appCaseResult.setOldCaseId(appCase.getOldCaseId());
        appCaseResult.setExecuteId(cd.getId());
        appCaseResult.setDeviceId(cd.getDeviceId());
        appCaseResult.setIsFinish(0);
        appCaseResult.setResultUrl(testResultInfo.getTestResultZip());
        appCaseResult.setDefectDesc(testResultInfo.getMsg());
        appCaseResult.setCreateTime(new Date());
        appCaseResult.setExecutePerson(appCase.getUpdatePerson());

        String resultStr = testResultInfo.getResult().trim().substring(0, testResultInfo.getResult().trim().length() - 1);
        List<String> resultList = Arrays.asList(resultStr.split(","));
        if (resultList.size() == 1)
        {
            // 用例的执行结果状态：0-通过，1-不通过。脚本执行后根据执行结果填入通过状态，用户在页面可修改本通过状态字段
            appCaseResult.setPassStatus(0);
            appCaseResult.setExecuteResult(0);
            // 设备执行脚本的通过状态：0-未通过；1-通过；2-未执行
            cd.setPassStatus(Constants.PASS_STATUS_SUCCESS);
        }
        else
        {
            // 不通过
            appCaseResult.setPassStatus(1);
            appCaseResult.setExecuteResult(1);
            cd.setPassStatus(Constants.PASS_STATUS_FAILED);
            cd.setFailureReason(testResultInfo.getMsg());
        }

        // 若是当前用例需要短信内容校验，则需等短信内容校验OK才算执行完成
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scriptId", appCase.getScriptId());
        paramMap.put("actionType", AppConstants.SCRIPT_STEP_SMSVARIFY);
        AppScriptStep smsVerifyStep = stepMapper.getSmsVerifyByMap(paramMap);
        if (smsVerifyStep == null || resultList.size() > 1)
        {
            cd.setExecuteStatus(Constants.EXECUTE_STATUS_DONE);
        }
        else
        {
            LOGGER.info("用例appCaseId = "+ appCaseId + "在deviceSn = " + testResultInfo.getDeviceSn() + "上还未执行完成，需等待短信校验。");
        }

        appCaseDeviceService.updateByPrimaryKeySelective(cd);

        if (CollectionUtils.isNotEmpty(testResultInfo.getScreenShotUrl()))
        {
            String urlStr = StringUtils.join(testResultInfo.getScreenShotUrl(), ",");
            appCaseResult.setScreenshotUrl(urlStr);
        }
        if (StringUtils.isNotEmpty(testResultInfo.getLogUrl()))
        {
            appCaseResult.setLogUrl(testResultInfo.getLogUrl());
        }
        if (StringUtils.isNotEmpty(testResultInfo.getVideoUrl()))
        {
            appCaseResult.setRecordVideoUrl(testResultInfo.getVideoUrl());
        }

        appCaseResultService.insertSelective(appCaseResult);

        // 脚本执行步骤详情
        List<StepDetails> stepDetailsList = testResultInfo.getStepDetails();
        if (CollectionUtils.isNotEmpty(stepDetailsList))
        {
            List<AppRecordStep> stepRecordList = new ArrayList<>();
            AppRecordStep smsVerifyRecordStep = null;

            int clickNum = 0;
            int inputNum = 0;
            double consumeTime = 0.0;
            // 所有步骤是否都执行成功
            boolean stepExecuteSuccess = true;

            for (StepDetails step : stepDetailsList)
            {
                if (step.getResult() != 1)
                {
                    stepExecuteSuccess = false;
                }
                if (AppConstants.SCRIPT_STEP_SMSVARIFY.equals(step.getActionType()))
                {
                    smsVerifyRecordStep = new AppRecordStep();
                    smsVerifyRecordStep.setCommand(AppConstants.SCRIPT_STEP_SMSVARIFY);
                    smsVerifyRecordStep.setBeginTime(new Date());
                    smsVerifyRecordStep.setEndTime(null);
                    smsVerifyRecordStep.setConsumeTime(null);
                    smsVerifyRecordStep.setResult(2);
                    smsVerifyRecordStep.setReturnInfo("等待短信验证");
                    smsVerifyRecordStep.setStepNo(step.getStepNo());
                    smsVerifyRecordStep.setStepDesc(step.getStepDesc());
                    smsVerifyRecordStep.setCaseResultId(appCaseResult.getResultId());
                    smsVerifyRecordStep.setScreenShotUrl(step.getScreenShotUrl());
                }
                else
                {
                    AppRecordStep recordStep = new AppRecordStep();
                    recordStep.setCommand(step.getActionType());
                    recordStep.setBeginTime(step.getBeginTime());
                    recordStep.setEndTime(step.getEndTime());
                    recordStep.setConsumeTime(step.getConsumeTime());
                    recordStep.setResult(step.getResult());
                    recordStep.setReturnInfo(step.getResultMsg());
                    recordStep.setStepNo(step.getStepNo());
                    recordStep.setStepDesc(step.getStepDesc());
                    recordStep.setCaseResultId(appCaseResult.getResultId());
                    recordStep.setScreenShotUrl(step.getScreenShotUrl());

                    stepRecordList.add(recordStep);
                }
                // 统计步骤点击次数、输入次数、总耗时
                consumeTime += step.getConsumeTime();
                if (AppConstants.SCRIPT_STEP_CLICK.equals(step.getActionType())
                        || AppConstants.SCRIPT_STEP_DOUBLECLICK.equals(step.getActionType())
                        || AppConstants.SCRIPT_STEP_TAP.equals(step.getActionType()))
                {
                    clickNum++;
                }
                else if (AppConstants.SCRIPT_STEP_SENDTEXT.equals(step.getActionType())
                        || AppConstants.SCRIPT_STEP_DYNAMIC.equals(step.getActionType()))
                {
                    inputNum++;
                }
            }
            appCaseResult.setClickNum(clickNum);
            appCaseResult.setInputNum(inputNum);
            appCaseResult.setConsumeTime(consumeTime);
            appCaseResultService.updateByPrimaryKeySelective(appCaseResult);

            appRecordStepService.insertList(stepRecordList);

            // 判断当前用例是否需要进行短信校验
            if (checkSmsVerify(resultList, smsVerifyStep, stepExecuteSuccess))
            {
                appRecordStepService.insertSelective(smsVerifyRecordStep);
                // 将该短信校验的步骤保存到Redis中
                Map<String, Object> appSmsVerifyMap = new HashMap<>();
                appSmsVerifyMap.put("appCaseId", appCaseId);
                appSmsVerifyMap.put("deviceSn", testResultInfo.getDeviceSn());
                appSmsVerifyMap.put("appSmsVerifyStep", smsVerifyStep);
                appSmsVerifyMap.put("appRecordStep", smsVerifyRecordStep);
                RedisUtil.setObject("appSmsVerifyMap_" + testResultInfo.getDeviceSn(), appSmsVerifyMap);

                LOGGER.error("短信校验相关信息已经保存到Redis中：appSmsVerifyMap = " + appSmsVerifyMap + ", deviceSn = " + appSmsVerifyMap.get("deviceSn"));
            }
            else
            {
                // 无需进行短信校验，则表示该当次执行完成
                cd.setExecuteStatus(Constants.EXECUTE_STATUS_DONE);
                if (!stepExecuteSuccess)
                {
                    cd.setPassStatus(0);
                }
                appCaseDeviceService.updateByCaseIdAndDeviceId(cd);
            }
        }
        else
        {
            // 结束当前执行的任务
            LOGGER.error("Proxy上报的结果中无脚本执行步骤详情信息：TestResultInfo = " + testResultInfo.toString());
        }

        // 检查用例是否执行完成
        List<String> devSnList = new ArrayList<>();
        if (StringUtils.isNotEmpty(testResultInfo.getDeviceSn()))
        {
            devSnList.add(testResultInfo.getDeviceSn());
        }
        checkCaseIsFinish(appCase, devSnList);
    }

    /**
     * 校验是否需要短信校验
     *
     * @return
     */
    private boolean checkSmsVerify(List<String> resultList, AppScriptStep smsVerifyStep, boolean stepExecuteSuccess)
    {

        if (smsVerifyStep == null || resultList.size() > 1 || !stepExecuteSuccess)
        {
            //无短信校验步骤，或测试结果失败，或执行步骤中有失败的步骤
            return false;
        }
        else
        {
            // 测试结果是成功的，并且所有执行步骤也是OK，并且有短信校验步骤
            return true;
        }
    }

    /**
     * 用例执行完成后，将当前用例的执行次数统计到redis中
     * @param appCaseId
     */
    @Override
    public void countAppCaseExecuteNumber(Integer appCaseId)
    {
        if (appCaseId != null)
        {
            List<Map<String, Integer>> list = (List<Map<String, Integer>>) RedisUtil.getObject("appCaseList");
            if (list == null)
            {
                list = new ArrayList<>();
            }

            //用例已经执行的次数
            if (appCaseId != null) {
                // 取出集合中当前用例的相关信息
                List<Map<String, Integer>> list1 = list.stream().filter(e -> appCaseId.equals(e.get("caseId"))).collect(Collectors.toList());
                // 取出集合中非当前用例的相关信息
                List<Map<String, Integer>> list2 = list.stream().filter(e -> !appCaseId.equals(e.get("caseId"))).collect(Collectors.toList());
                if (list1.size() > 0)
                {
                    Map<String,Integer> currentCaseMap = list1.get(0);
                    // 用例已执行次数加1
                    int number = currentCaseMap.get("number");
                    number++;
                    currentCaseMap.put("number", number);
                    list2.add(currentCaseMap);
                    list = list2;
                }
                else
                {
                    Map<String, Integer> map = new HashMap<>();
                    AppCase appCase = appCaseMapper.getAppCaseById(appCaseId);

                    Integer executeCount=appCase.getRetryCount();
                    List<AppCaseDevice> appCaseDeviceList = appCaseDeviceMapper.getListByCaseId(appCaseId);

                    map.put("caseId", appCaseId);
                    map.put("executeNumber", appCaseDeviceList.size());
                    map.put("number", 1);
                    map.put("type",1);
                    list.add(map);
                }
                RedisUtil.setObject("appCaseList",list);
            }
        }
    }

    /**
     * 根据用例ID查询该用例是否执行完成
     * 若执行完成，则更改状态及相关信息
     * @param appCase  副本用例
     */
    @Override
    public void checkCaseIsFinish(AppCase appCase, List<String> devSnList){

        boolean flag = true;

        if (CollectionUtils.isNotEmpty(devSnList))
        {
            // 设备空闲
            DeviceDTO dto = new DeviceDTO();
            dto.setDeviceSnList(devSnList);
            dto.setUseStatus("0");
            deviceService.updateDeviceByDto(dto);

            List<DeviceMessage> msgList = new ArrayList<>();
            for (String deviceSn : devSnList)
            {
                // 通知前端页面设备已经被释放
                DeviceMessage deviceMessage = new DeviceMessage();
                deviceMessage.setDeviceSn(deviceSn);
                deviceMessage.setType("release");
                deviceMessage.setUseStatus("0");
                msgList.add(deviceMessage);
            }
            DeviceListWebSocketServer.sendMsgListToPage(msgList);
        }

        String keyCase = "appCaseId_" + appCase.getCaseId();
        Map<String, Object> redisMap = (Map<String, Object>) RedisUtil.getObject(keyCase);
        if (redisMap != null)
        {
            Integer retryCount = appCase.getRetryCount();
            Integer realExecuteNum = appCase.getRealExecuteNum();

            LOGGER.info("保存在Redis中的用例执行情况：" + JSON.toJSONString(redisMap));

            Integer executeNumber = (Integer) redisMap.get("executeNumber");
            Integer number = (Integer) redisMap.get("number") + 1;
            if (executeNumber.equals(number))
            {
                // 用例执行完成
                redisMap.put("finishStatus", 0);
                redisMap.put("number", number);

                AppCase c = new AppCase();

                c.setCaseId(appCase.getCaseId());
                // 判断当前副本用例是否执行完成 (执行次数)
                if (retryCount.equals(realExecuteNum))
                {
                    // 表示所有次数都执行完成，原用例执行状态可以设置成执行完成
                    c.setOldCaseId(appCase.getOldCaseId());
                }
                else
                {
                    // 表示次数还未执行完成，原用例执行状态还不能设置成执行完成
                    c.setOldCaseId(null);
                }

                // 该用例已经执行结束,统计该用例的执行成功数和失败数
                Map<String, Object> map = appCaseDeviceService.getCountByCaseId(appCase.getCaseId());
                String failureCount = map.get("failureCount").toString();
                String successCount = map.get("successCount").toString();
                if (StringUtils.isNotEmptyAll(failureCount, successCount))
                {
                    c.setFailureCount(Integer.parseInt(failureCount));
                    c.setSuccessCount(Integer.parseInt(successCount));
                    String sumCount = map.get("sumCount").toString();
                    if (!"0".equals(sumCount))
                    {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String successRate = df.format(Double.parseDouble(successCount) / Integer.parseInt(sumCount));
                        c.setSuccessRate(successRate);
                    }
                }
                c.setIsFinish(0);
                // 不能修改execute_count字段
                c.setExecuteCount(null);
                appCaseMapper.updateByPrimaryKeySelective(c);

                // 用例执行完成，需要更改业务的成功和失败数
                // 参数：businessId
                projectMenuService.exeAfterUpdateData(appCase.getBusinessId());

                // 执行次数 = 实际执行次数 相等时表示用例执行完成
                if (retryCount > realExecuteNum)
                {
                    ExecuteCaseBO caseBO = new ExecuteCaseBO();

                    caseBO.setCaseId(appCase.getOldCaseId());
                    caseBO.setRealExecuteNum(realExecuteNum + 1);
                    if (appCase.getSysTaskId() == null && appCase.getSurveyTaskId() == null)
                    {
                        // 单个用例执行
                        caseBO.setType(AppConstants.EXECUTE_TYPE_SINGLECASE);
                    }
                    else
                    {
                        if (appCase.getSurveyTaskId() != null)
                        {
                            caseBO.setType(AppConstants.EXECUTE_TYPE_SURVEYTASK);
                            caseBO.setSurveyTaskId(appCase.getSurveyTaskId());
                        }
                        else if (appCase.getSysTaskId() != null)
                        {
                            caseBO.setType(AppConstants.EXECUTE_TYPE_BATCHTASK);
                            caseBO.setSysTaskId(appCase.getSysTaskId());
                        }
                    }

                    taskService.commonExecute(caseBO);

                    flag = false;
                }
            }
            else
            {
                // 用例执行未完成
                redisMap.put("number", number);
                LOGGER.info("该用例还未执行完成：" + JSON.toJSONString(redisMap));
            }
            RedisUtil.setObject(keyCase, redisMap);
        }
        else
        {
            LOGGER.info("从Redis中未获取到对应的执行状态：redisKey = " + keyCase);
        }

        if (flag && CollectionUtils.isNotEmpty(devSnList))
        {
            for (String deviceSn : devSnList)
            {
                // 当前手机执行完用例已经空闲， 检查调度是否有其他要执行的用例
                taskService.schedulingTask(deviceSn);
            }
        }

        // 统计用例执行次数
        countAppCaseExecuteNumber(appCase.getCaseId());

    }

    /**
     * 获取系统中所有APP执行中的任务
     * @return
     */
    @Override
    public PageBean<AppCase> getAllRunningAppCase(PageBean<AppCase> pageBean, CaseExcResultVO resultVO)
    {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<AppCase> appCaseList = appCaseDeviceMapper.getAllRunningAppCase(resultVO);
        pageBean.setItems(appCaseList);
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;
    }

    /**
     * 统计指定业务下的所有用例的执行的成功数和失败数
     * @param businessId
     * @return
     */
    @Override
    public Map<String, Long> appCaseCountNumber(Integer businessId) {

        return appCaseMapper.appCaseCountNumber(businessId);
    }

    /**
     * 用例分页: 统计执行的成功数据和失败数
     * @param name
     * @param businessId
     * @return
     */
    @Override
    public List<PbtVO> findByPage(String name, Integer businessId) {
        return appCaseMapper.findByPage(name, businessId);
    }

    /**
     * 单独手动执行单个用例
     * @param appCaseId
     * @return
     */
    @Override
    public Map<String, String> handExecuteCase(Integer appCaseId) {
        Integer updatePerson = ShiroKit.getUser().getId();
        Map<String, String> resultMap = new HashMap<>();
        try {
            AppCase appCase = appCaseMapper.getAppCaseById(appCaseId);
            if (appCase == null)
            {
                throw new RuntimeException("未找到执行的用例");
            }
            // 1-定时执行；2-周期执行
            if ("1".equals(appCase.getExecuteType()) || "2".equals(appCase.getExecuteType()))
            {
                throw new RuntimeException("定时任务不能手动执行");
            }
            // 只有未执行或者执行完成的用例才能够执行
            if ("1".equals(appCase.getIsFinish()))
            {
                throw new RuntimeException("当前用例正在执行中...");
            }

            ExecuteCaseBO caseBO = new ExecuteCaseBO();
            caseBO.setExecutorId(updatePerson);
            caseBO.setCaseId(appCaseId);
            caseBO.setType(AppConstants.EXECUTE_TYPE_SINGLECASE);
            caseBO.setRealExecuteNum(1);
            taskService.commonExecute(caseBO);

            resultMap.put("successMsg", "执行成功");
            return resultMap;
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("errorMsg", e.getMessage());
            return resultMap;
        }
    }

    @Override
    public Map<String, Integer> caseScriptCount() {
        Map<String,Integer> resultMap = new HashMap<>();
        Integer ceseBugCount=appCaseResultMapper.queryCaseBugCount();
        Integer appScriptCount = appScriptMapper.queryAppScriptCount();
        Integer executeCount = appCaseDeviceMapper.queryExcuteNum();

        resultMap.put("ceseBugCount",ceseBugCount);   //缺陷数
        resultMap.put("appScriptCount",appScriptCount); //脚本数量
         //执行次数
        if (executeCount != null) {
            resultMap.put("executeCount",executeCount);
        } else {
            resultMap.put("executeCount",0);
        }
        return resultMap;
    }

    @Override
    public List<AppCase> listAllTimingCase() {
        return appCaseMapper.listAllTimingCase();
    }

    @Override
    public List<TestCase> getListByAppCase(TestCase testCase) {
        return appCaseMapper.getListByAppCase(testCase);
    }

    /**
     *复制用例
     * @param list
     * @param menuParentId
     * @return
     */

    @Override
    public boolean copy(List<SysPermission> list, int menuParentId) {
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        int result = 0;
        for (SysPermission sysPermission : list) {
            AppCase appCase = this.selectByPrimaryKey(sysPermission.getDataId());
          List<AppCaseDevice> appCaseDevice=appCaseDeviceMapper.selectByCaseId(sysPermission.getDataId());
            AppScript appScript = appScriptService.getScriptByScriptId(appCase.getScriptId());
//            int oldId= appScript.getScriptId();
            appCase.setCaseId(null);
            appCase.setIsMerge(0);
            appCase.setCaseName(sysPermission.getName());
            //String serialNumber = appCase.getCaseNum().substring(0, appCase.getCaseNum().lastIndexOf("_") + 1);
//            appCase.setCaseNum(serialNumber + UUID.randomUUID().toString().replaceAll("-", ""));
            appCase.setCaseNum(null);
            appCase.setBusinessId(parentSysPermission.getDataId());
            appCase.setTaskZipPath(null);
            appCase.setZipMd5Code(null);
            appCase.setScriptId(null);
            appCase.setExecuteType(3);
            appCase.setIsRecord(0);
            appCaseMapper.insert(appCase);

//            for (AppCaseDevice newCaseDevice:appCaseDevice){
//                newCaseDevice.setId(null);
//                newCaseDevice.setCaseId(appCase.getCaseId());
//                appCaseDeviceMapper.insert(newCaseDevice);
//
//            }
            sysPermission.setId(null);
            sysPermission.setDataId(appCase.getCaseId());
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
            AppCase appCase = appCaseMapper.selectByPrimaryKey(sysPermission.getDataId());
            appCase.setBusinessId(parentSysPermission.getDataId());
            appCase.setOrderCode(sysPermission.getCode());
            appCase.setScriptId(null);
            caseResult += appCaseMapper.updateByAppCaseId(appCase);
        }

        return sysResult > 0 && caseResult > 0;
    }

    @Override
    public List<Long> getExcuteNumById(Integer caseId) {
        return appCaseMapper.getExcuteNumById(caseId);
    }

    /**
     * 同步数据到众测平台
     * @param sysSurveyTask
     * @return
     */
    @Override
    public List<Map<String, Object>> syncSysSurveyTaskData(SysSurveyTask sysSurveyTask, Map<String, List<Integer>> caseIdsMap)
    {

        List<Map<String, Object>> caseList = new ArrayList<>();
        Map<String, String> proMap = null;
        Map<String, String> proMap2 = null;

        List<Integer> appCaseIdList = (List<Integer>) caseIdsMap.get("app");
        List<SurveyTaskReportVO> surveyReportList_0 = this.getAppCaseReportList(sysSurveyTask.getId(), null, 0);
        if (CollectionUtils.isEmpty(surveyReportList_0))
        {
            //无用例报告生成
            return Collections.emptyList();
        }
        List<SurveyTaskReportVO> surveyReportList_1 = this.getAppCaseReportList(sysSurveyTask.getId(), null, 1);

        String jsonStr = sysSurveyTask.getCaseIds();
        JSONObject jsonObject = JsonUtil.parseJsonObject(jsonStr);
        List<Integer> caseIds = (List<Integer>) jsonObject.get("app");

        for (Integer caseId : caseIds)
        {
            if (CollectionUtils.isNotEmpty(appCaseIdList) && !appCaseIdList.contains(caseId))
            {
                continue;
            }
            AppCase appCase = appCaseMapper.getAppCaseById(caseId);

            List<String> provinceList = new ArrayList<>();
            List<String> provinceList1 = new ArrayList<>();

            // 获取当前用例预计执行的对应手机设备
            List<AppCaseDevice> deviceList = appCaseDeviceMapper.getBySysSurveyIdAndCaseId(sysSurveyTask.getId(), caseId);
            if (CollectionUtils.isEmpty(deviceList))
            {
                continue;
            }
            if (CollectionUtils.isNotEmpty(surveyReportList_1))
            {
                Map<String, Object> caseMap = new HashMap<>();
                List<Map<String, String>> proList = new ArrayList<>();

                for (SurveyTaskReportVO vo : surveyReportList_0)
                {
                    if(vo.getTestcaseId().equals(caseId))
                    {
                        proMap2 = this.getProMap(vo,sysSurveyTask, "1", "全国平均");
                        proList.add(proMap2);
                    }
                }

                for (SurveyTaskReportVO vo : surveyReportList_1)
                {
                    if(vo.getTestcaseId().equals(caseId)){
                        proMap = this.getProMap(vo, sysSurveyTask, "2", "" + vo.getProvince());
                        proList.add(proMap);
                        //添加省列表
                        provinceList1.add(vo.getProvince());
                    }
                }
                caseMap.put("CASE_NAME", "" + appCase.getCaseName());
                caseMap.put("PRO_LIST", proList);
                caseList.add(caseMap);
            }
        }
        return caseList;
    }

    /**
     * 众测任务 同步数据
     * @param surveyTaskId 众测任务ID
     * @param province     省份
     * @param type
     * @return
     */
    @Override
    public List<SurveyTaskReportVO> getAppCaseReportList(Integer surveyTaskId, String province, Integer type)
    {
        SysSurveyTask sysSurveyTask = sysSurveyTaskMapper.selectByPrimaryKey(surveyTaskId);
        if (null == sysSurveyTask)
        {
            return Collections.emptyList();
        }
        String jsonStr = sysSurveyTask.getCaseIds();
        JSONObject jsonObject = JsonUtil.parseJsonObject(jsonStr);
        List<Integer> appCaseIdList = (List<Integer>) jsonObject.get("app");

        if (CollectionUtils.isEmpty(appCaseIdList))
        {
            return Collections.emptyList();
        }
        if (type.equals(0))
        {
            return appCaseMapper.getCaseReportList(province, surveyTaskId, appCaseIdList);
        }
        else if (type.equals(1))
        {
            return appCaseMapper.getCaseProvinceReportList(appCaseIdList, surveyTaskId);
        }
        return Collections.emptyList();

    }

    public Map<String ,String> getProMap(SurveyTaskReportVO surveyTaskReportVO,SysSurveyTask sysSurveyTask,String proType,String proName)
    {
        Map<String ,String> proMap = new HashMap<>();

        if("1".equals(sysSurveyTask.getTgAccuracy()))
        {
            // 业务正确率 即业务正确率指标得分
            proMap.put("TG_ACCURACY",""+surveyTaskReportVO.getSuccessRote());
            proMap.put("SC_ACCURACY",""+surveyTaskReportVO.getSuccessRote());
        }
        if("1".equals(sysSurveyTask.getTgClicksAvg()))
        {
            proMap.put("TG_CLICKS_AVG",""+surveyTaskReportVO.getAvgClickNum());
            proMap.put("SC_CLICKS_AVG",""+surveyTaskReportVO.getClickScore());
        }
        if("1".equals(sysSurveyTask.getTgInputAvg()))
        {
            proMap.put("TG_INPUT_AVG",""+surveyTaskReportVO.getAvgInputNum());
            proMap.put("SC_INPUT_AVG",""+surveyTaskReportVO.getInputScore());
        }
        if("1".equals(sysSurveyTask.getTgNoteAvg()))
        {
            proMap.put("TG_NOTE_AVG",""+surveyTaskReportVO.getAvgMsgConsumeTime());
            proMap.put("SC_NOTE_AVG",""+surveyTaskReportVO.getMsgConsumeTimeScore());
        }
        if("1".equals(sysSurveyTask.getTgStepAvg()))
        {
            proMap.put("TG_STEP_AVG",""+surveyTaskReportVO.getAvgConsumeTime());
            proMap.put("SC_STEP_AVG",surveyTaskReportVO.getConsumeTimeScore());
        }

        proMap.put("PRO_TYPE",proType);
        proMap.put("PRO_NAME",""+proName);

        return proMap;
    }

    @Override
    public List<DownloadFileDto> downloadCompressPicZip(CommonResultVO caseVO) {

        List<DownloadFileDto> downloadFileDtoList = new ArrayList<>();
        List<CommonResultVO> list = appCaseResultMapper.getDownloadZipFileInfo(caseVO);

        Map<String, Integer> countMap = new HashMap<>();
        // 用于保存所有截图文件的名称
        List<String> fileNameList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list))
        {
            // 获取FastDfs文件服务器上的截图文件
            for (CommonResultVO resultVo : list)
            {
                //  /项目名/第n轮/业务名/省份/
                StringBuffer jpgBasePath = new StringBuffer();
                jpgBasePath.append(resultVo.getProjectName() + File.separator);
                jpgBasePath.append("第" + resultVo.getExecuteNum() + "轮" + File.separator);
                jpgBasePath.append(resultVo.getBusinessName() +File.separator + resultVo.getProvince() + File.separator);

                List<AppRecordStep> recordStepList = resultVo.getRecordStepList();
                if (CollectionUtils.isNotEmpty(recordStepList))
                {
                    for (AppRecordStep step : recordStepList)
                    {
                        if (StringUtils.isNotEmpty(step.getScreenShotUrl()))
                        {
                            DownloadFileDto downloadFileDto = new DownloadFileDto();
                            //  TS00000001-广东-1-不通过.jpg
                            StringBuffer jpgName = new StringBuffer();
                            jpgName.append(resultVo.getSerialNumber() + "-" + resultVo.getProvince() + "-" + step.getStepNo());
                            if (!step.getResult().equals(1))
                            {
                                jpgName.append("-不通过");
                            }
                            String fileName = jpgBasePath.toString() + jpgName + ".jpg";
                            // 针对于一省有多个报告的情况，截图文件需要使用序号区分
                            if (fileNameList.contains(fileName))
                            {
                                countMap = ToolUtil.listCount(countMap, fileNameList.toArray());
                                fileName.replace(".jpg", "("+countMap.get(fileName)+").jpg");
                            }

                            byte[] byteFile = fileStorageOperate.downloadFile(step.getScreenShotUrl());
                            downloadFileDto.setFileName(fileName);
                            if (byteFile != null)
                            {
                                downloadFileDto.setByteDataArr(byteFile);
                            }
                            else
                            {
                                downloadFileDto.setByteDataArr(new byte[1]);
                                LOGGER.info(fileName + ": 图片丢失");
                            }
                            downloadFileDtoList.add(downloadFileDto);
                            fileNameList.add(fileName);
                        }
                    }
                }
            }
        }
        return downloadFileDtoList;
    }

    @Override
    public int batchUpdateAppCase(List<AppCase> caseList) {
        return appCaseMapper.batchUpdateAppCase(caseList);
    }


    @Override
    public Integer getExecutingCountByBusiness(Integer businessId) {
        Map<String, Long> map = appCaseMapper.getExecuteStatusCountByBusiness(businessId);
        Long executingNum = 0L;
        if (map != null)
        {
            executingNum = map.get("doingNum");
        }
        return executingNum.intValue();
    }

    @Override
    public List<AppCase> getTestCaseByIds(List<Integer> ids) {
        return appCaseMapper.getTestCaseByIds(ids);
    }
}
