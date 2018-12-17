package com.cmit.testing.dao;

import com.cmit.testing.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog>{
    /**
     * 日志分页
     * @param sysLog
     * @param startDate
     * @param endDate
     * @return
     */
    List<SysLog> findPage(@Param("sysLog") SysLog sysLog, @Param("startDate") String startDate, @Param("endDate") String endDate);
}