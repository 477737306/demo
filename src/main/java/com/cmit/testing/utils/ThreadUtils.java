package com.cmit.testing.utils;

import com.cmit.testing.common.thread.ScriptStepScreenshotExecutionThread;
import com.cmit.testing.entity.TestCase;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadUtils {

    public static LinkedBlockingQueue<Callable> taskList = new LinkedBlockingQueue<>();
    public static Map<Integer, Map<String, Integer>> testcaseMap = new ConcurrentHashMap<>();
    public static ThreadPoolExecutor poolExecutor;

    public static volatile AtomicInteger connSize = new AtomicInteger(20);

    public static ReentrantLock reentrantLock;

    static {
        poolExecutor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        reentrantLock = new ReentrantLock();
    }

    public static void executeThread(Callable task) {
        Future<Integer> future = poolExecutor.submit(task);
        try {
            if (future.get() == 0) {

                connSize.incrementAndGet();
                setTestcaseMap((ScriptStepScreenshotExecutionThread) task);
                runTask(null);
            }
        } catch (Exception e) {
            connSize.incrementAndGet();
            setTestcaseMap((ScriptStepScreenshotExecutionThread) task);
        }
    }

    public static void addTask(Callable task) {
        setTestcaseMap((ScriptStepScreenshotExecutionThread) task);
        if (connSize.get() > 0) {
            runTask(task);
        } else {
            try {
                taskList.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取执行用例执行信息
     * 0 未找到 1 执行中 2 执行结束
     *
     * @param testcaseId 执行用例id
     * @return
     */
    public static Integer getTestcaseExecuteStatus(Integer testcaseId) {
        if (testcaseId != null) {
            Map<String, Integer> tmpMap = testcaseMap.get(testcaseId);
            if (tmpMap != null) {
                return tmpMap.get("status");
            }
        }
        return 0;
    }

    /**
     * 移除执行用例执行信息
     *
     * @param testcaseId 执行用例id
     */
    public static void removeTestcaseExecuteStatus(Integer testcaseId) {
        if (testcaseId != null) {
            Map<String, Integer> tmpMap = testcaseMap.get(testcaseId);
            if (tmpMap != null) {
                testcaseMap.remove(testcaseId);
            }
        }
    }

    private static void setTestcaseMap(ScriptStepScreenshotExecutionThread task) {
        //获取执行用例
        TestCase testCase = task.getTestCase();
        //执行次数
        Integer executeNumber = testCase.getExecuteNumber();
        //用例id
        Integer testcaseId = testCase.getId();
        //获取该用例的map
        Map<String, Integer> tmpMap = testcaseMap.get(testcaseId);
        if (tmpMap == null) {
            tmpMap = new ConcurrentHashMap<>();
            tmpMap.put("status", 1);
            tmpMap.put("executeNumber", executeNumber);
            tmpMap.put("executedNumber", 0);
        } else {
            //已执行次数
            Integer executedNumber = tmpMap.get("executedNumber");
            executedNumber++;
            if (executedNumber.equals(executeNumber)) {
                tmpMap.put("status", 2);
            }
            tmpMap.put("executedNumber", executedNumber);
        }
        testcaseMap.put(testcaseId, tmpMap);
    }

    private static void runTask(Callable task) {
        try {
            if (task != null || (task = taskList.poll()) != null) {
                connSize.decrementAndGet();
                executeThread(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
