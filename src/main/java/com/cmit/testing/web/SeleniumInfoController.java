package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.verify.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取selenium终端信息
 *
 * @author YangWanLi
 * @date 2018/9/5 21:53
 */
@RestController
@RequestMapping("/api/v1/seleniumInfo")
@Permission
public class SeleniumInfoController extends BaseController{



    /**
     *
     * @return
     */
    @RequestMapping(value = "/getInfo",method = RequestMethod.GET)
    public JsonResultUtil getSeleniumInfo(){
        HttpResponse response = null;
        String message ="";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        querys.put("config","true");
        querys.put("configDebug","true");

        try {
            response	= HttpUtils.doGet("http://192.168.41.68:4444","/grid/console",headers,querys);
            HttpEntity entity = response.getEntity();
            message = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResultUtil(200,"操作成功",message);
    }
}
