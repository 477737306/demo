package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.vo.AppDeviceVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.app.DeviceService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/8/28.
 */
@RestController
@RequestMapping("/api/v1/device")
@Permission
public class DeviceController extends BaseController {
    @Autowired
    private DeviceService deviceService;

    /**
     * 分页查询设备信息
     */
    @RequestMapping(value = "/pageDeviceQuery",method = RequestMethod.GET)
    public JsonResultUtil pageDeviceQuery(PageBean<Device> pageBean,String brand, String sysOs, String resolution,
                                          String useStatus, String onlineStatus, String name)
    {
        Map<String, Object> paramMap = new HashMap<>();
        // 品牌
        if (StringUtils.isEmpty(brand))
        {
            paramMap.put("brandList", null);
        }
        else
        {
            paramMap.put("brandList", brand.split(";"));
        }

        // 系统版本
        if (StringUtils.isEmpty(sysOs)) {
            paramMap.put("sysOsList", null);
        } else {
            paramMap.put("sysOsList", sysOs.split(";"));
        }

        // 分辨率
        if (StringUtils.isEmpty(resolution)) {
            paramMap.put("resolutionList", null);
        } else {
            paramMap.put("resolutionList", resolution.split(";"));
        }

        // 使用状态
        if (StringUtils.isEmpty(useStatus)) {
            paramMap.put("useStatusList", null);
        } else {
            paramMap.put("useStatusList", useStatus.split(";"));
        }

        // 连接状态
        if (StringUtils.isEmpty(onlineStatus)) {
            paramMap.put("onlineStatusList", null);
        } else {
            paramMap.put("onlineStatusList", onlineStatus.split(";"));
        }

        //模糊查询条件
        if (StringUtils.isEmpty(name)) {
            paramMap.put("searchCriteria", null);
        } else {
            paramMap.put("searchCriteria", name);
        }

        return new JsonResultUtil(200,"获取成功",deviceService.findPageDevice(pageBean,paramMap));
    }

    /**
     * 查询真机列表查询条件
     * @return
     */
    @RequestMapping(value = "/queryTerms",method = RequestMethod.GET)
    public JsonResultUtil queryTerms () {
        return new JsonResultUtil(200,"查询成功",deviceService.queryTerms());
    }

    /**
     * 设备详情查看
     * @param id
     * @return
     */
    @RequestMapping(value = "/deviceDetail/{id}",method = RequestMethod.GET)
    public JsonResultUtil queryDeviceDetail(@PathVariable("id") Integer id) {
        return new JsonResultUtil(200,"设备详情查询成功！",deviceService.selectByPrimaryKey(id));
    }

    /**
     * 更新手机号码
     * @return
     */
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public JsonResultUtil updateDevice (@PathVariable("id")Integer id,@RequestBody Map<String,String> map) {
        Device device = new Device();
        device.setId(id);
        device.setTelNum(map.get("telNum"));
        int i = deviceService.alterDevice(device);
        if (i > 0) {
            return new JsonResultUtil(200,"更新成功" , i);
        } else {
            return new JsonResultUtil(300,"修改失败，请重试或联系管理员");
        }
    }

    /**
     * 新建用例-测试机列表筛选
     * @return
     */
    @RequestMapping(value = "/getAllDeviceRecord", method = RequestMethod.POST)
    public JsonResultUtil getDeviceList(@RequestBody AppDeviceVO deviceVO){
        List<AppDeviceVO> deviceVOList = deviceService.getDeviceList(deviceVO);
        List<AppDeviceVO>  device  =new ArrayList<>();
        deviceVOList.stream().forEach(
                p -> {
                    if (!device.contains(p)) {
                        device.add(p);
                    }
                }
        );

        return new JsonResultUtil(200,"操作成功" , device);
    }


}
