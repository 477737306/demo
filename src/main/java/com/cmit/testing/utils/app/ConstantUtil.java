package com.cmit.testing.utils.app;

import com.cmit.testing.utils.ConfigLocation;
import com.cmit.testing.utils.Constants;

import java.io.File;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 6:20.
 */
public class ConstantUtil {

    public static final String SCRIPT_EXEC_START_UP = "Start up";

    public static final String SCRIPT_EXEC_STEP_END_MESSAGE = "Exec step end :";

    /**
     * atap config path
     */
    public static final String ATAP_CONFIG_PROPERTIES = "/home/atap/config/atap.properties";
    //public static final String ATAP_CONFIG_PROPERTIES = "F:/workspace/Testing/atap/config/atap.properties";

    /**
     * FastDFS资源访问域名地址
     */
    public static final String FASTDFS_DOMAIN = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "atap.fastdfs.domain");
    /**
     * 临时目录 /home/template
     */
    public static final String BASE_TEMP_PATH = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "atap.temp.path");
    /**
     * 配置文件存放根路径
     */
    public static final String BASE_CONFIG_PATH = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "base.config.path");
    public static final String BASE_TOOLS_PATH = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "base.tools.path");

    public static final String SERVER_PORT = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "atap.serverPort");
    /**
     * aapt
     */
    public static final String AAPT_TOOL = PropertiesUtil.getValue(ATAP_CONFIG_PROPERTIES, "atap.apkUtil.aapt");
    /**
     * AAPT工具存放路径
     */
    public static final String AAPT_TOOL_PATH = BASE_TOOLS_PATH + AAPT_TOOL;

    /**
     * 脚本打包文件根目录 /home/template/buildtemp/
     */
    public static final String ROOT = BASE_TEMP_PATH + File.separator + "buildtemp" + File.separator;
    /**
     * 脚本临时存放目录 /home/template/scripts/
     */
    public static final String SCRIPT_TEMP_DIR = BASE_TEMP_PATH + File.separator + "scripts" + File.separator;

    /**
     * APK临时存放目录  /home/template/apk/
     */
    public static final String APK_TEMP_DIR = BASE_TEMP_PATH + File.separator + "apk" + File.separator;

    /**
     * APP下发任务时打包zip文件时使用  /home/template/task/
     */
    public static final String TASK_TEMP_URL = BASE_TEMP_PATH + File.separator + "task" + File.separator;

    public static final String APP_PARAMS_CONFIG_PATH = Constants.TEMPLATE_PATH + "xml/AppParamConfig.xml";
    /**
     * 应用参数模板路径
     */
    public static final String APP_PARAMS_TEMPLATE_PATH = "/excel/AppParamsTemplate.xlsx";

    /**
     * 应用参数excel文件临时存放路径
     */
    public static final String APP_PARAMS_IMPORT_TEM_PATH = BASE_TEMP_PATH + "paramsExcel/";
    /**
     * 编译jar包的配置文件路径
     */
    public static final String ANT_CONFIG_XML_PATH = BASE_CONFIG_PATH + "phoneauthority.xml";
    /**
     * Script.zip文件的存放路径
     */
    public static final String SCRIPT_ZIP_PATH = BASE_TOOLS_PATH + File.separator + "script" + File.separator + "script.zip";

    /**
     * ExecOperateThreadHead.txt文件路径
     */
    public static final String EXEC_OPERATE_HEAD_FILE_PATH = "ExecOperateFile" + File.separator + "ExecOperateThreadHead.txt";
    /**
     * ExecOperateThreadEnd.txt文件路径
     */
    public static final String EXEC_OPERATE_END_FILE_PATH = "ExecOperateFile" + File.separator + "ExecOperateThreadEnd.txt";
    /**
     * 脚本步骤执行的java文件存放路径
     */
    public static final String EXEC_THREAD_FILE_PATH_NAME = "src" + File.separator + "ExecOperateThread.java";
    /**
     * Script.zip解压后pics目录
     */
    public static final String PICS_PATH_NAME = "pics";
    /**
     * 编译后文件输出目录out
     */
    public static final String SCRIPT_OUT = "out";
    /**
     * 编译后文件输出的jar包文件名称
     */
    public static final String SCRIPT_NAME = "script.jar";



}
