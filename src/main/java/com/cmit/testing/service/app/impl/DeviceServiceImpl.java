package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.dao.SimEquipmentMapper;
import com.cmit.testing.dao.app.AppParamMapper;
import com.cmit.testing.dao.app.DeviceMapper;
import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.Product;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.BeartHeatInfo;
import com.cmit.testing.entity.proxy.CurrentExecuteCase;
import com.cmit.testing.entity.proxy.VerifySmsMsg;
import com.cmit.testing.entity.vo.AppDeviceVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.MessageService;
import com.cmit.testing.service.app.AppCaseDeviceService;
import com.cmit.testing.service.app.AppProxyService;
import com.cmit.testing.service.app.DeviceService;
import com.cmit.testing.utils.app.DeviceConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

/**
 * Created by zhangxiaofang on 2018/8/28.
 */
@Service("deviceService")
public class DeviceServiceImpl implements DeviceService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    private AppProxyService appProxyService;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;
    @Autowired
    private AppParamMapper appParamMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private SimEquipmentMapper simEquipmentMapper;

    @Override
    public PageBean<Device> findPageDevice(PageBean<Device> pageBean, Map<String, Object> params) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<Device> list = deviceMapper.queryPageDeviceList(params);
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public int updateDeviceByDto(DeviceDTO dto) {
        return deviceMapper.updateDeviceByDto(dto);
    }

    @Override
    public List<Map<String, Object>> queryTerms() {
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("column", "device_brand");
        List<String> brandMenu = removeBlankFromList(deviceMapper.queryTerms(paraMap));
        Map<String,Object> brandMap = getResultMap("brand",brandMenu);
        resultList.add(brandMap);
       // resultMap.put("brand", removeBlankFromList(deviceMapper.queryTerms(paraMap)));

        paraMap.put("column", "resolution");
        String[] resolutionStr = new String[]{"320*480", "480*640", "480*800", "480*854", "600*800", "540*960",
                "640*960", "720*1280", "768*1280", "800*1280", "1080*1920", "1080*2160", "1440*2560", "1440*2960","1920*1080", "2160*3840"};
        ArrayUtils.reverse(resolutionStr);
        List<String> resolutionList = removeBlankFromList(deviceMapper.queryTerms(paraMap));
        List<String> finalResolutionList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(resolutionList)) {
            for (String currentResolution : resolutionStr)
            {
                if(resolutionList.contains(currentResolution))
                {
                    finalResolutionList.add(currentResolution);
                }
            }
        }
        Map<String,Object> resolutionMap = getResultMap("resolution",finalResolutionList);
        resultList.add(resolutionMap);
        //resultMap.put("resolution",finalResolutionList );

        paraMap.put("column","sys_os");
        List<String> sysOsMenu = removeBlankFromList(deviceMapper.queryTerms(paraMap));
        Map<String,Object> sysOsMap = getResultMap("sysOs",sysOsMenu);
        resultList.add(sysOsMap);
       // resultMap.put("sysOs",removeBlankFromList(deviceMapper.queryTerms(paraMap))) ;

        List<String> onlineStatus = new ArrayList<String>();
        onlineStatus.add(DeviceConstant.ONLINE_STATUS_0);
        onlineStatus.add(DeviceConstant.ONLINE_STATUS_1);
        Map<String,Object> onlineStatusMap = getResultMap("onlineStatus",onlineStatus);
        resultList.add(onlineStatusMap);
        //resultMap.put("onLineStatus", onlineStatus);

        List<String> useStatus = new ArrayList<String>();
        useStatus.add(DeviceConstant.USER_STATUS_0);
        useStatus.add(DeviceConstant.USER_STATUS_1);
        Map<String,Object> useStatusMap = getResultMap("useStatus",useStatus);
        resultList.add(useStatusMap);
        return resultList;
    }

    /**
     * 更改设备信息(号码)
     * @param device
     * @return
     */
    @Override
    public int alterDevice(Device device) {
        if (StringUtils.isNotEmpty(device.getTelNum()))
        {
            SimCard simCard = simCardMapper.getByPhone(device.getTelNum());
            if (simCard != null)
            {
                device.setProvince(simCard.getProvince());
            }
        }
        Integer updatePerson = ShiroKit.getUser().getId();
        device.setUpdatePerson(updatePerson);
        device.setUpdateTime(new Date());
        List<Device> deviceList = deviceMapper.queryDeviceByTelNum(device.getTelNum(), device.getId());
        if (deviceList.size() > 0) {
            // 若当前手机号已经绑定在其他手机上的话，则需要将其他手机的号码置空
            for (Device d : deviceList)
            {
                d.setTelNum(null);
                deviceMapper.updateDeviceTelNum(d);
            }
        }
        return deviceMapper.updateDeviceTelNum(device);
    }

    @Override
    public Device selectByPrimaryKey(Integer id) {
        return deviceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Device> getAppDeviceByIds(List<String> ids) {
        List<Integer> list = new ArrayList<>();
        for (String id : ids) {
            list.add(Integer.parseInt(id));
        }
        return deviceMapper.getAppDeviceByIds(list);
    }

    /**
     * 获取指定格式的真机查询条件
     * @param name
     * @param menu
     * @return
     */
    public Map<String,Object> getResultMap(String name,List<String>menu) {
        Map<String,Object> resultMap = new HashMap<>();
        if ("brand".equals(name)) {
            resultMap.put("name","品牌:");
        } else if ("resolution".equals(name)) {
            resultMap.put("name","分辨率:");
        } else if ("sysOs".equals(name)) {
            resultMap.put("name","系统版本:");
        } else if ("useStatus".equals(name)) {
            resultMap.put("name","使用状态:");
        } else if ("onlineStatus".equals(name)) {
            resultMap.put("name","连接状态:");
        }
        List<Map<String,String>> menuMapList = new ArrayList<>();
        if (menu.size() > 0) {
            for (String m : menu) {
                Map<String,String> map = new HashMap<>();
                map.put("name",m);
                menuMapList.add(map);
            }
        }
        resultMap.put("menu",menuMapList);
        return resultMap;
    }

    /**
     * 删除list中空元素
     * @param list
     * @return
     */
    public  List<String> removeBlankFromList(List<String> list){
        for(int i=0;i<list.size();i++){
            if(StringUtils.isBlank(list.get(i))){
                list.remove(i);
                i--;
            }
        }
        return list;
    }

    @Override
    public int updateByKey(Device device) {
        return deviceMapper.updateByKey(device);
    }

    @Override
    public int updateDeviceByDeviceSn(Device device) {
        return deviceMapper.updateDeviceByDeviceSn(device);
    }

    @Override
    public int updateByProxy(Map<String, Object> map) {
        return deviceMapper.updateByProxy(map);
    }

    @Override
    public void deviceRegister(DeviceInfo deviceInfo, String proxyIpMac) {
        String []proxy = proxyIpMac.split("_");
        if (deviceInfo == null || deviceInfo.getDeviceSn() == null || "".equals(deviceInfo.getDeviceSn()))
        {
            LOGGER.error("终端手机序列号不能为空...");
            return;
        }
        if (64 < deviceInfo.getDeviceSn().trim().length())
        {
            LOGGER.error("终端手机序列号太长，不能超过64位：deviceSn = " + deviceInfo.getDeviceSn());
            return;
        }

        Device device = new Device();
        device.setDeviceSn(deviceInfo.getDeviceSn());
        device.setImei1(deviceInfo.getImei());
        List<Device> list = deviceMapper.deviceRegister(device);
        Date date = new Date();
        if (list != null && list.size() > 0){
            // 数据库中已有该设备，更新数据即可
            Device d = list.get(0);
            d.setDeviceSn(deviceInfo.getDeviceSn());
            if (StringUtils.isNotBlank(deviceInfo.getBrand())){
                d.setBrand(deviceInfo.getBrand());
            }
            if (StringUtils.isNotBlank(deviceInfo.getSysOs())){
                String os = deviceInfo.getSysOs();
                if (StringUtils.isNotBlank(deviceInfo.getSysOsVersion()))
                {
                    os = os + " " + deviceInfo.getSysOsVersion();
                }
                d.setSysOs(os);
            }
            if (StringUtils.isNotBlank(deviceInfo.getBoard())){
                d.setVersion(deviceInfo.getBoard());
            }
            if (StringUtils.isNotBlank(deviceInfo.getResolution())){
                d.setResolution(deviceInfo.getResolution());
            }
            String imeiStr = deviceInfo.getImei();
            setImeiToDevice(d, imeiStr);
            d.setUpdateTime(date);
            d.setUpdatePerson(DeviceConstant.AUTO_REISTER_888);
            // 在线状态：0-离线；1-在线
            d.setOnlineStatus(DeviceConstant.ONLINE_STATUS_1);
            // 获取Proxy信息
            AppProxy ap = new AppProxy();
            ap.setProxyIp(proxy[0]);
            ap.setProxyMac(proxy[1]);
            AppProxy p = appProxyService.getProxyByIpAndMac(ap);
            if (p != null){
                d.setProxyId(p.getProxyId());
            }else {
                LOGGER.warn("设备自动注册失败，deviceSn=" + deviceInfo.getDeviceSn() + "。 服务器端无proxy " + proxyIpMac + " 的信息！");
                return;
            }

            updateSimCardByIMSI(d, deviceInfo);

            deviceMapper.updateByKey(d);

        }else{
            // 数据库中无该设备，新增一条数据即可
            device.setBrand(deviceInfo.getBrand());
            String os = deviceInfo.getSysOs();
            if (StringUtils.isNotEmpty(deviceInfo.getSysOsVersion()))
            {
                os = os + " " + deviceInfo.getSysOsVersion();
            }
            device.setSysOs(os);
            device.setResolution(deviceInfo.getResolution());
            device.setVersion(deviceInfo.getBoard());
            device.setCreateTime(date);
            // 自动注册
            device.setCreatePerson(DeviceConstant.AUTO_REISTER_888);
            device.setUpdateTime(date);
            device.setUpdatePerson(DeviceConstant.AUTO_REISTER_888);
            String imeiStr = deviceInfo.getImei();
            setImeiToDevice(device, imeiStr);
            // 在线状态：0-在线；1-离线
            device.setOnlineStatus(DeviceConstant.ONLINE_STATUS_0);
            device.setUseStatus(DeviceConstant.USER_STATUS_0);
            // 获取Proxy信息
            AppProxy ap = new AppProxy();
            ap.setProxyIp(proxy[0]);
            ap.setProxyMac(proxy[1]);
            AppProxy p = appProxyService.getProxyByIpAndMac(ap);
            if (p != null){
                device.setProxyId(p.getProxyId());
            }else {
                LOGGER.warn("设备自动注册失败，deviceSn=" + deviceInfo.getDeviceSn() + "。 服务器端无proxy " + proxyIpMac + " 的信息！");
                return;
            }
            updateSimCardByIMSI(device, deviceInfo);

            deviceMapper.insertSelective(device);

        }


    }

    /**
     * SIM卡与手机设备自动识别 处理
     * @param d
     * @param deviceInfo
     */
    private void updateSimCardByIMSI(Device d, DeviceInfo deviceInfo)
    {
        if (StringUtils.isNotEmpty(deviceInfo.getTelNumber()))
        {
            d.setTelNum(deviceInfo.getTelNumber());
        }
        else
        {
            if (StringUtils.isNotEmpty(deviceInfo.getImsi()))
            {
                d.setImsi(deviceInfo.getImsi());
                // 若是没有获取到手机号，但有SIM对应的IMSI，则可以通过imsi去SIM表中关联
                SimCard simCard = simCardMapper.getSimCardByImsi(deviceInfo.getImsi());
                if (simCard != null)
                {
                    d.setTelNum(simCard.getPhone());
                    d.setProvince(simCard.getProvince());

                    simCard.setDisableStatus(2);
                    simCardMapper.updateByPrimaryKeySelective(simCard);
                }
            }
            else
            {
                // 拔掉了SIM卡
                String imsi = d.getImsi();
                if (StringUtils.isNotEmpty(imsi))
                {
                    d.setImsi(null);
                    d.setImsiSetNull("imsi");
                    //d.setTelNum(null);
                    //d.setProvince(null);

                    SimCard simCard = simCardMapper.getSimCardByImsi(imsi);
                    if (simCard != null)
                    {
                        simCard.setDisableStatus(0);
                        simCardMapper.updateByPrimaryKeySelective(simCard);
                    }
                }
            }
        }
    }

    private void setImeiToDevice(Device device, String imeiStr){
        if (StringUtils.isNotBlank(imeiStr))
        {
            List<String> imeiList = Arrays.asList(imeiStr.split(","));
            if (imeiList.size() == 1)
            {
                device.setImei1(imeiList.get(0));
            }
            else if (imeiList.size() == 2)
            {
                device.setImei1(imeiList.get(0));
                device.setImei2(imeiList.get(1));
            }
            else if (imeiList.size() == 3)
            {
                device.setImei1(imeiList.get(0));
                device.setImei2(imeiList.get(1));
                device.setMeid(imeiList.get(2));
            }
        }
    }

    @Override
    public void beartHeat(BeartHeatInfo beartHeatInfo, String proxyIpMac) {

        String[] proxy = proxyIpMac.split("_");

        // Proxy上报的在线终端机唯一序列号列表
        List<String> onlineDeviceSn = beartHeatInfo.getOnline();

        List<Device> deviceList = deviceMapper.getDeviceByProxyIpAndMac(proxy[0], proxy[1]);
        List<Integer> onlineList = new ArrayList<Integer>();
        List<Integer> offlineList = new ArrayList<Integer>();
        List<String> onImsiList = new ArrayList<>();
        List<String> offImsiList = new ArrayList<>();
        for (Device d : deviceList)
        {
            String deviceSn = d.getDeviceSn();
            Integer deviceId = d.getId();
            String onlineStatus = d.getOnlineStatus();
            String imsi = d.getImsi();
            if (onlineDeviceSn.contains(deviceSn))
            {
                if (DeviceConstant.ONLINE_STATUS_0.equals(onlineStatus))
                {
                    onlineList.add(deviceId);
                }
                if (StringUtils.isNotEmpty(imsi))
                {
                    onImsiList.add(imsi);
                }
            }
            else if (!onlineDeviceSn.contains(deviceSn))
            {
                if (DeviceConstant.ONLINE_STATUS_1.equals(onlineStatus))
                {
                    offlineList.add(deviceId);
                }
                if (StringUtils.isNotEmpty(imsi))
                {
                    offImsiList.add(imsi);
                }
            }
        }
        if (onlineList.size() > 0)
        {
            LOGGER.info("Server端有 " + onlineList.size() + " 款终端机在线状态被修改为“在线”");
            deviceMapper.updateOnlineStatusByIds(DeviceConstant.ONLINE_STATUS_1, onlineList);
        }
        if (offlineList.size() > 0)
        {
            LOGGER.info("Server端有 " + offlineList.size() + " 款终端机在线状态被修改为“离线”");
            deviceMapper.updateOnlineStatusByIds(DeviceConstant.ONLINE_STATUS_0, offlineList);
        }
        if (onImsiList.size() > 0)
        {
            SimCard simCard = new SimCard();
            simCard.setDisableStatus(2);
            simCard.setImsiList(onImsiList);
            simCardMapper.updateStatusByImsi(simCard);
        }
        if (offImsiList.size() > 0)
        {
            SimCard simCard = new SimCard();
            simCard.setDisableStatus(0);
            simCard.setImsiList(offImsiList);
            simCardMapper.updateStatusByImsi(simCard);
        }
    }

    /**
     * 当前用例执行步骤情况
     */
    @Override
    public void changeTask(CurrentExecuteCase exeCase)
    {
        /*if ("-1".equals(exeCase.getCountType()))
        {*/
            // 开始执行脚本，修改app_case_device、
            AppCaseDevice cd = new AppCaseDevice();
            // 执行状态：0-未执行，1-执行中，2-执行完成
            cd.setExecuteStatus(1);
            cd.setExecuteTime(new Date());
            cd.setCaseId(exeCase.getAppCaseId());
            cd.setDeviceSn(exeCase.getDeviceSn());

            appCaseDeviceService.updateByCaseIdAndDeviceId(cd);

        //}
    }

    @Override
    public List<AppDeviceVO> getDeviceList(AppDeviceVO deviceVO)
    {
//        if (deviceVO == null)
//        {
//            // 表示查询所有
//            deviceVO = new AppDeviceVO();
//        }
        // 在线
        List<AppDeviceVO> resultList = new ArrayList<>();
        deviceVO.setOnlineStatus(DeviceConstant.ONLINE_STATUS_1);
        List<AppDeviceVO> list = deviceMapper.getDeviceList(deviceVO);
        List<Map<String,Object>> orderStatusList = deviceVO.getOrderStatusList();
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(orderStatusList))
        {
            for (AppDeviceVO vo : list)
            {
               Integer appId = vo.getAppId();
                AppParam p = new AppParam();
                p.setAppId(appId);
                p.setAccount(vo.getTelNum());
                List<AppParam> paramList = appParamMapper.selectByExample(p);
                      vo.setAccountPwd(null);
                if (CollectionUtils.isNotEmpty(paramList))
                {
                    vo.setAccountPwd(paramList.get(0).getPassword());
                }
                boolean flag = true;

                // 取出对应的套餐业务筛选条件
                List<String> areadyProduct = new ArrayList<>();
                List<String> noProduct = new ArrayList<>();
                for (Map<String,Object> map : orderStatusList)
                {
                    if (map.size() > 0)
                    {
                        // 0-未办理  1-已办理
                        Integer orderStatus = (Integer) map.get("orderStatus");
                        String product = (String) map.get("product");
                        if (orderStatus == 1)
                        {
                            areadyProduct.add(product);
                        }
                        else
                        {
                            noProduct.add(product);
                        }
                    }
                }

                List<Product> productList = vo.getProductList();
                if (CollectionUtils.isNotEmpty(productList))
                {
                    // 取出该手机号码中已办理的套餐业务
                    List<String> proNameList = new ArrayList<>();
                    for (Product d : productList)
                    {
                        proNameList.add(d.getName());
                    }
                    // 过滤
                    if (CollectionUtils.isNotEmpty(areadyProduct) && !proNameList.containsAll(areadyProduct))
                    {
                        flag = false;
                    }
                    if (CollectionUtils.isNotEmpty(noProduct) && proNameList.containsAll(noProduct))
                    {
                        flag = false;
                    }
                }
                else
                {
                    //该手机号未办理任何套餐
                    if (CollectionUtils.isNotEmpty(areadyProduct))
                    {
                        flag = false;
                    }
                }
                if (flag)
                {
                    resultList.add(vo);
                }
            }
        }
        else if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isEmpty(orderStatusList))
        {
            for (AppDeviceVO d : list)
        {
            // 获取对应应用的账号密码
            Integer appId = d.getAppId();
            AppParam p = new AppParam();
            p.setAppId(appId);
            p.setAccount(d.getTelNum());
            List<AppParam> paramList = appParamMapper.selectByExample(p);
            d.setAccountPwd(null);
            if (CollectionUtils.isNotEmpty(paramList))
            {
                d.setAccountPwd(paramList.get(0).getPassword());
            }
           resultList.add(d);
        }
           //resultList.addAll(list);
        }

     // List<AppDeviceVO> list1 = deviceMapper.getDeviceList(deviceVO);
