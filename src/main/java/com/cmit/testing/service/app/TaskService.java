package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.app.CmdParamJson;
import com.cmit.testing.entity.app.ExecuteCaseBO;

import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description APP任务执行接口：用例批量执行和单个用例执行
 * @date 2018/9/12 0012.
 */
public interface TaskService {


    /**
     * 执行用例的公共方法
     * @param caseBO
     */
    List<Integer> commonExecute(ExecuteCaseBO caseBO);

    void executeMethod(AppCase appCase, List<AppCaseDevice> deviceList);

    /**
     * 执行前生成对应的副本用例
     * @return
     */
    AppCase createCaseExecuteBefore(AppCase appCase, Integer realExecuteNum);

    /**
     * 单个用例执行
     * @param cmdParam
     * @return
     */
    void executeSingleCase(CmdParamJson cmdParam, List<AppCaseDevice> deviceList);

    /**
     * 任务调度执行
     *  先进先出
     * @param deviceSn
     */
    void schedulingTask(String deviceSn);

    /**
     * 下发任务消息给Proxy
     * @return
     */
    int sendMsgToProxy(CmdParamJson cmdParam, Integer proxyId, Integer miss);
}
