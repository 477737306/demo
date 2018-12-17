package com.cmit.testing.listener.operatecmd;

import com.cmit.testing.entity.SendMessage;
import com.cmit.testing.entity.app.MsgHead;
import com.cmit.testing.entity.proxy.*;
import com.cmit.testing.listener.request.SendRequest;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.app.DeviceConstant;

import java.util.Date;
import java.util.List;

/**
 * @author XieZuLiang
 * @description  真机操作命令对象
 * @date 2018/9/5 0005 上午 10:03.
 */
public class OperateCmdFactory {

    private static OperateCmdFactory operateCmdFactory;

    public static OperateCmdFactory getInstance() {
        if (operateCmdFactory == null) {
            operateCmdFactory = new OperateCmdFactory();
            return operateCmdFactory;
        } else {
            return operateCmdFactory;
        }
    }

    /**
     * 点击操作
     *
     * @return
     */
    public CmdRoot click(String deviceSn, int x, int y) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_CLICK);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setX(x);
        cmdBody.setY(y);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 长按操作
     *
     * @return
     */
    public CmdRoot longClick(String deviceSn, int x, int y, int duration) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_LONG_CLICK);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setX(x);
        cmdBody.setY(y);
        cmdBody.setDuration(duration);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 滑动操作
     *
     * @return
     */
    public CmdRoot swipe(String deviceSn, int fromX, int fromY, int toX, int toY, int duration) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_SWIPE);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setFromX(fromX);
        cmdBody.setFromY(fromY);
        cmdBody.setToX(toX);
        cmdBody.setToY(toY);
        cmdBody.setDuration(duration);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 点击主页按钮
     *
     * @return
     */
    public CmdRoot home(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_HOME);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 点击主页按钮
     *
     * @return
     */
    public CmdRoot menu(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_MENU);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 点击返回按钮
     *
     * @return
     */
    public CmdRoot back(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_BACK);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 点击搜索按钮
     *
     * @return
     */
    public CmdRoot search(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_SEARCH);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 唤醒/锁屏
     *
     * @return
     */
    public CmdRoot power(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_POWER);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 截屏
     *
     * @return
     */
    public CmdRoot screencap(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_SCREENCAP);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 重启
     *
     * @return
     */
    public CmdRoot reboot(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_REBOOT);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 输入文本
     *
     * @return
     */
    public CmdRoot input(String deviceSn, String text) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_INPUT);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setText(text);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 发送短信（仅支持英文）
     *
     * @return
     */
    public CmdRoot sendSms(String deviceSn, String smsRecipient, String smsContent) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_TYPE_SEND_SMS);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setSmsRecipient(smsRecipient);
        cmdBody.setSmsContent(smsContent);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 打开指定网址（默认浏览器）
     *
     * @return
     */
    public CmdRoot openUrl(String deviceSn, String url) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_OPEN_URL);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdBody.setUrl(url);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 回车
     *
     * @return
     */
    public CmdRoot enter(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_ENTER);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 退格
     *
     * @return
     */
    public CmdRoot backspace(String deviceSn) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(DeviceConstant.CMD_BACKSPACE);
        CmdBody cmdBody = new CmdBody();
        cmdBody.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(cmdBody);
        return cmdRoot;
    }

    /**
     * 安装apk
     */
    public CmdRoot installApk(String deviceSn, String uri,String pkgName,String version) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(AppConstants.CMD_INSTALL_APK);
        AppOperate appOperate = new AppOperate();
        appOperate.setDeviceSn(deviceSn);
        appOperate.setUri(uri);
        appOperate.setPackageName(pkgName);
        appOperate.setAppVersion(version);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }

    /**
     * 卸载apk
     */
    public CmdRoot uninstallApk(String deviceSn, String pkgName) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(AppConstants.CMD_UNINSTALL_APK);
        AppOperate appOperate = new AppOperate();
        appOperate.setDeviceSn(deviceSn);
        appOperate.setPackageName(pkgName);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }

    /**
     * 批量卸载app
     *
     * @return
     */
    public CmdRoot batchUnIntallApk(String deviceSn, List<BaseApk> apkList) {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(AppConstants.CMD_BATCH_UNINSTALL);
        BatchUnInstallApkRequest appOperate = new BatchUnInstallApkRequest();
        appOperate.setDeviceSn(deviceSn);
        appOperate.setAppInfos(apkList);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }

    /**
     * 查询应用apk的安装
     */
    public CmdRoot queryInstalledApk(String deviceSn, String packageName, String appVersion)
    {

        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(AppConstants.CMD_SEARCH_INSTALL_APK);
        AppOperate appOperate = new AppOperate();
        appOperate.setDeviceSn(deviceSn);
        appOperate.setPackageName(packageName);
        appOperate.setAppVersion(appVersion);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }


    /**
     * 下发命令给手机设备发送短信
     * @param sendRequest
     * @return
     */
    public CmdRoot sendMessage(SendRequest sendRequest)
    {
        CmdRoot cmdRoot = createRootCmd();
        cmdRoot.setCMD_TYPE(AppConstants.CMD_TYPE_SEND_MSG);
        cmdRoot.setCMD_PARAM(sendRequest);
        return cmdRoot;
    }

    /**
     * 构造请求对象头
     *
     * @return
     */
    public CmdRoot createRootCmd() {
        CmdRoot cmdRoot = new CmdRoot();
        cmdRoot.setACC_TYPE(AppConstants.CMD_ACC_TYPE);
        cmdRoot.setSEQ_NO(System.currentTimeMillis() + "");
        return cmdRoot;
    }

    /**
     * 构造请求对象头
     *
     * @return
     */
    public MsgHead<ImageTransParam> startImgTransRequest(String deviceSn, int port, int control) {
        MsgHead<ImageTransParam> cmdRoot = new MsgHead<ImageTransParam>(AppConstants.CMD_ACC_TYPE, AppConstants.CMD_START_IMAGE_TRANS);
        cmdRoot.setSEQ_NO(new Date().getTime() + "");
        ImageTransParam appOperate = new ImageTransParam();
        appOperate.setDeviceSn(deviceSn);
        appOperate.setControl(control);
        appOperate.setImage_socketPort(port);
        appOperate.setCompressRate(AppConstants.IMAGE_COMPRESS_RATE);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }

    /**
     * 构造请求对象头
     *
     * @return
     */
    public MsgHead<ImageTransParam> stopImgTransRequest(String deviceSn) {
        MsgHead<ImageTransParam> cmdRoot = new MsgHead<ImageTransParam>(AppConstants.CMD_ACC_TYPE, AppConstants.CMD_STOP_IMAGE_TRANS);
        cmdRoot.setSEQ_NO(new Date().getTime() + "");
        ImageTransParam appOperate = new ImageTransParam();
        appOperate.setDeviceSn(deviceSn);
        cmdRoot.setCMD_PARAM(appOperate);
        return cmdRoot;
    }


}
