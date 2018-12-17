package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.proxy.CurrExecuteStep;
import com.cmit.testing.entity.vo.AppExecutingTaskVO;
import com.cmit.testing.service.BaseService;
import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/2 0002 上午 10:23.
 */
public interface AppCaseDeviceService extends BaseService<AppCaseDevice> {

    /**
     * 提供web侧使用：获取用例执行通过的手机号码接口
     * @param caseId 用例ID
     * @param type
     *  0-单独执行的用例；1-批量任务执行的用例；2-众测任务执行的用例
     * @param taskId 任务ID
     * @return
     *  执行通过的手机号码集合
     */
    List<String> getPhoneNumList(Integer caseId, String type, Integer taskId);

    /**
     * 根据依赖的ID 获取执行通过的设备
     * @param followId
     * @return
     */
    List<AppCaseDevice> getCaseDeviceList(Integer followId);

    /**
     * 修改设备执行脚本的情况
     * @param step
     */
    public void changeStep(CurrExecuteStep step);

    List<AppExecutingTaskVO> getRunningTaskList();

    /**
     * 批量添加
     * @param list
     * @return
     */
    int insertList(List<AppCaseDevice> list);

    int deleteByIds(List<Integer> ids);

    List<AppCaseDevice> selectByCaseDevice(AppCaseDevice cd);

    /**
     * 根据 appCaseId 删除相关的数据
     */
    int deleteByCaseId(Integer appCaseId);

    int save(AppCaseDevice record);

    int saveCaseDevice(AppCaseDevice record);

    AppCaseDevice getCaseDeviceById(Integer id);

    int updateBySelective(AppCaseDevice record);

    int updateById(AppCaseDevice record);

    /**
     * 根据deviceSn和caseID修改下发状态
     * @param devList
     * @return
     */
    int updateStatusByDeviceSnAndCaseId(List<AppCaseDevice> devList);

    List<AppCaseDevice> getListByCondition(AppCaseDevice acd);

    List<AppCaseDevice> getListByCaseId(Integer caseId);

    int updateByCaseIdAndDeviceId(AppCaseDevice cd);

    List<AppCaseDevice> getListByNotFinish(Integer caseId, List<Integer> statusList);

    /**
     * 统计用例在终端机上执行的成功数和失败数
     * @param caseId
     * @return
     */
    Map<String, Object> getCountByCaseId(Integer caseId);

    /**
     * 根据设备唯一标识获取当前设备要执行的用例ID列表
     * @param deviceSn
     * @return
     */
    List<AppCase> getCaseIdByDeviceSn(String deviceSn);

}
