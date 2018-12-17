package com.cmit.testing.service;

import com.cmit.testing.entity.SysLog;
import com.cmit.testing.page.PageBean;

/**
 * 系统日志 Service
 *
 * @author YangWanLi
 * @date 2018/8/8 17:22
 */
public interface SysLogService extends BaseService<SysLog> {


    /**
     * 日志分页
     * @param pageBean
     * @param sysLog
     * @param startDate
     * @param endDate
     * @return
     */
    PageBean<SysLog> findPage(PageBean<SysLog> pageBean, SysLog sysLog, String startDate, String endDate);
}
