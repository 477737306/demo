package com.cmit.testing.listener;

/**
 * @author XieZuLiang
 * @description 定时任务
 * @date 2018/10/13 0013.
 */
public interface TaskScheduleService {

    /**
     * 定时更新设备的状态
     */
    public void updateDeviceListTask();

    /**
     * 定时清理图片传输队列中图片
     */
    public void clearTempImageTask();

    /**
     * 定时扫描短信任务表，并下发
     */
    public void querySendMessageTask();

    /**
     * 定时清理垃圾文件
     */
    public void clearRubbishFiles();

    /**
     * 定时执行的所有任务处理逻辑
     */
    public void queryScheduledTaskList();

    /**
     * 短信校验超时处理任务
     */
    public void smsMsgVerifyTask();

}
