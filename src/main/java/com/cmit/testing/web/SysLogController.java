package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysLog;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.SysLogService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志
 * @author YangWanLi
 * @date 2018/8/8 19:42
 */
@RestController
@RequestMapping("api/v1/sysLog")
@Permission
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;


    /**
     * 日志分页
     * @param pageBean
     * @param sysLog
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(PageBean<SysLog> pageBean,SysLog sysLog,String startTime , String endTime){
        return new JsonResultUtil(sysLogService.findPage(pageBean,sysLog,startTime,endTime));
    }
}
