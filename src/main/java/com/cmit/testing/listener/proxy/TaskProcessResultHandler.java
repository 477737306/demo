package com.cmit.testing.listener.proxy;

import com.cmit.testing.entity.proxy.CmdParamStartTask;
import com.cmit.testing.entity.proxy.TestResultInfo;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.utils.SpringContextHolder;
import com.cmit.testing.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/16 0016.
 */
@Component
public class TaskProcessResultHandler implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskProcessResultHandler.class);

    @Autowired
    private AppCaseService appCaseService;

    /**
     * 用于存放任务下发后proxy返回的信息(包含任务回执和测试结果)
     */
    private static ConcurrentLinkedQueue<Object> processResultQueue = new ConcurrentLinkedQueue<Object>();

    @Override
    public void afterPropertiesSet() {
        LOGGER.info("--------------- 启动处理用例下发后，Proxy返回信息的线程 -----------------");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try
                    {
                        if (processResultQueue.isEmpty())
                        {
                            Thread.sleep(50);
                        }

                        Object processResultInfo = processResultQueue.poll();

                        if (processResultInfo != null)
                        {
                            // 处理下发用例响应的消息
                            if (processResultInfo instanceof HashMap)
                            {
                                Map<String, Object> map = (Map<String, Object>) processResultInfo;
                                String proxyIpMac = (String) map.get("proxyIpMac");
                                CmdParamStartTask cmdParam = (CmdParamStartTask) map.get("cmdParamStartTask");
                                if (cmdParam != null && StringUtils.isNotEmpty(proxyIpMac))
                                {
                                    appCaseService.updateTestCase(cmdParam, proxyIpMac);
                                }
                            }
                            // 处理用例执行完成后的执行结果
                            else if (processResultInfo instanceof TestResultInfo)
                            {
                                appCaseService.readTestResult((TestResultInfo) processResultInfo);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("处理Proxy返回的信息异常：" + e.getMessage(), e);
                    }
                }
            }
        });
        thread.start();
    }

    public static ConcurrentLinkedQueue<Object> getProcessResultQueue() {
        return processResultQueue;
    }
}
