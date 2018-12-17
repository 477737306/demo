package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @date 2018/9/29 16:54
 */
@RestController
public class PingController extends BaseController{

    @RequestMapping("ping")
    public JsonResultUtil pingIp(){
        return new JsonResultUtil(200 ,"true");
    }
}
