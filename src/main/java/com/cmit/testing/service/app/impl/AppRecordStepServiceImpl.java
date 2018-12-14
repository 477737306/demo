package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppRecordStepMapper;
import com.cmit.testing.entity.app.AppRecordStep;
import com.cmit.testing.service.app.AppRecordStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/10/12 0012.
 */
@Service("appRecordStepService")
public class AppRecordStepServiceImpl implements AppRecordStepService {

    @Autowired
    private AppRecordStepMapper appRecordStepMapper;

    @Override
    public List<Map<String, Object>> getAllSmsVerifyStep() {
        return appRecordStepMapper.getAllSmsVerifyStep();
    }

    @Override
    public int insertList(List<AppRecordStep> recordSteps) {

        return appRecordStepMapper.insertList(recordSteps);
    }

    @Override
    public int deleteRecordStep(AppRecordStep step) {

        return appRecordStepMapper.deleteRecordStep(step);
    }

    @Override
    public int updateByPrimaryKeySelective(AppRecordStep step) {
        return appRecordStepMapper.updateByPrimaryKeySelective(step);
    }

    @Override
    public int insertSelective(AppRecordStep record) {
        return appRecordStepMapper.insertSelective(record);
    }
}
