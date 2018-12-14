package com.cmit.testing.service;

import com.cmit.testing.entity.SysReport;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

public interface SysReportService extends BaseService<SysReport>{

    Map<String, Object> add(SysReport sysReport, Map<String, Object> map);

    PageBean<SysReport> getReportAll(PageBean<SysReport> pageBean, SysReport sysReport);

    /**
     * 条件查询所有
     * @param sysReport
     * @return
     */
    List<SysReport> getAll(SysReport sysReport);

}
