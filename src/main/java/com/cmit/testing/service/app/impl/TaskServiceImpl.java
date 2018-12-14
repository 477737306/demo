package com.cmit.testing.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.dao.app.*;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.DeviceParam;
import com.cmit.testing.entity.proxy.TaskBody;
import com.cmit.testing.entity.vo.AppScriptFileVO;
import com.cmit.testing.entity.vo.AppScriptVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.listener.msg.DeviceMessage;
import com.cmit.testing.listener.proxy.netty.NettyChannelMap;
import com.cmit.testing.listener.proxy.netty.NettyServerHandler;
import com.cmit.testing.listener.websocket.DeviceListWebSocketServer;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.ProjectMenuService;
import com.cmit.testing.service.app.*;
import com.cmit.testing.utils.*;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.app.DeviceConstant;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.utils.file.ZipUtils;
import io.netty.channel.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * @author XieZuLiang
 * @description 执行用例
 * @date 2018/9/12 0012.
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private AppProxyService appProxyService;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;
    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AppScriptService appScriptService;
    @Autowired
    private AppCaseMapper appCaseMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private AppParamMapper paramMapper;
    @Autowired
    private AppCaseDeviceMapper appCaseDeviceMapper;
    @Autowired
    private AppCaseResultMapper appCaseResultMapper;
    @Autowired
    private AppRecordStepMapper appRecordStepMapper;
    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;
    @Autowired
    private ProjectMenuService projectMenuService;
    @Autowired
    private ProvideWebService provideWebService;

    /**
     * 生成副本用例
     * @param appCase
     * @return
     */
    @Override
    public AppCase createCaseExecuteBefore(AppCase appCase, Integer realExecuteNum) {

        // 开始执行用例之前，需要更新下项目和业务的执行状态
        projectMenuService.exeBeforeUpdateData(appCase.getBusinessId());

        Integer oldCaseId = appCase.getCaseId();
        Integer maxExecuteCount = appCaseMapper.getMaxExecuteCount(oldCaseId);
        maxExecuteCount = maxExecuteCount == null ? 0 : maxExecuteCount;
        appCase.setOldCaseId(oldCaseId);
        appCase.setExecuteCount(maxExecuteCount);
        appCase.setRetryCount(appCase.getRetryCount());
        // 第几次执行
        appCase.setRealExecuteNum(realExecuteNum);
        appCase.setUpdateTime(new Date());
        appCase.setIsFinish(2);
        appCase.setSuccessCount(0);
        appCase.setFailureCount(0);
        appCase.setSuccessRate("0.0");
        appCase.setCaseId(null);
        appCase.setUpdateTime(new Date());
        appCase.setCreateTime(new Date());
        // 重新保存一份新的用例副本
        appCaseMapper.insertSelective(appCase);

        // 原用例 修改其执行状态
        AppCase c = new AppCase();
        c.setIsFinish(1);
        c.setCaseId(oldCaseId);
        appCaseMapper.updateByPrimaryKeySelective(c);

        // 将用例执行的情况保存到Redis中
        Map<String, Object> map = new HashMap<>();
        map.put("caseId", appCase.getCaseId());
        map.put("oldCaseId", oldCaseId);
        map.put("finishStatus", 1);
        map.put("executeNumber", appCase.getDeviceNum());
        map.put("number", 0);
        String mapKey = "appCaseId_" + appCase.getCaseId();
        RedisUtil.setObject(mapKey, map);

        return appCase;
    }

    /**
     * 执行用例的共用接口
     *  执行批量任务，首先要设置批量任务ID(sysTaskId)
     *  执行众测任务，首先要设置众测任务ID(surveyTaskId)
     * @param caseBO
     */
    @Override
    public List<Integer> commonExecute(ExecuteCaseBO caseBO)
    {
        List<Integer> copyCaseIds = new ArrayList<>();

        // 批量执行、众测任务执行
        if (AppConstants.EXECUTE_TYPE_BATCHTASK.equals(caseBO.getType()) || AppConstants.EXECUTE_TYPE_SURVEYTASK.equals(caseBO.getType()))
        {
            if (CollectionUtils.isNotEmpty(caseBO.getCaseIds()))
            {
                for (Integer oldCaseId : caseBO.getCaseIds())
                {
                    AppCase appCase = appCaseMapper.getAppCaseById(oldCaseId);
                    if (null == appCase)
                    {
                        throw new TestSystemException("任务关联的用例不存在，无法执行。appCaseId = " + oldCaseId);
                    }
                    // 执行前校验用例是否可以执行
                    List<AppCaseDevice> caseDeviceList = getExecuteDeviceList(appCase.getFollowId(), oldCaseId);
                    if (CollectionUtils.isEmpty(caseDeviceList))
                    {
                        throw new TestSystemException("用例未设置参数和依赖，或依赖用例无执行通过的号码无法执行。appCaseId = " + oldCaseId);
                    }

                    // 众测任务 清除数据
                    if (AppConstants.EXECUTE_TYPE_SURVEYTASK.equals(caseBO.getType()))
                    {
                        /**
                         * 判断该众测任务是否已经执行过
                         * 1.已执行过，则要删除执行的相关结果
                         * 2.未执行过，则新建一个用例副本
                         */
                        List<AppCase> copyAppCaseList = appCaseMapper.getAppCaseBySurveyTaskIdAndCaseId(caseBO.getSurveyTaskId(), oldCaseId);
                        if (CollectionUtils.isNotEmpty(copyAppCaseList))
                        {
                            // 删除众测任务关联的副本记录 及执行结果  app_case_device、app_case_result、app_record_step
                            for (AppCase c : copyAppCaseList)
                            {
                                List<Integer> list = new ArrayList<>();
                                list.add(c.getCaseId());
                                List<Integer> idsList = appCaseResultMapper.getResultIdsByCaseIds(list);
                                if (CollectionUtils.isNotEmpty(idsList))
                                {
                                    appRecordStepMapper.deleteByCaseResultIds(idsList);
                                    appCaseResultMapper.deleteResultByIds(idsList);
                                }
                                appCaseDeviceMapper.deleteByCaseId(c.getCaseId());
                                appCaseMapper.deleteByPrimaryKey(c.getCaseId());
                            }
                        }

                        appCase.setSurveyTaskId(caseBO.getSurveyTaskId());
                    }
                    else
                    {
                        appCase.setSysTaskId(caseBO.getSysTaskId());
                    }

                    // 创建副本用例
                    appCase.setDeviceNum(caseDeviceList.size());
                    if (null != caseBO.getExecutorId())
                    {
                        appCase.setUpdatePerson(caseBO.getExecutorId());
                    }
                    AppCase c = createCaseExecuteBefore(appCase, caseBO.getRealExecuteNum());
                    copyCaseIds.add(c.getCaseId());
                    executeMethod(c, caseDeviceList);
                }
            }
        }
        // 单独一个用例执行
        else {
            AppCase appCase = appCaseMapper.getAppCaseById(caseBO.getCaseId());
            if (null == appCase) {
                throw new TestSystemException("用例不存在，无法执行。appCaseId = " + caseBO.getCaseId());
            }
            List<AppCaseDevice> caseDeviceList = getExecuteDeviceList(appCase.getFollowId(), caseBO.getCaseId());
            if (CollectionUtils.isEmpty(caseDeviceList)) {
                throw new TestSystemException("用例未设置参数和依赖，或依赖用例无执行通过的号码无法执行。appCaseId = " + caseBO.getCaseId());
            }
            appCase.setSurveyTaskId(null);
            appCase.setSysTaskId(null);
            if (null != caseBO.getExecutorId())
            {
                appCase.setUpdatePerson(caseBO.getExecutorId());
            }
            // 创建副本用例
            appCase.setDeviceNum(caseDeviceList.size());
            AppCase c = createCaseExecuteBefore(appCase, caseBO.getRealExecuteNum());
            copyCaseIds.add(c.getCaseId());
            executeMethod(c, caseDeviceList);
        }

        return copyCaseIds;
    }

    /**
     * 执行副本用例
     * @param appCase 副本用例
     * @return
     */
    @Override
    public void executeMethod(AppCase appCase, List<AppCaseDevice> deviceList)
    {
        for (AppCaseDevice cd : deviceList)
        {
            cd.setCaseId(appCase.getCaseId());
            Map<String, Integer> maxNumMap = appCaseMapper.getMaxBatchNumber(appCase.getOldCaseId(), cd.getProvince());
            Integer maxNum = maxNumMap == null ? 1 : maxNumMap.get("maxNum") + 1;
            cd.setExecuteNum(maxNum);
            cd.setId(null);
            cd.setTotalSteps(null);
            cd.setCurrentStep(null);
            cd.setParamType("1");
            cd.setPassStatus(2);
            cd.setFailureReason(null);
            cd.setExecuteStatus(0);
            cd.setIsPush(0);
            cd.setExecuteTime(null);
            appCaseDeviceMapper.insertSelective(cd);
        }

        List<AppCaseDevice> resultList = new ArrayList<>();
        // 过滤掉一些不在线和被占用的设备
        for (AppCaseDevice caseDevice: deviceList)
        {
            // 在线、空闲
            if ("0".equals(caseDevice.getUseStatus()) && "1".equals(caseDevice.getOnlineStatus()))
            {
                resultList.add(caseDevice);
            }
        }
        if (CollectionUtils.isEmpty(resultList))
        {
            LOGGER.info("当前无在线空闲的设备无法立即执行，已将其排入到队列中等待执行");
            return;
        }

        // 下发用例执行
        CmdParamJson cmdParamJson = new CmdParamJson();
        cmdParamJson.setAppCaseId(appCase.getCaseId());
        cmdParamJson.setTaskZipPath(appCase.getTaskZipPath());
        cmdParamJson.setMd5Code(appCase.getZipMd5Code());
        executeSingleCase(cmdParamJson, deviceList);
    }

    @Override
    public void executeSingleCase(CmdParamJson cmdParam, List<AppCaseDevice> deviceList)
    {

        Set<Integer> proxyIds = new HashSet<Integer>();
        for (AppCaseDevice cd : deviceList)
        {
            proxyIds.add(cd.getProxyId());
        }
        int proxyNum = proxyIds.size();
        int miss = 0;
        // 根据Proxy分别下发消息
        for (Integer proxyId : proxyIds)
        {
            List<String> devSnList = new ArrayList<>();
            for (AppCaseDevice cd : deviceList)
            {
                if (proxyId.equals(cd.getProxyId()))
                {
                    devSnList.add(cd.getDeviceSn());
                }
            }
            // 下发消息
            if (devSnList.size() != 0)
            {
                Integer appId = appCaseMapper.getAppIdByCaseId(cmdParam.getAppCaseId());
                List<DeviceParam> paramList = paramMapper.getAppParamByMap(appId, devSnList);
                cmdParam.setDeviceList(paramList);

                miss = sendMsgToProxy(cmdParam, proxyId, miss);
            }
        }

        if (proxyNum == miss)
        {
            throw new TestSystemException("终端手机异常，请稍后再执行");
        }
    }

    @Override
    public int sendMsgToProxy(CmdParamJson cmdParam, Integer proxyId, Integer miss) {

        AppProxy proxy = appProxyService.selectByPrimaryKey(proxyId);
        String proxyIpMac = proxy.getProxyIp() + "_" + proxy.getProxyMac();
        Channel channel = NettyChannelMap.getChannel(proxyIpMac);
        if (channel == null)
        {
            // Proxy终端异常
            miss++;
            return miss;
        }

        RootJson root = new RootJson();
        root.setACC_TYPE(AppConstants.CMD_ACC_TYPE);
        root.setCMD_TYPE(AppConstants.CMD_TYPE_STARTTASK);
        root.setSEQ_NO(DateUtil.stamp(new Date()) + cmdParam.getAppCaseId());
        root.setCmdParamJson(cmdParam);
        String jsonStr = JSON.toJSONString(root);
        LOGGER.info("Server send msg to proxy : " + jsonStr);
        NettyServerHandler.sendMsgToProxy(channel, proxyIpMac, jsonStr);
        LOGGER.info("用例下发成功!");

        List<AppCaseDevice> list = new ArrayList<>();
        for (DeviceParam  dp : cmdParam.getDeviceList())
        {
            AppCaseDevice caseDevice = new AppCaseDevice();
            caseDevice.setCaseId(cmdParam.getAppCaseId());
            caseDevice.setIsPush(1);
            caseDevice.setDeviceSn(dp.getDeviceSn());
            list.add(caseDevice);

            Device device = new Device();
            // 使用状态: 0-空闲；1-占用；2-预占用
            device.setUseStatus("2");
            device.setDeviceSn(dp.getDeviceSn());
            deviceService.updateDeviceByDeviceSn(device);
        }
        appCaseDeviceService.updateStatusByDeviceSnAndCaseId(list);

        return miss;
    }

    /**
     * 任务调度，当前手机执行完用例之后，需要检查库中是否还有其他用例执行
     * 任务优先级：先进先出
     */
    @Override
    public void schedulingTask(String deviceSn) {
        List<AppCase> caseList = appCaseDeviceService.getCaseIdByDeviceSn(deviceSn);
        if (CollectionUtils.isNotEmpty(caseList))
        {
            // 表示当前设备还有用例要执行，故再次下发用例给当前设备执行
            AppCase copyAppCase = caseList.get(0);

            CmdParamJson cmdParam = new CmdParamJson();
            cmdParam.setTaskZipPath(copyAppCase.getTaskZipPath());
            cmdParam.setMd5Code(copyAppCase.getZipMd5Code());
            cmdParam.setAppCaseId(copyAppCase.getCaseId());

            List<String> deviceSnList = new ArrayList<>();
            deviceSnList.add(deviceSn);
            Integer appId = appCaseMapper.getAppIdByCaseId(cmdParam.getAppCaseId());
            List<DeviceParam> paramList = paramMapper.getAppParamByMap(appId, deviceSnList);
            cmdParam.setDeviceList(paramList);

            int miss = 0;

            Device d = deviceService.getDeviceByDeviceSn(deviceSn);

            miss = sendMsgToProxy(cmdParam, d.getProxyId(), miss);

            if (miss == 1)
            {
                LOGGER.error("终端手机状态异常，请稍后再执行用例！ deviceSn = " + deviceSn);
                return;
            }
        }
        else
        {
            // 表示当前设备没有要执行的用例，直接将设备改成空闲即可
            Device d = new Device();
            d.setDeviceSn(deviceSn);
            d.setUseStatus(Constants.USE_STATUS_FREE.toString());
            deviceService.updateDeviceByDeviceSn(d);

            // 通知前端页面设备已经被释放
            DeviceMessage deviceMessage = new DeviceMessage();
            deviceMessage.setDeviceSn(deviceSn);
            deviceMessage.setType("release");
            deviceMessage.setUseStatus("0");
            DeviceListWebSocketServer.sendMsgToPage(deviceMessage);
        }
    }

    /**
     * 根据依赖ID获取到对应能执行的手机设备
     *
     * 所有依赖用例执行成功的手机号码中取交集来执行当前用例
     * @param followId
     * @return
     */
    private List<AppCaseDevice> getExecuteDeviceList(String followId, Integer oldCaseId)
    {
        List<AppCaseDevice> deviceList = new ArrayList<>();

        // 先判断原用例是否设置了参数
        deviceList = appCaseDeviceMapper.getListByCaseId(oldCaseId);
        if (CollectionUtils.isEmpty(deviceList))
        {
            // 未设置参数， 检查是否设置了依赖
            List<String> phoneList = provideWebService.getPhoneNumByFollow(followId);
            if (CollectionUtils.isEmpty(phoneList))
            {
                return Collections.emptyList();
            }

            // 在线、不在线、被占用的都获取到
            List<Device> list = deviceMapper.getDeviceByPhoneNum(phoneList);
            for (Device d : list)
            {
                AppCaseDevice cd = new AppCaseDevice();
                cd.setTelNum(d.getTelNum());
                cd.setDeviceId(d.getId());
                cd.setDeviceSn(d.getDeviceSn());
                cd.setProvince(d.getProvince());

                cd.setOnlineStatus(d.getOnlineStatus());
                cd.setUseStatus(d.getUseStatus());
                cd.setProxyId(d.getProxyId());

                deviceList.add(cd);
            }
        }
        return deviceList;
    }

}