//        for (AppDeviceVO d : list)
//        {
//            // 获取对应应用的账号密码
//            Integer appId = d.getAppId();
//            AppParam p = new AppParam();
//            p.setAppId(appId);
//            p.setAccount(d.getTelNum());
//            List<AppParam> paramList = appParamMapper.selectByExample(p);
//            if (CollectionUtils.isNotEmpty(paramList))
//            {
//                d.setAccountPwd(paramList.get(0).getPassword());
//            }
//        }

        return resultList;
    }

    @Override
    public Device getDeviceByDeviceSn(String deviceSn) {
        return deviceMapper.getDeviceByDeviceSn(deviceSn);
    }

    @Override
    public List<Integer> getDeviceIdByProxyAndCaseId(Integer appCaseId, String proxyIp, String proxyMac) {
        return deviceMapper.getDeviceIdByProxyAndCaseId(appCaseId.toString(), proxyIp, proxyMac);
    }

    @Override
    public Map<String, Integer> getDeviceData() {
        //终端数据
        Map<String,Integer> resultMap = new HashMap<>();
        Integer SMSCatCount = simEquipmentMapper.deviceCount();//在线短信猫
        Map<String, Long> phoneCount = deviceMapper.countAppDevice(); //手机在线终端数
        Integer provinceCount = 0; //分布省份总数
        Integer freeCount = 0;//设备空闲数
        //app端设备空闲数
        freeCount +=  phoneCount.get("freeCount").intValue();
        //web端设备空闲数
        freeCount+=simEquipmentMapper.webFreeCount();

        List<String> provinceList = new ArrayList<>();
        //web端的省列表
        provinceList.addAll(simEquipmentMapper.webProvinceCount());
        // app端的省列表
        provinceList.addAll(deviceMapper.countAppDeviceProvince());

        if(provinceList != null )
        {
            HashSet<String> hashSet = new HashSet<>(provinceList);
            provinceCount =hashSet.size();
        }
        resultMap.put("SMSCatCount",SMSCatCount);
        resultMap.put("phoneCount", phoneCount.get("onLineCount").intValue());
        resultMap.put("provinceCount",provinceCount);
        resultMap.put("freeCount",freeCount);
        return resultMap;
    }

    @Override
    public void saveSmsMsg(VerifySmsMsg smsMsg) {

        SimCardInfo info = deviceMapper.getSimCardAndDeviceByDeviceSn(smsMsg.getDeviceSn());
        if (null != info)
        {
            Message msg = new Message();
            msg.setContent(smsMsg.getContent());
            msg.setType(1);
            msg.setOthernumber(smsMsg.getNumber());
            msg.setReceivedevicetype("1");
            msg.setReceivetime(new Date());
            msg.setImsi(info.getImsi());
            msg.setPhone(info.getTelNum());

            messageService.insertSelective(msg);
        }
    }
}
