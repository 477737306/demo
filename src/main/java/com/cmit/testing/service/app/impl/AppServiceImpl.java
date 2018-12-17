package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppApkMapper;
import com.cmit.testing.dao.app.AppParamMapper;
import com.cmit.testing.entity.app.ApkInfo;
import com.cmit.testing.entity.app.AppApk;
import com.cmit.testing.entity.app.AppParam;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.app.AppService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.Md5Utils;
import com.cmit.testing.utils.apk.ApkUtils;
import com.cmit.testing.utils.apk.IconUtils;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.file.FolderUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zhangxiaofang on 2018/9/4.
 */
@Service("appService")
public class AppServiceImpl extends BaseServiceImpl<AppApk> implements AppService{
    @Autowired
    FileStorageOperate fileStorageOperate;

    @Autowired
    AppApkMapper appApkMapper;

    @Autowired
    AppParamMapper appParamMapper;

    @Override
    public AppApk getApp(AppApk apk) {
        return appApkMapper.getApp(apk);
    }

    @Override
    public AppApk uploadApp(String apkPath) throws Exception{
        ApkUtils apkUtils = new ApkUtils();
        //apkUtils.setmAaptPath("D:\\aapt\\"+"aapt");
        apkUtils.setmAaptPath(ConstantUtil.AAPT_TOOL_PATH);
        AppApk appApk = new AppApk();
        ApkInfo apkInfo = new ApkInfo();
        try {
            File apkFile = new File(apkPath);
            String md5Code = Md5Utils.getFileMD5Str(apkPath);
            apkInfo = apkUtils.getApkInfo(apkPath);
            if (apkInfo != null) {
                String iconUrl = apkFile.getParent() + "/" + apkInfo.getPackageName() + ".png";
                // 获取Icon并保存到指定位置
                IconUtils.extractFileFromApk(apkPath, apkInfo.getApplicationIcon(), iconUrl);
                String fastDfsIconUrl = fileStorageOperate.fileStorageUploadFile(iconUrl);
                FolderUtils.deleteFile(iconUrl);
                String apkUrl = fileStorageOperate.fileStorageUploadFile(apkPath);
                long size = apkFile.length();
                String strSize = new BigDecimal((double) size).divide(new BigDecimal(1024 * 1024), 1, BigDecimal.ROUND_HALF_UP)
                        .toString() + "M";
                FolderUtils.deleteFile(apkPath);
                appApk.setApkName(apkInfo.getApplicationLable());
                appApk.setApkVersion(apkInfo.getVersionName());
                appApk.setPkgName(apkInfo.getPackageName());
                appApk.setApkType(0);
                appApk.setIconPath(fastDfsIconUrl);
                appApk.setApkUrl(apkUrl);
                appApk.setApkSize(strSize);
                ShiroUser shiroUser = ShiroKit.getUser();
                appApk.setCreatePerson(shiroUser.getId());
                appApk.setCreateTime(new Date());
                appApk.setUpdatePerson(shiroUser.getId());
                appApk.setUpdateTime(new Date());
                appApk.setMd5Code(md5Code);
                addApp(appApk);

            }
        } catch (Exception e) {
            FolderUtils.deleteFile(apkPath);
            throw new Exception("apk解析失败!",e);
        }
        return appApk;
    }


    @Override
    public PageBean<AppApk> findPageDevice(PageBean<AppApk> pageBean, AppApk appApk) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<AppApk> list = appApkMapper.selectByExample(appApk);
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public Integer getAppIdByInfo(String appName, String appVersion, String pkgName) {
        Integer appId = null;
        AppApk appApk = appApkMapper.getApkIdByAppInfo(appName, appVersion, pkgName);
        if (appApk != null)
        {
            appId = appApk.getId();
        }
        return appId;
    }

    @Override
    public void addApp(AppApk appApk) {
        AppApk para = new AppApk();
        para.setPkgName(appApk.getPkgName());
        para.setApkVersion(appApk.getApkVersion());
        para.setApkType(appApk.getApkType());
        List<AppApk> appApks = appApkMapper.selectByExample(para);
        // 存在相同版本的应用
        if (appApks != null && appApks.size() > 0) {
            appApk.setId(appApks.get(0).getId());
            appApk.setCreatePerson(null);
            appApk.setCreateTime(null);
            appApkMapper.updateByPrimaryKeySelective(appApk);
        }
        else
        {
            appApkMapper.insertSelective(appApk);
        }

    }

    @Override
    public List<AppParam> queryAppParam(AppParam appParam) {
        List<AppParam> paramsList = appParamMapper.selectByExample(appParam);
        return paramsList;
    }

    @Override
    public PageBean<AppParam> findPageParams(PageBean<AppParam> pageBean, AppParam appParam) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<AppParam> paramsList = appParamMapper.selectByExample(appParam);
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(paramsList);
        return pageBean;
    }

   /* @Override
    public boolean bacthImport(String fileName, MultipartFile file ,Integer appId) {
        boolean isSuccess = false;
        //创建要处理的Excel
        ReadExcel readExcel = new ReadExcel();
        List<AppParam> paramsList = readExcel.getExcelInfo(fileName,file);
        if (paramsList != null && paramsList.size() >0 ) {
            isSuccess = true;
            for (AppParam param : paramsList) {
                param.setAppId(appId);
                param.setIsDel("1");   //伪入库标志
                appParamMapper.insertSelective(param);
            }
        }
        return isSuccess;
    }*/

    @Override
    public Map<String,Object> addParams(List<AppParam> paramsList , Integer appId) {
        Map<String,Object> resultMap = new HashMap<>();
        boolean isSuccess = false;
        String message = "";
        if (paramsList != null && paramsList.size() >0 ) {
            isSuccess = true;
            for (AppParam param : paramsList) {
                String account = param.getAccount();
                String regExp = "^[1]([3][0-9]{1}|59|58|88|89|57)[0-9]{8}$";
                Pattern p = Pattern.compile(regExp);
                Matcher m = p.matcher(account);
                if (!m.find()) {  //手机号码格式不匹配
                    isSuccess = false;
                    message = "手机号码格式不匹配，请输入正确的手机号码!";

                } else {
                    param.setAppId(appId);
                    param.setIsDel("1");   //伪入库标志
                    appParamMapper.insertSelective(param);
                    isSuccess = true;
                    message = "应用参数添加成功!";
                }
            }
            resultMap.put("isSuccess",isSuccess);
            resultMap.put("message",message);
        }
        return resultMap;
    }

    /**
     * 确定导入应用参数
     * @param appParam
     */
    @Override
    public void paramsImportConfirm(AppParam appParam) {
        //删除当前应用已经存在的应用参数 isDel为0，并将伪入库的数据状态由1改为0
        appParamMapper.deleteByExam(appParam);
        AppParam updateParam = new AppParam();
        updateParam.setAppId(appParam.getAppId());
        updateParam.setIsDel("0");
        appParamMapper.updateByExample(updateParam);
    }

    /**
     * 取消导入excel中的应用参数
     * @param appParam
     */
    @Override
    public void cancleParamsImport(AppParam appParam) {
        appParamMapper.deleteByExam(appParam);
    }
}
