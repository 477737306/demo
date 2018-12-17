package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppRecordStep;

import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/10/12 0012.
 */
public interface AppRecordStepService {

    /**
     * 批量添加脚本步骤记录信息
     * @param recordSteps
     * @return
     */
    int insertList(List<AppRecordStep> recordSteps);

    int insertSelective(AppRecordStep record);

    int updateByPrimaryKeySelective(AppRecordStep record);

    /**
     * 删除步骤详情记录信息
     * @param step
     * @return
     */
    int deleteRecordStep(AppRecordStep step);

    /**
     * 获取短信内容校验超时步骤相关的信息
     * @return
     */
    List<Map<String, Object>> getAllSmsVerifyStep();


}
