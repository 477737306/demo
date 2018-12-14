package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.app.AppApk;
import com.cmit.testing.entity.app.AppParam;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.app.AppService;
import com.cmit.testing.utils.*;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.web.BaseController;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/8/31.
 */
@RestController
@RequestMapping("/api/v1/app")
//@Permission
public class AppController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    AppService appService;

    @RequestMapping(value = "/uploadApp", method = RequestMethod.POST)
    public JsonResultUtil uploadApp(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            return new JsonResultUtil(300, "请上传文件！");

        } else {
            String dateStr = String.valueOf(System.currentTimeMillis());
            //String temPath = "D:\\Test\\upload\\app\\";  //本地测试路径
            String temPath = ConstantUtil.APK_TEMP_DIR;  //本地临时路径
            File temFile = new File(temPath);
            try {
                // 如果文件夹不存在则创建
                if (!temFile.exists() && !temFile.isDirectory()) {
                    boolean isMk = temFile.mkdirs();
                    if (!isMk)
                    {
                        LOGGER.info("临时目录新建失败：" + isMk);
                    }
                }
                file.transferTo(new File(temPath + dateStr + ".apk"));
                AppApk appApk = appService.uploadApp(temPath + dateStr + ".apk");
                return new JsonResultUtil(200, "上传应用成功!", appApk);
            } catch (IOException e) {
                return new JsonResultUtil(300, "上传文件失败！");
            }
        }
    }


    @RequestMapping(value = "/listApp", method = RequestMethod.GET)
    public JsonResultUtil listApp(PageBean<AppApk> pageBean, AppApk appApk) {
        return new JsonResultUtil(200, "应用列表查询成功！", appService.findPageDevice(pageBean, appApk));
    }

    @RequestMapping(value = "/batchDelApp", method = RequestMethod.DELETE)
    public JsonResultUtil batchDeleteApp(@RequestBody Map<String,String> delIds) {
        String delIdsStr = delIds.get("delIdsStr");
        if (StringUtils.isNotEmpty(delIdsStr)) {
            String[] delIdsArr = delIdsStr.split(";");
            String[] list = delIdsArr;
            for (String id : list) {
                AppApk appApk = appService.selectByPrimaryKey(Integer.parseInt(id));
                String apkUrl = appApk.getApkUrl();
                //先删除fastdfs上的apk文件
                if (StringUtils.isNotEmpty(apkUrl)) {
                    FolderUtils.deleteFile(apkUrl);
                }
                appService.deleteByPrimaryKey(Integer.parseInt(id));
            }
            return new JsonResultUtil(200, "操作成功！");
        } else {
            return new JsonResultUtil(300, "请选择要删除的应用！");
        }
    }

    /**
     * 查询应用对应的参数列表
     *
     * @param appParam
     * @return
     */
    @RequestMapping(value = "/queryAppParam", method = RequestMethod.GET)
    public JsonResultUtil queryAppParam(PageBean<AppParam> pageBean, AppParam appParam) {
        if (appParam != null) {
            return new JsonResultUtil(200, "应用参数查询成功!", appService.findPageParams(pageBean, appParam));
        } else {
            return new JsonResultUtil(300, "请先选择应用！");
        }
    }

    /**
     * 导出应用参数模板
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportParamExcel", method = RequestMethod.GET)
    public JsonResultUtil exportParamsTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String destFileName = "应用参数模板";
            String templateFileName = ConstantUtil.APP_PARAMS_TEMPLATE_PATH;
            ExcelJXLSUtil<AppParam> excelJXLSUtil = new ExcelJXLSUtil<>();
            excelJXLSUtil.export(response, destFileName, templateFileName, request);
            return new JsonResultUtil(200, "下载模板成功!");
        } catch (Exception e) {
            return new JsonResultUtil(300, "导入应用参数模板失败!");
        }
    }

    @RequestMapping(value = "/importParamsExcel", method = RequestMethod.POST)
    public JsonResultUtil importExcel(@RequestParam("file") MultipartFile file, Integer appId) {
        try {
            if (file != null && file.getName() != null && file.getSize() > 0) {
                //获取文件名
                String fileName = file.getOriginalFilename();
                //本地临时存放路径
                //String temPath ="D://Test//APP//";
                String temPath = ConstantUtil.APP_PARAMS_IMPORT_TEM_PATH;
                File temFile = new File(temPath);
                // 如果文件夹不存在则创建
                if (!temFile.exists() && !temFile.isDirectory()) {
                    temFile.mkdirs();
                }
                file.transferTo(new File(temPath + fileName));
                ExcelJXLSUtil<AppParam> excelJXLSUtil = new ExcelJXLSUtil<>();
                List<AppParam> paramsList =  excelJXLSUtil.importEcle(temPath + fileName, ConstantUtil.APP_PARAMS_CONFIG_PATH);
                FolderUtils.deleteFile(temPath + fileName);  //删除excel文件
                Map<String,Object> resultMap = new HashMap<>();
                boolean repeat = false;
                if (paramsList.size()>1) {
                    labe:for (int i = 0; i < paramsList.size()-1; i++) {
                        AppParam a1 = paramsList.get(i);
                       for (int j=1+i;j<paramsList.size();j++){
                           AppParam a2 = paramsList.get(j);
                           if (a1.getAccount().equals(a2.getAccount())) {
                               repeat = true;
                               break labe;
                           }
                       }
                    }
                }
                if (!repeat) {
                    resultMap = appService.addParams(paramsList, appId);
                } else{
                    resultMap.put("isSuccess",false);
                    resultMap.put("message","导入的账号有重复，请重新导入！");
                }

                boolean isSuccesss = (boolean) resultMap.get("isSuccess");
                if (isSuccesss) {
                    return new JsonResultUtil(200, "导入Excel文件成功!",paramsList);
                } else {
                    return new JsonResultUtil(300, resultMap.get("message").toString());
                }
            } else {
                return new JsonResultUtil(300, "请导入Excel文件!");
            }

        } catch (Exception e) {
            return new JsonResultUtil(300, "导入应用参数Excel文件出错!");
        }
    }

    /**
     * 确认导入应用参数Excel文件
     *
     * @return
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public JsonResultUtil confirmImport(Integer appId) {
        try {
            //删除当前应用已经存在的应用参数 (isDel为0)
            if (appId != null) {
                AppParam example = new AppParam();
                example.setAppId(appId);
                example.setIsDel("0");
                appService.paramsImportConfirm(example);
                return new JsonResultUtil(200, "上传应用参数成功！");
            } else {
                return new JsonResultUtil(300, "请先选择应用！");
            }

        } catch (Exception e) {
            return new JsonResultUtil(300, "确认上传出错，请稍后重试！");
        }

    }

    /**
     * 取消导入应用参数Excel文件
     *
     * @return
     */
    @RequestMapping(value = "/cancleParams", method = RequestMethod.GET)
    public JsonResultUtil cancleImport(Integer appId) {
        //删除伪入库的数据 (isDel为1)
        try {
            AppParam example = new AppParam();
            example.setAppId(appId);
            example.setIsDel("1");
            appService.cancleParamsImport(example);
            return new JsonResultUtil(200, "取消导入成功!");
        } catch (Exception e) {
            return new JsonResultUtil(300, "取消导入出现异常，请稍后再试！");
        }
    }


}
