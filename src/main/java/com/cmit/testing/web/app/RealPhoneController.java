package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.listener.operatecmd.CmdOperationBO;
import com.cmit.testing.service.app.RealPhoneService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.web.BaseController;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/9/13.
 */
@Controller
@RequestMapping("/api/v1/realPhone")
@Permission
public class RealPhoneController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(RealPhoneController.class);
    @Autowired
    private RealPhoneService realPhoneService;

    @RequestMapping(value = "/sendCmd", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil sendCmd(CmdOperationBO cbo)
    {
        if (StringUtils.isEmptyAny(cbo.getDeviceSn(), cbo.getProxyIpMac()))
        {
            throw new TestSystemException("参数缺失");
        }

        realPhoneService.sendCmd(cbo);

        return new JsonResultUtil(200, "操作指令发送成功");
    }


    /**
     * 手机设备操控
     * @param map
     * @return
     */
    @RequestMapping(value = "/ctrlRealPhone", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil ctrlRealPhone(@RequestParam Map<String,String> map)
    {
        String deviceSn = map.get("deviceSn");
        String proxyIp = map.get("proxyIp");
        String proxyMac = map.get("proxyMac");
        if (StringUtils.isEmptyAny(deviceSn, proxyIp, proxyMac))
        {
            throw new TestSystemException("参数缺失");
        }
        realPhoneService.showRealPhone(deviceSn, proxyIp, proxyMac);
        return new JsonResultUtil(200,"操控设备成功");
    }

    /**
     * 截图，并下载到本地
     */
    @RequestMapping(value = "/downloadScreenCap", method = RequestMethod.POST)
    public void downloadScreenCap(@RequestBody CmdOperationBO cbo, HttpServletRequest request, HttpServletResponse response)
    {
        if (cbo == null)
        {
            throw new TestSystemException("参数异常");
        }
        Base64Encoder encoder = new Base64Encoder();
        String base64Str = cbo.getBase64Str();
        if (StringUtils.isEmpty(base64Str))
        {
            throw new TestSystemException("截图数据异常");
        }
        String base64 = base64Str.replace("data:image/jpg;base64,", "");
        byte[] baseMsg = encoder.decode(base64);
        try
        {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            //response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis() + ".jpg");

            response.getOutputStream().write(baseMsg);
            response.flushBuffer();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("截图文件下载异常");
        }
    }

}
