package com.cmit.testing.dao.app;

import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.app.DeviceDTO;
import com.cmit.testing.entity.app.SimCardInfo;
import com.cmit.testing.entity.vo.AppDeviceVO;
import com.cmit.testing.listener.operatecmd.DpDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/8/28.
 */
@Mapper
public interface DeviceMapper {

    /**
     * 统计App侧 关联了手机号并在线的终端数
     * @return
     */
    Map<String, Long> countAppDevice();

    /**
     * 统计App侧 终端覆盖的省份
     * @return
     */
    List<String> countAppDeviceProvince();

    List<Device> getDeviceByPhoneNum(@Param("phoneNumList") List<String> phoneNumList);
    /**
     * 根据条件查询Device列表
     * @param params
     * @return
     */
    List<Device> queryPageDeviceList(Map<String, Object> params);

    List<String> queryTerms(Map<String, String> map);

    List<Device> queryDeviceByTelNum(@Param("telNum") String telNum, @Param("deviceId") Integer deviceId);

    int updateDeviceTelNum(Device device);

    int updateByKey(Device device);

    int updateDeviceByDeviceSn(Device device);

    Device getDeviceByDeviceSn(@Param("deviceSn") String deviceSn);

    int updateByProxy(Map<String, Object> map);

    Device selectByPrimaryKey(Integer id);

    /**
     * 根据IDs查询终端
     * @param ids
     * @return
     */
    List<Device> getAppDeviceByIds(@Param("ids") List<Integer> ids);

    List<Device> getList(Device device);

    List<Device> deviceRegister(Device device);

    /**
     * 添加终端机信息
     * @param device 终端机
     * @return 添加的条数
     */
    int insertSelective(Device device);

    /**
     * 根据Proxy的Ip地址和Mac地址查询终端
     */
    List<Device> getDeviceByProxyIpAndMac(@Param("proxyIp") String proxyIp, @Param("proxyMac") String proxyMac);

    /**
     *  根据终端ID集合 修改其在线状态
     * @param onlineStatus 在线状态
     * @param ids 终端ID列表
     * @return
     */
    int updateOnlineStatusByIds(@Param("onlineStatus") String onlineStatus, @Param("ids") List<Integer> ids);

    /**
     * 根据终端ID集合 修改其使用状态
     */
    //int updateUseStatusByIds(@Param("useStatus") Integer useStatus, @Param("ids") List<Integer> ids);

    /**
     * 修改设备
     * @param dto
     * @return
     */
    int updateDeviceByDto(DeviceDTO dto);
    /**
     * 新建用例-终端机列表显示
     * @param deviceVO
     * @return
     */
    List<AppDeviceVO> getDeviceList(AppDeviceVO deviceVO);

    /**
     * 根据用例ID和Proxy查询相关终端设备ID（deviceId）
     */
    List<Integer> getDeviceIdByProxyAndCaseId(@Param("appCaseId") String appCaseId, @Param("proxyIp") String proxyIp,
                                              @Param("proxyMac") String proxyMac);


    SimCardInfo getSimCardAndDeviceByDeviceSn(String deviceSn);

    DpDto getDeviceAndProxyByPhone(@Param("phoneNum") String phoneNum);

}
