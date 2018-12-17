package com.cmit.testing.listener.timer;

import com.cmit.testing.dao.SendMessageTaskMapper;
import com.cmit.testing.entity.SendMessage;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.ImageData;
import com.cmit.testing.entity.vo.AppExecutingTaskVO;
import com.cmit.testing.listener.TaskScheduleService;
import com.cmit.testing.listener.msg.DeviceMessage;
import com.cmit.testing.listener.proxy.websocket.DeviceWebSocketServer;
import com.cmit.testing.listener.websocket.DeviceListWebSocketServer;
import com.cmit.testing.service.app.*;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.app.DeviceConstant;
import com.cmit.testing.utils.file.FolderUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author XieZuLiang
 * @description 定时调度任务
 * @date 2018/10/16 0016.
 */
@Service("taskScheduleService")
public class TaskScheduleServiceImpl implements TaskScheduleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduleServiceImpl.class);
    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;
    @Autowired
    private AppRecordStepService appRecordStepService;
    @Autowired
    private AppCaseResultService appCaseResultService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SendMessageTaskMapper sendMessageTaskMapper;
    @Autowired
    private ProvideWebService provideWebService;


    @Override
    public void updateDeviceListTask() {

        try
        {
            List<AppExecutingTaskVO> list = appCaseDeviceService.getRunningTaskList();
            if (CollectionUtils.isNotEmpty(list))
            {
                // 以设备为颗粒度推送消息
                List<DeviceMessage> msgList = new ArrayList<>();

                for (AppExecutingTaskVO taskVO : list)
                {
                    DeviceMessage msg = new DeviceMessage();
                    msg.setDeviceSn(taskVO.getDeviceSn());
                    msg.setCaseId(taskVO.getOldCaseId() + "");
                    msg.setUseStatus("1");
                    msg.setCaseName(taskVO.getCaseName());

                    if (taskVO.getTotalSteps() == null || taskVO.getTotalSteps() == 0 || taskVO.getCurrentStep() == null)
                    {
                        msg.setExecuteRate("0.00%");
                    }
                    else
                    {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String executeRate = df.format(taskVO.getCurrentStep() / taskVO.getTotalSteps());
                        msg.setExecuteRate(executeRate + "%");
                    }

                    msgList.add(msg);
                }

                DeviceListWebSocketServer.sendMsgListToPage(msgList);
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void clearTempImageTask() {
        try
        {
            //Map<String, Map<String, ImageData>>
            //Map<String, Set<DeviceWebSocketServer>> webSocketMap = DeviceWebSocketServer.getConnectionMap();
        }
        catch (Exception e)
        {
            LOGGER.error("清理图片队列出错：", e);
        }
    }

    /**
     * 每隔10s扫描一次短信任务表，并调度发送短信接口
     */
    @Override
    public void querySendMessageTask()
    {
        List<SendMessage> taskList = sendMessageTaskMapper.queryAppTask();
        if (CollectionUtils.isNotEmpty(taskList))
        {
            for (SendMessage sm : taskList)
            {
                try
                {
                    provideWebService.sendMessageTaskToProxy(sm);
                }
                catch (Exception e)
                {
                    LOGGER.error("短信任务异常：" + e.getMessage(), e);
                }

            }
        }
    }

    /**
     * 每天凌晨3点清除临时目录中的数据
     * 包括apk、buildtemp、task、scripts
     */
    @Override
    public void clearRubbishFiles()
    {
        try {
            LOGGER.info("---------------------清理临时垃圾数据----------------------------");
            FolderUtils.deleteFolder(ConstantUtil.ROOT);
            FolderUtils.deleteFolder(ConstantUtil.SCRIPT_TEMP_DIR);
            FolderUtils.deleteFolder(ConstantUtil.APK_TEMP_DIR);
            FolderUtils.deleteFolder(ConstantUtil.TASK_TEMP_URL);
        }
        catch (Exception e)
        {
            LOGGER.error("清理临时数据异常：" + e.getMessage(), e);
        }

    }

    @Override
    public void smsMsgVerifyTask()
    {
        //LOGGER.info("----------------------- 短信校验超时处理 ------------------------------------");
        /**
         * TODO 针对于短信内容超时的步骤
         * 1. 需要修改app_record_step、app_case_result表中相关的信息
         * 2. 校验该步骤对应的用例是否全部执行完成，若是执行完成，则修改对应的数据：app_case、并通知统计用例执行的成功数据和失败数
         */
        List<Map<String, Object>> mapList = appRecordStepService.getAllSmsVerifyStep();
        if (CollectionUtils.isNotEmpty(mapList))
        {
            for (Map<String, Object> map : mapList)
            {
                RedisUtil.delkeyObject("appSmsVerifyMap_"+map.get("deviceSn").toString());

                AppRecordStep recordStep = new AppRecordStep();
                recordStep.setId((Integer) map.get("stepId"));
                recordStep.setEndTime(new Date());
                recordStep.setResult(0);
                recordStep.setReturnInfo("短信内容校验超时");
                appRecordStepService.updateByPrimaryKeySelective(recordStep);

                AppCaseResult caseResult = new AppCaseResult();
                caseResult.setCaseId((Integer) map.get("caseId"));
                caseResult.setDeviceId((Integer) map.get("deviceId"));
                caseResult.setResultId((Integer) map.get("caseResultId"));
                caseResult.setIsFinish(0);
                caseResult.setPassStatus(1);
                caseResult.setExecuteResult(1);
                appCaseResultService.updateByPrimaryKeySelective(caseResult);

                AppCaseDevice caseDevice = new AppCaseDevice();
                caseDevice.setId((Integer) map.get("executeId"));
                caseDevice.setPassStatus(0);
                caseDevice.setFailureReason("短信内容校验超时");
                caseDevice.setExecuteStatus(2);
                appCaseDeviceService.updateBySelective(caseDevice);

                // 校验当前用例是否全部执行完成
                AppCase appCase = appCaseService.getAppCaseById((Integer) map.get("caseId"));
                appCaseService.checkCaseIsFinish(appCase, null);
            }
        }

    }

    @Override
    public void queryScheduledTaskList()
    {

        List<AppCase> appCaseList = appCaseService.listAllTimingCase();
        if (CollectionUtils.isEmpty(appCaseList))
        {
            LOGGER.info("当前没有可调度的用例!");
            return;
        }
        for (AppCase c : appCaseList)
        {
            try
            {
                ExecuteCaseBO caseBO = new ExecuteCaseBO();
                caseBO.setRealExecuteNum(1);
                caseBO.setCaseId(c.getCaseId());
                caseBO.setType(AppConstants.EXECUTE_TYPE_SINGLECASE);
                taskService.commonExecute(caseBO);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
        }

        LOGGER.info("当前需要调度的用例有：" + appCaseList.size() + "个。");
    }

}
