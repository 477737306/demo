package com.cmit.testing.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.dao.app.AppCaseMapper;
import com.cmit.testing.dao.app.AppScriptStepMapper;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.VerifySmsMsg;
import com.cmit.testing.service.ProjectMenuService;
import com.cmit.testing.service.app.*;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 1:59.
 */
@Service("appScriptStepService")
public class AppScriptStepServiceImpl extends BaseServiceImpl<AppScriptStep> implements AppScriptStepService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppScriptStepServiceImpl.class);

    @Autowired
    private AppScriptStepMapper appScriptStepMapper;

    @Autowired
    private AppCaseDeviceService appCaseDeviceService;

    @Autowired
    private ProjectMenuService projectMenuService;

    @Autowired
    private AppCaseMapper appCaseMapper;

    @Autowired
    private AppCaseService appCaseService;

    @Autowired
    private AppRecordStepService appRecordStepService;

    @Autowired
    private AppCaseResultService appCaseResultService;

    @Override
    public int insertList(List<AppScriptStep> steps) {
        int count = 0;
        for (AppScriptStep step : steps) {
            int i = appScriptStepMapper.insertSelective(step);
            count += i;
        }
        return count;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return appScriptStepMapper.deleteByIds(ids);
    }

    @Override
    public int deleteByScriptFileId(List<Integer> xmlFileIds) {
        return appScriptStepMapper.deleteByScriptFileId(xmlFileIds);
    }

    @Override
    public int deleteByScriptId(Integer scriptId) {
        return appScriptStepMapper.deleteByScriptId(scriptId);
    }

    @Override
    public List<AppScriptStep> getStepByScriptFileId(Integer scriptFileId) {
        return appScriptStepMapper.getStepByScriptFileId(scriptFileId);
    }


    /**
     * 短信内容校验
     * @param smsMsg
     */
    @Override
    public void verifySmsContent(VerifySmsMsg smsMsg) {
        //  deviceSn、imei、content、number
        if (smsMsg != null && StringUtils.isNotEmpty(smsMsg.getContent()))
        {

            Map<String, Object> appSmsVerifyMap = (Map<String, Object>) RedisUtil.getObject("appSmsVerifyMap_"+smsMsg.getDeviceSn());

            LOGGER.info("收到Proxy发过来的短信，并获取要检验的短信内容：appSmsVerifyMap = " + appSmsVerifyMap);

            if (StringUtils.isNotEmpty(smsMsg.getDeviceSn()) && null != appSmsVerifyMap)
            {
                AppScriptStep smsVerifyStep = JsonUtil.getBean(JSONObject.toJSONString(appSmsVerifyMap.get("appSmsVerifyStep")), AppScriptStep.class);

                LOGGER.info("已找到匹配的短信校验数据：AppScriptStep = " + smsVerifyStep.getInputValue());

                if (verifyContent(smsMsg.getContent(), smsVerifyStep.getInputValue()))
                {

                    // 校验成功，删除Redis中的数据
                    RedisUtil.delkeyObject("appSmsVerifyMap_"+smsMsg.getDeviceSn());

                    Integer appCaseId = (Integer) appSmsVerifyMap.get("appCaseId");

                    // 校验成功，则结束app_case_device当前执行状态，并且修改短信校验步骤信息app_record_step
                    AppCaseDevice caseDevice = new AppCaseDevice();
                    caseDevice.setCaseId(appCaseId);
                    caseDevice.setDeviceSn(smsMsg.getDeviceSn());
                    caseDevice.setExecuteStatus(Constants.EXECUTE_STATUS_DONE);
                    appCaseDeviceService.updateByCaseIdAndDeviceId(caseDevice);

                    AppRecordStep recordStep = JsonUtil.getBean(JSONObject.toJSONString(appSmsVerifyMap.get("appRecordStep")), AppRecordStep.class);

                    Date date = new Date();
                    Long consumeTime = date.getTime() - recordStep.getBeginTime().getTime();
                    recordStep.setEndTime(date);
                    recordStep.setConsumeTime(consumeTime.intValue());
                    recordStep.setResult(1);
                    recordStep.setReturnInfo("success");
                    appRecordStepService.updateByPrimaryKeySelective(recordStep);

                    AppCaseResult appCaseResult = new AppCaseResult();
                    appCaseResult.setExecuteResult(0);
                    appCaseResult.setResultId(recordStep.getCaseResultId());
                    appCaseResult.setConsumeTime(recordStep.getConsumeTime());
                    appCaseResultService.updateByPrimaryKeySelective(appCaseResult);

                    // 校验当前用例是否已经执行完成
                    AppCase appCase = appCaseMapper.getAppCaseById(appCaseId);
                    appCaseService.checkCaseIsFinish(appCase, null);
                }
            }
            else
            {
                LOGGER.info("未找到匹配的短信校验数据： deviceSn = " + smsMsg.getDeviceSn() + ", appSmsVerifyMap = " + appSmsVerifyMap);
            }
        }

    }

    /**
     * 校验短信内容
     * @return
     */
    private boolean verifyContent(String msg, String msgModule)
    {
        boolean resultFlag = false;

        // 2018-11-12 新增 '|' 判别条件
        List<String> _msgList = Arrays.asList(msgModule.split("\\|"));
        for (String msgStr : _msgList)
        {
            List<String> msgList = Arrays.asList(msgStr.replace("&amp;", "&").split("&"));
            boolean _resultFlag = true;
            for (String msgCode : msgList)
            {
                if (!msg.contains(msgCode))
                {
                    _resultFlag = false;
                    break;
                }
            }

            // 与条件成立，则可以直接判定校验成功，结束校验；否则就继续校验
            if (_resultFlag)
            {
                resultFlag = true;
                break;
            }
        }

        if (resultFlag)
        {
            LOGGER.info("短信内容校验成功： msg = " + msg + ", msgModule = " + msgModule + ", success = " + resultFlag);
        }
        else
        {
            LOGGER.info("短信内容校验失败： msg = " + msg + ", msgModule = " + msgModule + ", success = " + resultFlag);
        }
        return resultFlag;
    }

}
