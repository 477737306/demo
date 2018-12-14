package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppCaseDeviceMapper;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.proxy.CurrExecuteStep;
import com.cmit.testing.entity.vo.AppExecutingTaskVO;
import com.cmit.testing.service.app.AppCaseDeviceService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/2 0002 上午 10:29.
 */
@Service("appCaseDeviceService")
public class AppCaseDeviceServiceImpl extends BaseServiceImpl<AppCaseDevice> implements AppCaseDeviceService {

    @Autowired
    private AppCaseDeviceMapper appCaseDeviceMapper;

    /**
     * 提供web侧使用：获取用例执行通过的手机号码接口
     * @param caseId 用例ID
     * @param type
     *      0-单独执行的用例；1-批量任务执行的用例；2-众测任务执行的用例
     * @param taskId 任务ID
     * @return
     *      执行通过的手机号码集合
     */
    @Override
    public List<String> getPhoneNumList(Integer caseId, String type, Integer taskId)
    {
        List<String> phoneNumList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("taskId", taskId);
        if ("1".equals(type))
        {
            // 批量任务
            map.put("type", "BatchTask");
            phoneNumList.addAll(appCaseDeviceMapper.getPhoneNumList(map));
        }
        else if ("2".equals(type))
        {
            // 众测任务
            map.put("type", "SurveyTask");
            phoneNumList.addAll(appCaseDeviceMapper.getPhoneNumList(map));
        }
        else
        {
            // 单独执行
            map.put("type", "SingleTask");
            phoneNumList.addAll(appCaseDeviceMapper.getPhoneNumList(map));
        }
        return phoneNumList;
    }

    @Override
    public List<AppCaseDevice> getCaseDeviceList(Integer followId)
    {
        List<AppCaseDevice> deviceList = appCaseDeviceMapper.getCaseDeviceList(followId);
        if (CollectionUtils.isNotEmpty(deviceList))
        {
            return deviceList;
        }
        else
        {
            return Collections.emptyList();
        }
    }

    /**
     * Proxy执行脚本返回具体执行到第几步的信息
     */
    @Override
    public void changeStep(CurrExecuteStep step)
    {
        
        AppCaseDevice acd = new AppCaseDevice();
        acd.setCurrentStep(step.getCurrentStep());
        acd.setTotalSteps(step.getTotalStep());
        acd.setCaseId(step.getAppCaseId());
        acd.setDeviceSn(step.getDeviceSn());

        appCaseDeviceMapper.updateStatusByDeviceSnAndCaseId(acd);
    }

    @Override
    public List<AppExecutingTaskVO> getRunningTaskList() {
        return appCaseDeviceMapper.getRunningTaskList();
    }

    @Override
    public List<AppCase> getCaseIdByDeviceSn(String deviceSn) {
        return appCaseDeviceMapper.getCaseIdByDeviceSn(deviceSn);
    }

    @Override
    public int insertList(List<AppCaseDevice> list) {
        int count = 0;
        for (AppCaseDevice cd : list)
        {
            int i = appCaseDeviceMapper.insertSelective(cd);
            count += i;
        }
        return count;
    }

    @Override
    public int updateStatusByDeviceSnAndCaseId(List<AppCaseDevice> devList) {
        int count = 0;
        for (AppCaseDevice cd : devList) {
            int i = appCaseDeviceMapper.updateStatusByDeviceSnAndCaseId(cd);
            count += i;
        }
        return count;
    }

    @Override
    public int updateByCaseIdAndDeviceId(AppCaseDevice cd) {
        return appCaseDeviceMapper.updateByCaseIdAndDeviceId(cd);
    }

    @Override
    public List<AppCaseDevice> getListByNotFinish(Integer caseId, List<Integer> statusList) {
        return appCaseDeviceMapper.getListByNotFinish(caseId, statusList);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return appCaseDeviceMapper.deleteByIds(ids);
    }

    @Override
    public int deleteByCaseId(Integer appCaseId) {
        AppCaseDevice cd = new AppCaseDevice();
        cd.setCaseId(appCaseId);
        return appCaseDeviceMapper.deleteBySelective(cd);
    }

    @Override
    public List<AppCaseDevice> selectByCaseDevice(AppCaseDevice cd) {
        return appCaseDeviceMapper.selectByCaseDevice(cd);
    }

    @Override
    public int save(AppCaseDevice record) {
        return appCaseDeviceMapper.insert(record);
    }

    @Override
    public int saveCaseDevice(AppCaseDevice record) {
        return appCaseDeviceMapper.insertSelective(record);
    }

    @Override
    public AppCaseDevice getCaseDeviceById(Integer id) {
        return appCaseDeviceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateBySelective(AppCaseDevice record) {
        return appCaseDeviceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateById(AppCaseDevice record) {
        return appCaseDeviceMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<AppCaseDevice> getListByCondition(AppCaseDevice acd) {
        return appCaseDeviceMapper.getListByCondition(acd);
    }

    @Override
    public List<AppCaseDevice> getListByCaseId(Integer caseId) {
        return appCaseDeviceMapper.getListByCaseId(caseId);
    }

    @Override
    public Map<String, Object> getCountByCaseId(Integer caseId) {
        return appCaseDeviceMapper.getCountByCaseId(caseId);
    }
}
