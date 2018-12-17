package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.vo.AppExecutingTaskVO;
import com.cmit.testing.entity.vo.CaseExcResultVO;
import com.cmit.testing.page.PageBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AppCaseDeviceMapper extends BaseMapper<AppCaseDevice>{

    /**
     * 提供给web侧使用： 获取执行通过的手机号
     * @param map
     * @return
     */
    List<String> getPhoneNumList(Map<String, Object> map);

    /**
     * 获取依赖用例执行通过的手机设备
     * @param followId
     * @return
     */
    List<AppCaseDevice> getCaseDeviceList(Integer followId);

    /**
     * 获取所有APP执行中的任务
     * @param resultVO
     * @return
     */
    List<AppCase> getAllRunningAppCase(CaseExcResultVO resultVO);

    /**
     * 获取所有正在执行的设备信息
     * @return
     */
    List<AppExecutingTaskVO> getRunningTaskList();

    int deleteDeviceByCaseIds(@Param("caseIdList") List<Integer> caseIdList);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);
    int deletByExecuteId(@Param("ids") List<Integer> ids);

    int deleteByCaseId(@Param("caseId") Integer caseId);
    /**
     * 根据条件删除数据
     * @param record
     * @return
     */
    int deleteBySelective(AppCaseDevice record);

    /**
     * 根据ID删除用例终端关联数据
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加数据（所有字段都有值）
     * @param record
     * @return
     */
    int insert(AppCaseDevice record);

    /**
     * 插入数据（部分字段有值）
     * @param record
     * @return
     */
    int insertSelective(AppCaseDevice record);

    /**
     * 根据主键ID查询数据对象
     * @param id
     * @return
     */
    AppCaseDevice selectByPrimaryKey(Integer id);

    List<AppCaseDevice> selectByCaseDevice(AppCaseDevice cd);

    List<AppCaseDevice> getBySysSurveyIdAndCaseId(@Param("surveyTaskId") Integer surveyTaskId, @Param("caseId") Integer caseId);
    /**
     * 更新部分字段的值
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AppCaseDevice record);

    /**
     * 更新所有字段的值
     * @param record
     * @return
     */
    int updateByPrimaryKey(AppCaseDevice record);

    List<AppCaseDevice> getListByCondition(AppCaseDevice acd);

    List<AppCaseDevice> getListByCaseId(Integer caseId);

    /**
     * 根据DeviceSN修改执行用例的设备状态
     * @param acd
     * @return
     */
    int updateStatusByDeviceSnAndCaseId(AppCaseDevice acd);

    /**
     * 根据caseID和deviceID修改用例执行设备的信息
     */
    int updateByCaseIdAndDeviceId(AppCaseDevice acd);

    /**
     * 批量修改
     * 不完全是批量修改，调用时注意SQL使用限制
     * @param appCaseDevice
     * @return
     */
    int batchUpdateCaseDevice(AppCaseDevice appCaseDevice);

    /**
     *  统计当前用例下未执行完成的测试机
     */
    List<AppCaseDevice> getListByNotFinish(@Param("caseId") Integer caseId, @Param("statusList") List<Integer> statusList);

    /**
     * 统计用例在终端机上执行的成功数和失败数
     * @param caseId
     * @return
     */
    Map<String, Object> getCountByCaseId(@Param("caseId") Integer caseId);

    /**
     * 根据设备唯一标识获取当前设备要执行的用例ID列表
     * @param deviceSn
     * @return
     */
    List<AppCase> getCaseIdByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 查询执行次数
     * @return
     */
    Integer queryExcuteNum();
    Integer deletByReustId(@Param("ids") List<Integer> ids);

    Map<String, Integer> getMaxExecuteNum(Integer appCaseId);
    List<AppCaseDevice>selectByCaseId(Integer caseId);
}