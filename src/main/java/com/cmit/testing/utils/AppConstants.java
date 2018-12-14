package com.cmit.testing.utils;

/**
 * @author XieZuLiang
 * @description
 * @date 2018/8/30 0030 下午 6:00.
 */
public class AppConstants {

    /***Proxy与Server操作命令**/
    /**
     * proxy连接注册
     */
    public static final String CMD_TYPE_PROXY_REGISTER = "Register";

    /**
     * proxy连接注册
     */
    public static final String CMD_TYPE_REBOOT = "Reboot";

    /**
     * proxy连接注册成功
     */
    public static final String CMD_TYPE_PROXY_REGISTER_1 = "1";

    /**
     * proxy连接注册失败
     */
    public static final String CMD_TYPE_PROXY_REGISTER_2 = "2";
    /**
     * proxy连接注册相应
     */
    public static final String CMD_TYPE_PROXY_REGISTER_RESPONSE = "Response";
    /**
     * 心跳
     */
    public static final String CMD_HEART_BEAT = "HeartBeat";
    /**
     * 设备注册命令标识
     */
    public static final String CMD_TYPE_DEVICE_REGISTER = "CollectPhoneInfo";

    /**
     * ACC_TYPE 标识
     */
    public static final String CMD_ACC_TYPE = "Request";
    /**
     * 开启真机图片传输请求
     */
    public static final String CMD_START_IMAGE_TRANS = "StartImageTrans";
    /**
     *  停止真机图片传输请求
     */
    public static final String CMD_STOP_IMAGE_TRANS = "StopImageTrans";

    /**
     * 短信内容校验
     * 接收短信接口
     */
    public static final String CMD_TYPE_SMS_VERIFY = "SmsVerify";

    /**
     * 发送短信接口
     */
    public static final String CMD_TYPE_SEND_MSG = "SendSms";

    /**
     * 查询手机是否安装apk
     */
    public static final String CMD_SEARCH_INSTALL_APK = "SearchInstalledApk";

    /**
     * 安装apk
     */
    public static final String CMD_INSTALL_APK = "InstallApk";

    /**
     * 卸载apk
     */
    public static final String CMD_UNINSTALL_APK = "UninstallApk";
    /**
     * 卸载apk
     */
    public static final String CMD_BATCH_UNINSTALL = "BatchUninstall";

    /**
     * 下发任务
     */
    public static final String CMD_TYPE_STARTTASK = "StartTask";

    /**
     * 上报测试结果
     */
    public static final String CMD_TYPE_TEST_RESULT = "TestResult";
    /**
     * 用例执行完成提示
     */
    public static final String CMD_TYPE_EXECUTE_FINISH = "executeFinish";
    /**
     * 设备当前执行的任务
     */
    public static final String CMD_TYPE_CURRENT_TASK = "CurrentTask";
    /**
     * 脚本当前执行到第几步的步骤信息
     */
    public static final String CMD_TYPE_CURRENT_STEP = "CurrentStep";

    /**
     * Proxy的在线状态：0-在线；1-离线；2-下线
     */
    public static final String PROXY_ONLINE_STATUS_0 = "0";

    public static final String PROXY_ONLINE_STATUS_1 = "1";

    public static final String PROXY_ONLINE_STATUS_2 = "2";

    /**
     * 图片压缩比例
     */
    public static final Integer IMAGE_COMPRESS_RATE = 3;

    /**
     * 脚本步骤关键字
     * sleep、upGlide、downGlide、leftGlide、rightGlide
     * back、tap、click、doubleClick、longPress
     * picturetap、ifJudge、forLoop、Affair、sendText
     */
    public static final String SCRIPT_STEP_SLEEP        = "sleep";
    public static final String SCRIPT_STEP_UPGLIDE      = "upGlide";
    public static final String SCRIPT_STEP_DOWNGLIDE    = "downGlide";
    public static final String SCRIPT_STEP_LEFTGLIDE    = "leftGlide";
    public static final String SCRIPT_STEP_RIGHTGLIDE   = "rightGlide";
    public static final String SCRIPT_STEP_BACK         = "back";
    public static final String SCRIPT_STEP_TAP          = "tap";
    public static final String SCRIPT_STEP_CLICK        = "click";
    public static final String SCRIPT_STEP_DOUBLECLICK  = "doubleclick";
    public static final String SCRIPT_STEP_LONGPRESS    = "longPress";
    public static final String SCRIPT_STEP_PICTURETAP   = "picturetap";
    public static final String SCRIPT_STEP_IFJUDGE      = "ifJudge";
    public static final String SCRIPT_STEP_FORLOOP      = "forLoop";
    public static final String SCRIPT_STEP_AFFAIR       = "Affair";
    public static final String SCRIPT_STEP_SENDTEXT     = "sendText";
    public static final String SCRIPT_STEP_SCREENSHOT   = "screenShot";
    /** 动态验证码校验 */
    public static final String SCRIPT_STEP_DYNAMIC      = "sendDynamicText";
    /** 短信校验 */
    public static final String SCRIPT_STEP_SMSVARIFY    = "smsVerify";
    /** 内容校验 */
    public static final String SCRIPT_STEP_VERIFY       = "verify";

    public static final String SCRIPT_CMD_START         = "start";
    public static final String SCRIPT_CMD_END           = "end";

    /** 批量任务 **/
    public static final String EXECUTE_TYPE_BATCHTASK        = "BatchTask";
    /** 众测任务 **/
    public static final String EXECUTE_TYPE_SURVEYTASK       = "SurveyTask";
    /** 单个执行任务 **/
    public static final String EXECUTE_TYPE_SINGLECASE       = "SingleCase";

}
