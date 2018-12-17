package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysLogMapper;
import com.cmit.testing.entity.SysLog;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志 ServiceImpl
 *
 * @author YangWanLi
 * @date 2018/8/8 17:23
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements SysLogService{

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 日志分页
     * @param pageBean
     * @param sysLog
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public PageBean<SysLog> findPage(PageBean<SysLog> pageBean, SysLog sysLog, String startDate, String endDate) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        pageBean.setItems(sysLogMapper.findPage(sysLog,startDate,endDate));
        pageBean.setTotalNum((int)page.getTotal());
        return pageBean;
    }

}
