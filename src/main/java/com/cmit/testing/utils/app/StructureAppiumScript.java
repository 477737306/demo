package com.cmit.testing.utils.app;

import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.app.AppScriptStep;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.file.FileHandlerUtils;
import com.cmit.testing.utils.file.FolderUtils;
import com.cmit.testing.utils.file.ZipUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO 脚本生成算法
 * @date 2018/9/7 0007 下午 5:57.
 */
public class StructureAppiumScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructureAppiumScript.class);

    // window system need to install ant tool.
    private static final String ANT_CMD = "c:\\windows\\System32\\cmd.exe /c ant";
    private static final String LINUX_ANT_CMD="ant -s";
    private static final String ANDROID_DRIVER = "driver = new AndroidDriver(new URL(\"http://127.0.0.1:\" + deviceEntity.getPort() + \"/wd/hub\"),capabilities);";
    // 检测权限间隔时间
    private static final int CHECK_PERMISSION_PERTIME = 2000;
    private StringBuffer isCleanOptionStr = new StringBuffer();
    private StringBuffer execFileMiddleStr = new StringBuffer();
    public static final String SCRIPT_TOTAL_STEP_MSG = "Script total steps :";
    private static final int CLICK_BY_XPATH = 0;
    private static final int CLICK_BY_UIAUTOMATOR_TEXT = 1;
    private static final int LONG_CLICK_BY_XPATH = 2;
    /**
     * 打包工具 ant 注：window环境下调用 ant.bat，Linux环境下调用 ant
     */
    private static final String ANT_TOOLS = PropertiesUtil.getValue(ConstantUtil.ATAP_CONFIG_PROPERTIES, "atap.ant.tools");

    private boolean isClear;

    private Integer phoneHeight;

    private Integer phoneWidth;

    private String deviceSn;

    private Integer appCaseId;

    public StructureAppiumScript() {
        initOptions();
    }

    public StructureAppiumScript(String deviceSn, Integer appCaseId) {
        this.deviceSn = deviceSn;
        this.appCaseId = appCaseId;
        initOptions();
    }

    public StructureAppiumScript(boolean isClear, Integer phoneHeight, Integer phoneWidth) {
        this.isClear = isClear;
        this.phoneHeight = phoneHeight;
        this.phoneWidth = phoneWidth;
        initOptions();
    }

    /**
     * 初始化脚本执行的代码字符串
     */
    private void initOptions(){
        if (!isClear)
        {
            isCleanOptionStr.setLength(0);
            isCleanOptionStr.append("\n");
        }
        else
        {
            isCleanOptionStr.append("clearApkData();").append("\n");
        }

        initExecuteFileMiddleStr();

    }

    /**
     * 初始化脚本步骤执行字符串
     */
    private void initExecuteFileMiddleStr()
    {
        execFileMiddleStr.setLength(0);

        execFileMiddleStr.append("recordWidth = " + phoneWidth + ";").append("\n");
        execFileMiddleStr.append("recordHeight = " + phoneHeight + ";").append("\n\n");

        execFileMiddleStr.append("System.out.println(\"Init AndroidDriver start....\");").append("\n");
        execFileMiddleStr.append(ANDROID_DRIVER).append("\n");
        execFileMiddleStr.append("driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);").append("\n");

        // 从配置文件读取某些手机在运行app时，弹出来的权限提示框的允许/确定按钮id
        List<String> idList = SAXParser.getAuthorityIdFromXml(ConstantUtil.ANT_CONFIG_XML_PATH);

        if (idList != null && idList.size() != 0) {
            execFileMiddleStr.append("startCheckPermissionServer(" + CHECK_PERMISSION_PERTIME + ",");
            for (int i = 0; i < idList.size(); i++) {
                execFileMiddleStr.append("\"" + idList.get(i) + "\"");
                if (i != (idList.size() - 1)) {
                    execFileMiddleStr.append(",");
                }
            }
            execFileMiddleStr.append(");").append("\n");
        }
        execFileMiddleStr.append("sleep(8 * 1000);").append("\n");
        execFileMiddleStr.append("sleep(3 * 1000);").append("\n");

        // 发送脚本执行开始消息
        execFileMiddleStr.append("sendMessage(\"" + ConstantUtil.SCRIPT_EXEC_START_UP + "\");").append("\n");
    }

    /**
     * 根据脚本打成jar包，并返回jar包的路径
     */
    public String generateScriptJar(List<AppScriptStep> scriptStepList, String basePath, String picsPath){
        StringBuffer cmdList = actionToExecuteTableCode(scriptStepList);
        LOGGER.info("All Step cmd list are : \n" + cmdList);
        return buildJarByScriptStep(cmdList.toString(), scriptStepList, basePath, picsPath);
    }

    /**
     *  打包jar文件
     * @param execStepStr
     * @param scriptStepList
     * @return
     */
    private String buildJarByScriptStep(String execStepStr, List<AppScriptStep> scriptStepList, String basePath, String picsPath)
    {
        boolean compileOK = false;
        Process antProc = null;
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader successResult = null;

        String scriptId = scriptStepList.get(0).getScriptId() + "";
        // /home/template/buildtemp/74
        String scriptDirectory = ConstantUtil.ROOT + File.separator + scriptId;

        File file = new File(scriptDirectory);
        if (!file.exists())
        {
            boolean isMk = file.mkdirs();
            LOGGER.error("scriptDirectory: " + scriptDirectory + ", isMk = " + isMk);
        }
        // /home/atap/tools/script/script.zip
        File scriptZipFile = new File(ConstantUtil.SCRIPT_ZIP_PATH);

        try
        {
            // 拷贝script.zip到scriptDirectory目录下
            FileUtils.copyFileToDirectory(scriptZipFile, file);
            //  /home/template/buildtemp/74/script.zip
            String copyZipFile = scriptDirectory + File.separator + "script.zip";
            // 解压拷贝的zip文件
            ZipUtils.unzip(copyZipFile, scriptDirectory + File.separator);
            // 删除拷贝的script.zip文件
            FileUtils.forceDelete(new File(copyZipFile));
            // 解压后的文件根目录
            String destPath = scriptDirectory + File.separator + "script";

            // 读取java代码head部分和end部分 等待与execStepStr字符串组装成java代码生成java文件
            String headStr = FileUtils.readFileToString(new File(destPath + File.separator + ConstantUtil.EXEC_OPERATE_HEAD_FILE_PATH), "UTF-8");
            String endStr = FileUtils.readFileToString(new File(destPath + File.separator + ConstantUtil.EXEC_OPERATE_END_FILE_PATH), "UTF-8");

            // 该字符串组成结构：headStr + isCleanOptionStr + execFileMiddleStr + execStepStr + endStr
            StringBuffer executeFileStr = new StringBuffer();
            executeFileStr.append(headStr).append("\n");
            executeFileStr.append(isCleanOptionStr).append("\n");
            executeFileStr.append(execFileMiddleStr).append("\n");
            executeFileStr.append(execStepStr).append("\n");
            executeFileStr.append(endStr);
            // 删除 ExecOperateThread.java 文件,然后生成新的java文件
            String javaFilePath = destPath + File.separator + ConstantUtil.EXEC_THREAD_FILE_PATH_NAME;
            FolderUtils.deleteFile(javaFilePath);
            File javaFile = new File(javaFilePath);
            if (!javaFile.exists())
            {
                javaFile.createNewFile();
            }
            // 将字符串写入文件中
            FileUtils.writeStringToFile(javaFile, executeFileStr.toString());
            // 将脚本中的pics目录拷贝到 destPath目录下
            String destPicsPath = destPath + File.separator + ConstantUtil.PICS_PATH_NAME;
            File picsDir = new File(picsPath);
            File destPicsDir = new File(destPicsPath);
            if (picsDir.exists())
            {
                FileUtils.copyDirectory(picsDir, destPicsDir);
            }

            // 以上所有准备操作完成之后 开始执行ant打包jar文件
            //LOGGER.error("ANT_TOOLS:" + ANT_TOOLS);
            antProc = Runtime.getRuntime().exec(ANT_TOOLS, null, new File(destPath));
            in = antProc.getInputStream();
            inReader = new InputStreamReader(in, "UTF-8");
            successResult = new BufferedReader(inReader);
            String s;
            while ((s = successResult.readLine()) != null)
            {
                LOGGER.info(s);
                if (s.contains("BUILD SUCCESSFUL"))
                {
                    compileOK = true;
                }
            }

            if (compileOK)
            {
                // 编译成功
                // 编译完成生成一个jar文件
                String destJarPath = destPath + File.separator + ConstantUtil.SCRIPT_OUT + File.separator + ConstantUtil.SCRIPT_NAME;
                // 将该文件拷贝到basePath目录下，然后删除编译过程中生成的文件
                File generateJarFile = new File(destJarPath);
                String tempDir = basePath + File.separator + scriptId;
                File tempFileDir = new File(tempDir);
                if (!tempFileDir.exists())
                {
                    tempFileDir.mkdirs();
                }
                FileUtils.copyFileToDirectory(generateJarFile, tempFileDir);
                FolderUtils.deleteDirectory(scriptDirectory);
                return tempDir + File.separator + ConstantUtil.SCRIPT_NAME;
            }
            else
            {
                //FolderUtils.deleteDirectory(scriptDirectory);
                LOGGER.error("Build Script Jar File Fail.");
                return null;
            }


        }
        catch (Exception e) {
            //FolderUtils.deleteDirectory(scriptDirectory);
            LOGGER.error("Build Script Jar File Error : ", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(successResult);
            IOUtils.closeQuietly(inReader);
            IOUtils.closeQuietly(in);
            if (antProc != null)
            {
                try {
                    antProc.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将步骤相关的信息拼接成字符串
     */
    private StringBuffer actionToExecuteTableCode(List<AppScriptStep> scriptStepList){
        StringBuffer execCodeList = new StringBuffer();

        // add screenshot when begin.
        //execCodeList.append("saveScreenShot();").append("\n");
        // show script total steps
        execCodeList.append("System.out.println(\"" + SCRIPT_TOTAL_STEP_MSG + "\" + " + scriptStepList.size() + ");").append("\n");

        int i = 0;
        for (AppScriptStep scriptStep : scriptStepList)
        {
            LOGGER.info(" Step is : " + scriptStep);

            i++;

            String[] params = null;
            //坐标
            if (StringUtils.isNotEmpty(scriptStep.getCoordinate()))
            {
                params = scriptStep.getCoordinate().split(",");
            }
            //动作类型
            String actionType = scriptStep.getActionType();
            String execCodeStr = "";
            if (!AppConstants.SCRIPT_STEP_SLEEP.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_SCREENSHOT.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_AFFAIR.equals(actionType))
            {
                // sleep 5 second after execute every step.
                execCodeStr += " \n sleep(5 * 1000); \n";
            }
            if (!AppConstants.SCRIPT_STEP_SLEEP.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_IFJUDGE.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_FORLOOP.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_SCREENSHOT.equals(actionType)
                    && !AppConstants.SCRIPT_STEP_SMSVARIFY.equals(actionType))
            {
                execCodeStr += "		saveScreenShot(" + i + "); \n";
            }


            //i++;
            //execCodeStr += "		sendMessage(\"" + i + " : " + actionType + "\"); \n";
            execCodeStr += "		sendMessage(\"" + i + " : " + actionType + " : " + scriptStep.getStepDesc() + "\"); \n";
            // 根据动作类型 分别拼接execCodeStr字符串
            switch (actionType)
            {
                // 点击
                case AppConstants.SCRIPT_STEP_CLICK:
                    if (StringUtils.isNotEmpty(scriptStep.getXpath()))
                    {
                        // Click This widget
                        execCodeStr += "clickByXpath(\"" + scriptStep.getXpath() + "\", " + CLICK_BY_XPATH + "); \n";
                        // Add the script logs: logStirng for store in script.log
                        execCodeStr += "logStirng += \" Xpath: " + scriptStep.getXpath() + " is Clicked! \\n \";";
                    }
                    break;
                // 双击
                case AppConstants.SCRIPT_STEP_DOUBLECLICK:
                    if (params == null)
                    {
                        // double click this widget
                        execCodeStr += "clickByXpath(\"" + scriptStep.getXpath() + "\", " + CLICK_BY_XPATH + "); \n";
                        execCodeStr += "clickByXpath(\"" + scriptStep.getXpath() + "\", " + CLICK_BY_XPATH + "); \n";
                        execCodeStr += "logStirng += \" Xpath: " + scriptStep.getXpath() + " is Double Clicked! \\n \";";
                    }
                    else if (params != null)
                    {
                        execCodeStr += "driver.tap(1, " + params[0] + ", " + params[1] + ", 200);\n";
                        execCodeStr += "driver.tap(1, " + params[0] + ", " + params[1] + ", 200);\n";
                        execCodeStr += "logStirng += \" X: " + params[0] + " Y: " + params[1] + " is Double Clicked! \\n \";";
                    }
                    break;
                // 长按
                case AppConstants.SCRIPT_STEP_LONGPRESS:
                    if (StringUtils.isNotEmpty(scriptStep.getXpath()))
                    {
                        // double click this widget
                        execCodeStr += "clickByXpath(\"" + scriptStep.getXpath() + "\", " + LONG_CLICK_BY_XPATH + "); \n";
                        execCodeStr += "logStirng += \" XPath: " + scriptStep.getXpath() + " is LongPressed! \\n \";";
                    }
                    else if(params != null)
                    {
                        execCodeStr += "driver.tap(1, " + params[0] + ", " + params[1] + ", 1500);\n";
                        execCodeStr += "logStirng += \" X: " + params[0] + " Y: " + params[1] + " is Long Clicked! \\n \";";
                    }
                    break;
                // 点击坐标
                case AppConstants.SCRIPT_STEP_TAP:
                    if (params != null && StringUtils.isNotEmpty(params[0]) && StringUtils.isNotEmpty(params[1]))
                    {
                        execCodeStr += "clickByPoint(" + params[0] + ", " + params[1] + "); \n";
                        execCodeStr += "logStirng += \" X: " + params[0] + " Y: " + params[1] + " is Tapped! \\n \";";
                    }
                    else if (params == null || StringUtils.isNotEmpty(params[0]))
                    {
                        execCodeStr += "clickByXpath(\"" + scriptStep.getActionText() + "\", " + CLICK_BY_UIAUTOMATOR_TEXT + "); \n";
                        execCodeStr += "logStirng += \" Text: " + scriptStep.getActionText() + " is Clicked!  \\n \";";
                    }
                    break;
                // 图片识别 点击图片按钮
                case AppConstants.SCRIPT_STEP_PICTURETAP:
                    if (StringUtils.isNotEmpty(scriptStep.getPicName()))
                    {
                        execCodeStr += "clickByPic(" + params[0] + ", " + params[1] + ", " + scriptStep.getPicName() + "); \n";
                        execCodeStr += "logStirng += \" StartX:" + params[0] + " StartY:" + params[1] + " picNum:" + scriptStep.getPicName() + " is Tapped! \\n \";";
                    }
                    break;
                // 上滑
                case AppConstants.SCRIPT_STEP_UPGLIDE:
                    execCodeStr += "driver.swipe(width/2, height*3/4, width/2, height/4, 500); \n";
                    execCodeStr += "logStirng += \" Swipe Up! \\n \";";
                    break;
                // 下滑
                case AppConstants.SCRIPT_STEP_DOWNGLIDE:
                    execCodeStr += "driver.swipe(width/2, height/4, width/2, height*3/4, 500); \n";
                    execCodeStr += "logStirng += \" Swipe Down! \\n \";";
                    break;
                // 右滑
                case AppConstants.SCRIPT_STEP_RIGHTGLIDE:
                    execCodeStr += "driver.swipe(width*5/6, height/2, width/6, height/2, 500); \n";
                    execCodeStr += "logStirng += \" Swipe Right! \\n \";";
                    break;
                // 左滑
                case AppConstants.SCRIPT_STEP_LEFTGLIDE:
                    execCodeStr += "driver.swipe(width/6, height/2, width*5/6, height/2, 500); \n";
                    execCodeStr += "logStirng += \" Swipe Left! \\n \";";
                    break;
                // 输入文本
                case AppConstants.SCRIPT_STEP_SENDTEXT:
                    if (StringUtils.isNotEmpty(scriptStep.getInputValue()))
                    {
                        execCodeStr += "inputText(\"" + scriptStep.getXpath() + "\", \"" + scriptStep.getInputValue() + "\", " + scriptStep.getStepCode() + "); \n";
                        execCodeStr += "logStirng += \" Xpath : " + scriptStep.getXpath() + " is input " + scriptStep.getInputValue() + "! \\n \";";
                    }
                    break;
                // sleep
                case AppConstants.SCRIPT_STEP_SLEEP:
                    if (StringUtils.isNotEmpty(scriptStep.getInputValue()))
                    {
                        execCodeStr += "sleep(" + scriptStep.getInputValue() + "*1000); \n";
                    }
                    break;
                // 截屏
                case AppConstants.SCRIPT_STEP_SCREENSHOT:
                    execCodeStr += "\n saveScreenShot(" + i + "); \n";
                    break;
                // if judge 判断
                case AppConstants.SCRIPT_STEP_IFJUDGE:
                    if (StringUtils.isNotEmpty(scriptStep.getActionText()))
                    {
                        if (AppConstants.SCRIPT_CMD_START.equals(scriptStep.getActionText()))
                        {
                            if (StringUtils.isNotEmpty(scriptStep.getInputValue()))
                            {

                                execCodeStr += "        boolean flag =  driver.getPageSource().contains(\"" + scriptStep.getInputValue() + "\"); \n ";
                                execCodeStr += "		System.out.println(\"" + ConstantUtil.SCRIPT_EXEC_STEP_END_MESSAGE +  i + "\"); \n";
                                execCodeStr += "		if (flag) { \n";
                                //execCodeStr += " if (driver.getPageSource().contains(\"" + scriptStep.getInputValue() + "\")) { \n";
                                execCodeStr += "            logStirng += \" If Judge content: " + scriptStep.getInputValue() + " is existed begin! \\n \";";
                            }

                        }
                        else if (AppConstants.SCRIPT_CMD_END.equals(scriptStep.getActionText()))
                        {
                            execCodeStr += "		System.out.println(\"" + ConstantUtil.SCRIPT_EXEC_STEP_END_MESSAGE +  i + "\"); \n";
                            execCodeStr += "} \n";
                            execCodeStr += "logStirng += \"If Judge end! \\n \";";
                        }
                    }
                    break;
                // for loop 循环
                case AppConstants.SCRIPT_STEP_FORLOOP:
                    if (StringUtils.isNotEmpty(scriptStep.getActionText()))
                    {
                        if (AppConstants.SCRIPT_CMD_START.equals(scriptStep.getActionText()))
                        {
                            if (StringUtils.isNotEmpty(scriptStep.getInputValue()))
                            {
                                execCodeStr += " for (int i = 0; i < " + scriptStep.getInputValue() + "; i++) { \n";
                                execCodeStr += "logStirng += \" For Loop Times: " + scriptStep.getInputValue() + " begin! \\n \";";
                            }
                        }
                        else if (AppConstants.SCRIPT_CMD_END.equals(scriptStep.getActionText()))
                        {
                            execCodeStr += "} \n";
                            execCodeStr += "logStirng += \"For Loop end! \\n \";";
                        }
                    }
                    break;
                // back 返回键
                case AppConstants.SCRIPT_STEP_BACK:
                    execCodeStr += "driver.pressKeyCode(AndroidKeyCode.BACK); \n";
                    execCodeStr += "logStirng += \" Back Key Press! \\n \";";
                    break;
                // 事务
                case AppConstants.SCRIPT_STEP_AFFAIR:
                    if (StringUtils.isNotEmpty(scriptStep.getActionText()))
                    {
                        if (AppConstants.SCRIPT_CMD_START.equals(scriptStep.getActionText()))
                        {
                            execCodeStr += " initStartTime(); \n";
                        }
                        else if (AppConstants.SCRIPT_CMD_END.equals(scriptStep.getActionText()))
                        {
                            if (StringUtils.isNotEmpty(scriptStep.getInputValue()))
                            {
                                execCodeStr += "findElementByXpath(\"" + scriptStep.getInputValue() + "\"); \n";
                            }
                        }
                    }
                    break;
                // 动态验证码
                case AppConstants.SCRIPT_STEP_DYNAMIC:
                    execCodeStr += "		inputDynamicText(\"" + scriptStep.getXpath() + "\", \"" + scriptStep.getInputValue() + "\", " + scriptStep.getStepCode() + "); \n";
                    execCodeStr += "		logStirng += \" XPath: " + scriptStep.getXpath() + " is input \" " + " + dynamicPassword + " + " \" !!!  \\n \";";
                    break;
                // 短信校验
                case AppConstants.SCRIPT_STEP_SMSVARIFY:
                    execCodeStr += "logStirng += \"sms verify: " + scriptStep.getInputValue() + "\";";
                    break;
                // 内容校验
                case AppConstants.SCRIPT_STEP_VERIFY:
                    execCodeStr += "verifyText(\"" + scriptStep.getInputValue() + "\");\n";
                    execCodeStr += "logStirng += \"verify: " + scriptStep.getInputValue() + "\";";
                    break;
                default:
                    LOGGER.error("无匹配的项: " + actionType);
                    break;
            }
            execCodeStr += "\n";
            if (!AppConstants.SCRIPT_STEP_IFJUDGE.equalsIgnoreCase(actionType))
            {
                execCodeStr += "		System.out.println(\"" + ConstantUtil.SCRIPT_EXEC_STEP_END_MESSAGE +  i + "\"); \n";
            }
            execCodeList.append(execCodeStr);
        }

        return execCodeList;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public Integer getPhoneHeight() {
        return phoneHeight;
    }

    public void setPhoneHeight(Integer phoneHeight) {
        this.phoneHeight = phoneHeight;
    }

    public Integer getPhoneWidth() {
        return phoneWidth;
    }

    public void setPhoneWidth(Integer phoneWidth) {
        this.phoneWidth = phoneWidth;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
    }

}
