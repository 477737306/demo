package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppApk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppApkMapper extends BaseMapper<AppApk>{
    int deleteByPrimaryKey(Integer apkId);
    int insert(AppApk record);
    int insertSelective(AppApk record);
    AppApk selectByPrimaryKey(Integer apkId);
    int updateByPrimaryKeySelective(AppApk record);
    int updateByPrimaryKey(AppApk record);
    List<AppApk> selectByExample(AppApk param);

    AppApk getApp(AppApk apk);

    /**
     * 查询应用列表
     * @param app 查询条件
     * @return
     */
    List<AppApk> getListByAppInfo(AppApk app);

    /**
     * 根据相关信息查询应用的ID
     * @param appName
     */
    AppApk getApkIdByAppInfo(@Param("appName") String appName, @Param("appVersion") String appVersion, @Param("pkgName") String pkgName);
}