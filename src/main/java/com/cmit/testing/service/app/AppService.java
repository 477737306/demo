package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppApk;
import com.cmit.testing.entity.app.AppParam;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/9/4.
 */

public interface AppService extends BaseService<AppApk> {

    AppApk getApp(AppApk apk);

    AppApk uploadApp(String apkPath)  throws Exception;

    void addApp(AppApk appApk) throws Exception;

    /**
     * 根据条件查找设备信息列表
     */
    PageBean<AppApk> findPageDevice(PageBean<AppApk> pageBean, AppApk appApk) ;

    PageBean<AppParam> findPageParams(PageBean<AppParam> pageBean, AppParam appParam);

    List<AppParam> queryAppParam(AppParam appParam);


    Map<String,Object> addParams(List<AppParam> paramsList, Integer appId);


    /**
     * 确认导入应用参数
     * @param appParam
     */
    void paramsImportConfirm(AppParam appParam);

    /**
     *  根据相关信息查询应用的ID
     * @return 应用ID
     */
    Integer getAppIdByInfo(String appName, String appVersion, String pkgName);


    /**
     * 取消导入excel中的应用参数
     * @param appParam
     */
    void cancleParamsImport(AppParam appParam);
}
