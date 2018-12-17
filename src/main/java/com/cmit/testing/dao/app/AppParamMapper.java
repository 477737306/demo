package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppParam;
import com.cmit.testing.entity.proxy.DeviceParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/9/14.
 */
@Mapper
public interface AppParamMapper{
    /**
     * 根据应用ID、是否展现标记查询应用参数
     * @param appParam
     * @return
     */
    List<AppParam> selectByExample(AppParam appParam);

    int insertSelective(AppParam param);

    int deleteByExam(AppParam exam);

    int updateByExample(AppParam exam);

    /**
     * 获取应用关联的参数
     */
    List<DeviceParam> getAppParamByMap(@Param("appId") Integer appId, @Param("deviceSnList") List<String> deviceSnList);

    List<DeviceParam> getAppParamByMap1(@Param("appId") Integer appId, @Param("deviceIdList") List<Integer> deviceIdList);

}
