package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.app.DeviceDTO;
import com.cmit.testing.entity.app.DeviceInfo;
import com.cmit.testing.entity.proxy.BeartHeatInfo;
import com.cmit.testing.entity.proxy.CurrentExecuteCase;
import com.cmit.testing.entity.proxy.VerifySmsMsg;
import com.cmit.testing.entity.vo.AppDeviceVO;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/8/28.
 */
public interface DeviceService  {

    /**
     * 根据条件查找设备信息列表
     */
    PageBean<Device> findPageDevice(PageBean<Device> pageBean, Map<String, Object> params) ;

    List<Map<String, Object>> queryTerms() ;

    int alterDevice(Device device);

    Device selectByPrimaryKey(Integer id);

    List<Device> getAppDeviceByIds(List<String> ids);

    int updateByKey(Device device);

    int updateDeviceByDeviceSn(Device device);

    Device getDeviceByDeviceSn(String deviceSn);

    int updateByProxy(Map<String, Object> map);

    /**
     * 终端机自动注册
     */
    void deviceRegister(DeviceInfo deviceInfo, String proxyIpMac) throws Exception;

    /**
     * Proxy心跳处理
     */
    void beartHeat(BeartHeatInfo beartHeatInfo, String proxyIpMac) throws Exception;

    void changeTask(CurrentExecuteCase exeCase);

    /**
     * 新建用例-终端机列表显示
     * @param deviceVO
     * @return
     */
    List<AppDeviceVO> getDeviceList(AppDeviceVO deviceVO);

    /**
     * 根据用例ID和Proxy查询相关终端设备ID（deviceId）
     */
    List<Integer> getDeviceIdByProxyAndCaseId(Integer appCaseId, String proxyIp, String proxyMac);

    /**
     * 查询终端数据
     */
    Map<String, Integer> getDeviceData();

    //int updateUseStatusByIds(Integer useStatus, List<Integer> ids);
    int updateDeviceByDto(DeviceDTO dto);

    /**
     * 短信授权码保存
     * @param smsMsg
     */
    void saveSmsMsg(VerifySmsMsg smsMsg);

}
