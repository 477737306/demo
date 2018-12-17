package com.cmit.testing.common.syncmsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author XieZuLiang
 */
public class SyncFuture<T> implements Future<T> {

    /**
     * 请求和响应是一一对应的，故初始化CountDownLatch值为1
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private Integer number = 0;

    /**
     * 需要响应线程设置的响应结果对象
     */
    private T response;

    private List<T> responseList = new ArrayList<>();

    /**
     * Future的请求时间，用于计算Future是否超时
     */
    private Long beginTime = System.currentTimeMillis();

    private Integer maxResult = 0;


    public SyncFuture() {
    }

    public SyncFuture(Integer number) {
        this.number = number;
        latch = new CountDownLatch(number);
    }

    public Integer getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if (response != null)
        {
            return true;
        }
        return false;
    }

    /**
     * 获取响应结果，直到有结果才返回
     * @return
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.wait();
        return this.response;
    }

    /**
     * 获取响应结果，直到有结果或者超过指定时间就返回
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit))
        {
            return this.response;
        }
        return null;
    }

    /**
     * 用于设置响应结果，并且做countDown操作，通知请求线程
     * @param response
     */
    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }

    public Long getBeginTime() {
        return beginTime;
    }
}
