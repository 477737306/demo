package com.cmit.testing.common.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.entity.*;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.service.*;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.SpringContextHolder;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.ToolUtil;
import com.cmit.testing.utils.selenium.ByMethod;
import com.cmit.testing.utils.selenium.Common;
import com.cmit.testing.utils.selenium.Driver;
import com.cmit.testing.utils.verify.testVerify;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

/**
 * @author XXM
 */
public class ScriptStepScreenshotExecutionThread implements Callable<Integer> {

    private List<ScriptStep> scriptSteps;

    private TestCase testCase;

    private String number;

    private volatile RemoteWebDriver driver;

    private RecordService recordService = SpringContextHolder.getBean("recordServiceImpl");

    private TestCaseReportService caseReportService = SpringContextHolder.getBean("testCaseReportServiceImpl");

    private List<RecordStep> recordSteps = new ArrayList<>();

    private MessageService messageService = SpringContextHolder.getBean("messageServiceImpl");

    private SimCardService simCardService = SpringContextHolder.getBean("simCardServiceImpl");

    private FileStorageOperate fileStorageOperate = SpringContextHolder.getBean("fileStorageOperateImpl");

    private OrderRelationshipService orderRelationshipService = SpringContextHolder.getBean("orderRelationshipServiceImpl");

    String videoName = "";
    volatile List<File> videos = new ArrayList<>();
    volatile boolean start = true;
    volatile AtomicInteger picCount = new AtomicInteger(0);

    @Override
    public Integer call() {
        //启动线程开始截图工作
        Thread screenshot = new Thread(new Runnable() {
            @Override
            public void run() {
                OK:
                while (start) {
                    while (picCount.get() == 0 || driver == null) {
                        if (!start) {
                            break OK;
                        }
                        Thread.yield();
                    }
                    File screenshot = null;
                    try {
                        screenshot = driver.getScreenshotAs(OutputType.FILE);
                    } catch (Exception e) {
                        picCount.set(0);
                        driver = null;
                        start = false;
                        break OK;
                    }
                    videos.add(screenshot);
                    picCount.getAndDecrement();
                }
            }
        });

        // 判断脚本步骤是否包含启动浏览器这一步，如果不包含，后面就不加载driver驱动
        boolean isContainsOpen = scriptSteps.contains("open");
        // boolean多线程时 相互不影响
        AtomicBoolean flag = new AtomicBoolean(true);
        String province = null;
        int excuteNum = 1; // 执行轮次默认为1
        // 根据手机号码查找省份
        if (!number.equals("")) {
            SimCard sc = new SimCard();
            sc.setPhone(number);
            List<SimCard> simCardList = simCardService.getAllRecordsBySimCard(sc);
            if (simCardList != null && simCardList.size() > 0) {
                province = simCardList.get(0).getProvince();
            }
        }
        TestCaseReport tr = new TestCaseReport(); // 脚本结果表
        tr.setCreateTime(new Date()); //创建时间
        tr.setTestcastId(testCase.getId());
        tr.setOldTestcaseId(testCase.getOldTestcaseId());
        tr.setIsFinish(1);
        tr.setPhoneNum(number);
        tr.setDefectDescription("");
        tr.setLog("");
        tr.setProvince(province);
        // 查询同一用例同一省份的最新执行报告
        TestCaseReport previousTestCaseReport = caseReportService.getTestCaseReportByOldTestcaseIdAndProvince(testCase.getOldTestcaseId(), province);
        if (previousTestCaseReport != null && ToolUtil.isNum(previousTestCaseReport.getExcuteNum())) {
            excuteNum = previousTestCaseReport.getExcuteNum() + 1;
        }
        tr.setExcuteNum(excuteNum);
        caseReportService.insert(tr); // 插入用例报告表
        int faild = 0;  // 失败数
        double consumeTime = 0;
        String messageCode = "";  //短信验证码
        String cerifyCode = "";   //图片验证码
        List<WebElement> iframeList = new ArrayList<>();
        String imgName = "";
        List<String> imgList = new ArrayList<>();
        //是否录像
        boolean isRecord = testCase.getIsRecord() == 1;
        if (isRecord) {
            screenshot.start();
        }
        Common common = new Common();
        for (int i = 0; i < scriptSteps.size(); i++) {
            int begin = 0;  // 开始时间
            int end;      // 结束时间
            RecordStep recordStep = new RecordStep();
            recordStep.setTestcaseReportId(tr.getId());
            try {
                recordStep.setStep(scriptSteps.get(i).getStep());
                recordStep.setCommand(scriptSteps.get(i).getCommand());
                begin = (int) System.currentTimeMillis();
                recordStep.setBeginTime(new Date());
                //*****************************************************************************************************
                if (isRecord && !"open".equals(scriptSteps.get(i).getCommand()) && !"setUrl".equals(scriptSteps.get(i).getCommand())) {
                    picCount.set(20);
                    while (picCount.get() > 0 && driver != null) {
                        Thread.yield();
                    }
                }
                //**********************************************Selenium执行操作(包含系统断言)********************************************

                if (StringUtils.isNotEmpty(scriptSteps.get(i).getOrientType()) && StringUtils.isNotEmpty(scriptSteps.get(i).getOrientValue())) {
                    if (!common.doesWebElementExist(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()))) {  // 第一层未找到时开始查找iframe
                        common.selectDefaultFrame();
                        iframeList = common.selectIfarme(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()), null);
                        if (iframeList != null && iframeList.size() > 0) {
                            for (int j = 0; j < iframeList.size(); j++) {
                                driver.switchTo().frame(iframeList.get(j));
                            }
                        } else {
                            common.selectDefaultFrame();
                        }
                    }
                }
                if (!scriptSteps.get(i).getCommand().equals("open") && isContainsOpen) { // 命令不为打开浏览器时(且脚本步骤包含open)将默认driver传入
                    common.load(driver);
                    if (StringUtils.isNotEmpty(scriptSteps.get(i).getOrientType()) && StringUtils.isNotEmpty(scriptSteps.get(i).getOrientValue())) {
                        By locator = ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue());
                        if (common.doesWebElementExist(locator) && !common.assertIsShow(driver, locator)) {
                            WebElement ele = driver.findElement(locator);
                            JavascriptExecutor je = (JavascriptExecutor) driver;
                            je.executeScript("arguments[0].scrollIntoView();", ele); // 用js脚本移动鼠标到指定元素
                        }
                    }
                }
                if (scriptSteps.get(i).getCommand().equals("open")) { // 打开浏览器
                    driver = (RemoteWebDriver) Driver.setup(scriptSteps.get(i).getParamValue());
                    if (!"LINUX".equalsIgnoreCase(driver.getCapabilities().getPlatform().name())) {
                        driver.manage().window().maximize();
                    }
                    tr.setExecuteHost(Driver.getIpAddress(driver));
                } else if (scriptSteps.get(i).getCommand().equals("setUrl")) {   // 设置URL
                    common.visitUrl(scriptSteps.get(i).getParamValue());
//                    common.url(scriptSteps.get(i).getParamValue());   // 网址断言
                } else if (scriptSteps.get(i).getCommand().equals("send_Keys")) {
                    RedisUtil.incr("input_" + testCase.getId());
                    common.fillField(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()), scriptSteps.get(i).getParamValue());
                    String fillField = common.getText(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue())); // 获取input框内容
                    common.equals(scriptSteps.get(i).getParamValue(), fillField);   // 文本断言
                } else if (scriptSteps.get(i).getCommand().equals("click")) {  // 点击
                    RedisUtil.incr("click_" + testCase.getId());
                    common.clickElement(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()));
                } else if (scriptSteps.get(i).getCommand().equals("select")) {  // 下拉框
                    RedisUtil.incr("click_" + testCase.getId());
                    common.chooseDropDownItem(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()), scriptSteps.get(i).getParamValue());
                    common.selected(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()), scriptSteps.get(i).getParamValue());  // 下拉框断言
                } else if (scriptSteps.get(i).getCommand().equals("doubleClick")) { // 双击
                    RedisUtil.incrBy("click_" + testCase.getId(), 2);
                    common.doubleClickElement(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()));
                } else if (scriptSteps.get(i).getCommand().equals("refresh")) {  // 刷新
                    driver.navigate().refresh();
                } else if (scriptSteps.get(i).getCommand().equals("goBack")) {  // 返回
                    driver.navigate().back();
                } else if (scriptSteps.get(i).getCommand().equals("selectWindow")) {  // 选择窗口  下标
                    Set<String> winHandels = driver.getWindowHandles();
                    List<String> it = new ArrayList<String>(winHandels);
                    driver.switchTo().window(it.get(Integer.parseInt(scriptSteps.get(i).getParamValue()) - 1)); // 选择窗口
                } else if (scriptSteps.get(i).getCommand().equals("accept")) {  // 点击确认 alert
                    common.accept();
                } else if (scriptSteps.get(i).getCommand().equals("contextMenu")) {  // 右键
                    common.contextMenu(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()));
                } else if (scriptSteps.get(i).getCommand().equals("dismiss")) {  // 关闭alert
                    common.dismiss();
                } else if (scriptSteps.get(i).getCommand().equals("to")) {  // 跳转URL
                    driver.navigate().to(scriptSteps.get(i).getParamValue());
                    common.url(scriptSteps.get(i).getParamValue()); //校验URL
                } else if (scriptSteps.get(i).getCommand().equals("wait")) {  //等待
                    common.Waiting(Integer.parseInt(scriptSteps.get(i).getParamValue()));
                } else if (scriptSteps.get(i).getCommand().equals("quit")) {   // 退出
                    driver.quit();
                } else if (scriptSteps.get(i).getCommand().equals("screenshot")) {
//                	File pic = new File(Constants.PIC_PATH + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");
                    byte[] captchaChallengeAsJpeg = null;
                    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
                    common.Screenshot(jpegOutputStream);
                    captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
                    imgName = fileStorageOperate.fileStorageUploadFile(captchaChallengeAsJpeg, ".jpg");
                    if (!StringUtils.isEmpty(imgName)) {
                        imgList.add(imgName);
                    }
                } else if (scriptSteps.get(i).getCommand().equals("assertForAlert")) {  // 用户Alert断言
                    String assertAlert = common.assertAlert();
                    if (assertAlert != null) {
                        if (assertAlert.contains(scriptSteps.get(i).getParamValue())) {
                            tr.setLog("断言成功");
                        } else {
                            tr.setLog("断言失败");
                        }
                    }
                } else if (scriptSteps.get(i).getCommand().equals("assertForText")) {  // 用户Text断言
                    String assertText = common.getText(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()));
                    if (assertText.contains(scriptSteps.get(i).getParamValue())) {
                        tr.setLog("断言成功");
                    } else {
                        tr.setLog("断言失败");
                    }
                } else if (scriptSteps.get(i).getCommand().equals("getMsgCaptcha")) { // 短信验证码接口
                    Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(scriptSteps.get(i).getParamValue());
                    StringBuffer receiveNecessity = new StringBuffer(), receiveSelectivity = new StringBuffer();
                    if (ToolUtil.isNotEmpty(jsonMap) && ToolUtil.isNotEmpty(jsonMap.get("receiveVirify"))) {
                        JSONArray jsonArray = (JSONArray) jsonMap.get("receiveVirify");
                        for (int j = 0; j < jsonArray.size(); j++) {// 通过循环取出数组里的值
                            JSONObject object = jsonArray.getJSONObject(j);
                            String content = object.getString("content");
                            String type = object.getString("type");
                            if ("and".equalsIgnoreCase(type)) {
                                receiveNecessity.append(content).append("_");// 校验条件中的必要性内容
                            } else if ("or".equalsIgnoreCase(type)) {
                                receiveSelectivity.append(content).append("_");// 校验条件中的选择性内容
                            }
                        }
                        if (receiveNecessity.length() > 0) {
                        	receiveNecessity.deleteCharAt(receiveNecessity.length() - 1);// 删除最后一个字符                        	
                        }
                        if (receiveSelectivity.length() > 0) {
                        	receiveSelectivity.deleteCharAt(receiveSelectivity.length() - 1);// 删除最后一个字符
                        }
                    }
                    int x = 0;
                    double startTime = System.currentTimeMillis();
                    double endTime = 0;
                    Date date = new Date();
                    for (int j = 0; j < 60; j++) {
                        messageCode = messageService.getSmsVerifyCode(number, receiveNecessity.toString(), receiveSelectivity.toString(), date);
                        System.out.println("messageCode当前值:" + messageCode + ",当前X值:" + x + ",当前号码:" + number);
                        x++;
                        if (StringUtils.isNotEmpty(messageCode)) {
                            endTime = System.currentTimeMillis();
                            break;
                        }
                        Thread.sleep(3000);
                    }
                    double validateTime = endTime - startTime;
                    if (validateTime > 0) {
                        tr.setValidateTime(validateTime);
                    }
                    if (StringUtils.isEmpty(messageCode)) {
                        recordStep.setResult(0);
                        recordStep.setReturnInfo("获取短信验证码异常");
                        tr.setLog(recordStep.getReturnInfo());
                        flag.set(false);
                        faild++;
//                        recordSteps.add(recordStep);
                        driver.quit();
                        break;
                    } else {
                        scriptSteps.get(i + 1).setParamValue(messageCode);  // 为下一个脚本添加验证码
                    }
                } else if (scriptSteps.get(i).getCommand().equals("graphCaptcha")) { // 图形验证码接口
                    testVerify ts = new testVerify(driver, null, scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue());
                    cerifyCode = ts.verify();
                    if (StringUtils.isEmpty(cerifyCode)) {
                        recordStep.setResult(0);
                        recordStep.setReturnInfo("获取图形验证码异常`");
                        tr.setLog(recordStep.getReturnInfo());
                        flag.set(false);
                        faild++;
                        recordService.insert(recordStep);
//                        recordSteps.add(recordStep);
                        driver.quit();
                        break;
                    } else {
                        scriptSteps.get(i + 1).setParamValue(cerifyCode);  // 为下一个脚本添加验证码
                    }
                } else if (scriptSteps.get(i).getCommand().equals("sendSms")) {// 发送短信
                    JSONObject paramValue = JsonUtil.parseJsonObject(scriptSteps.get(i).getParamValue());
                    String receiverPhone = null, content = null;
                    if (ToolUtil.isNotEmpty(paramValue)) {
                        receiverPhone = paramValue.getString("receiverPhone");
                        content = paramValue.getString("content");
                    }
                    messageService.sendMessage(receiverPhone, content, number);
                } else if (scriptSteps.get(i).getCommand().equals("virifySms")) {// 短信校验
                    Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(scriptSteps.get(i).getParamValue());
                    StringBuffer receiveNecessity = new StringBuffer(), receiveSelectivity = new StringBuffer();
                    int time = 5;
                    String replyContent = null;
                    if (ToolUtil.isNotEmpty(jsonMap)) {
                        if (ToolUtil.isNotEmpty(jsonMap.get("time"))) {
                            time = Integer.valueOf(jsonMap.get("time").toString());
                        }
                        if (ToolUtil.isNotEmpty(jsonMap.get("replyContent"))) {
                            replyContent = jsonMap.get("replyContent").toString();
                        }
                        if (ToolUtil.isNotEmpty(jsonMap.get("receiveVirify"))) {
                            JSONArray jsonArray = (JSONArray) jsonMap.get("receiveVirify");
                            for (int j = 0; j < jsonArray.size(); j++) {// 通过循环取出数组里的值
                                JSONObject object = jsonArray.getJSONObject(j);
                                String content = object.getString("content");
                                String type = object.getString("type");
                                if ("and".equalsIgnoreCase(type)) {
                                    receiveNecessity.append(content).append("_");// 校验条件中的必要性内容
                                } else if ("or".equalsIgnoreCase(type)) {
                                    receiveSelectivity.append(content).append("_");// 校验条件中的选择性内容
                                }
                            }
                            if (receiveNecessity.length() > 0) {
                            	receiveNecessity.deleteCharAt(receiveNecessity.length() - 1);// 删除最后一个字符                        	
                            }
                            if (receiveSelectivity.length() > 0) {
                            	receiveSelectivity.deleteCharAt(receiveSelectivity.length() - 1);// 删除最后一个字符
                            }
                        }
                        // 校验接收短信内容
                        Message result = messageService.checkMessage(number, receiveNecessity.toString(), receiveSelectivity.toString(), time);
                        if (ToolUtil.isNotEmpty(replyContent) && null != result) {// 如果回复内容不为空，则发送回复短信内容
                            // 发送短信
                            messageService.sendMessage(result.getOthernumber(), replyContent, number);
                        }
                        // 判断验证结果
                        if (result == null) {
                            recordStep.setResult(0);
                            recordStep.setReturnInfo("验证短信失败");
                            tr.setLog(recordStep.getReturnInfo());
                            flag.set(false);
                            faild++;
                            recordService.insert(recordStep);
                            driver.quit();
                            break;
                        }
                    }
                } else if (scriptSteps.get(i).getCommand().equals("virifyBillAndDetailed")) {// 账单与详单验证
                	String assertText = common.getText(ByMethod.getBy(scriptSteps.get(i).getOrientType(), scriptSteps.get(i).getOrientValue()));
                	JSONObject paramValue = JsonUtil.parseJsonObject(scriptSteps.get(i).getParamValue());
                	String type = null;
                	boolean result = false;
                    if (ToolUtil.isNotEmpty(paramValue)) {
                    	type = paramValue.getString("type");// 获取要验证的类型,如业务名称
                    	List<OrderRelationship> orderRelationships = orderRelationshipService.findByPhone(number);
                    	for (OrderRelationship orderRelationship : orderRelationships) {
                    		if (type.equals("businessName") && assertText.equals(orderRelationship.getBusinessName())) {//验证业务(产品)名称
                    			result = true;
                    			break;
                    		}
                    	}
                    }
                	// 判断验证结果
                    if (!result) {
                        recordStep.setResult(0);
                        recordStep.setReturnInfo("账单与详单验证失败");
                        tr.setLog(recordStep.getReturnInfo());
                        flag.set(false);
                        faild++;
                        recordService.insert(recordStep);
                        driver.quit();
                        break;
                    }
                }
                recordStep.setEndTime(new Date());
                end = (int) System.currentTimeMillis();
                recordStep.setConsumeTime(end - begin);
                if (!"open".equals(scriptSteps.get(i).getCommand())) {
                    if ("getMsgCaptcha".equals(scriptSteps.get(i).getCommand())) {
                        consumeTime += tr.getValidateTime();
                    } else {
                        consumeTime += (end - begin);
                    }
                }
                if (i == scriptSteps.size() - 1 && driver != null) {
                    try {
                        driver.quit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    driver = null;
                }
                recordStep.setResult(1);
                recordStep.setReturnInfo("success");
                recordService.insert(recordStep);
//                recordSteps.add(recordStep);
            } catch (AssertionError | Exception e) {  // 捕捉断言
                System.out.println("-----------------------" + e.toString());
                end = (int) System.currentTimeMillis();
                recordStep.setConsumeTime(end - begin);
                recordStep.setResult(0);
                recordStep.setReturnInfo(e.toString());
                recordService.insert(recordStep);
                flag.set(false);
                faild++;
                tr.setLog(e.toString());
                // 不是 打开浏览器和设置url的错误才去截图
                if (!scriptSteps.get(i).getCommand().equals("setUrl") && !scriptSteps.get(i).getCommand().equals("open")) {
                    byte[] captchaChallengeAsJpeg = null;
                    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
                    common.Screenshot(jpegOutputStream);
                    captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
                    imgName = fileStorageOperate.fileStorageUploadFile(captchaChallengeAsJpeg, ".jpg");
                    if (!StringUtils.isEmpty(imgName)) {
                        imgList.add(imgName);
                    }
                }
                try {
                    driver.quit();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                driver = null;
                break;
            }
        }
        if (faild > 0) {
            tr.setExcuteResult(1);
            tr.setPassStatus(1);

        } else {
            tr.setExcuteResult(0);
            tr.setPassStatus(0);
        }
        if (isRecord) {
            //停止截图
            start = false;
            if (videos.size() > 0) {
                outputMp4();
            }
        }
        if (StringUtils.isNotEmpty(videoName)) {
            tr.setVideoLocation(videoName);
        }
        if (imgList.size() > 0) {
            tr.setSnapshotLocation(String.join(",", imgList));
        }
        Object clickNum = RedisUtil.get("click_" + testCase.getId());
        if (clickNum != null) {
            tr.setClickNum(Integer.valueOf(clickNum.toString()));
        }
        Object inputNum = RedisUtil.get("input_" + testCase.getId());
        if (inputNum != null) {
            tr.setInputNum(Integer.valueOf(inputNum.toString()));
        }
//        tr.setCreateBy(ShiroKit.getUser().getName());  //创建人
        tr.setConsumeTime(consumeTime);
        tr.setIsFinish(0);
        caseReportService.updateByPrimaryKey(tr);

        /*Map<String,Object> redisMap = (Map<String,Object>) RedisUtil.getObject("SurveyTask");*/
        List<Map<String, Integer>> list = (List<Map<String, Integer>>) RedisUtil.getObject("caseList");
        if (list == null) {
            list = new ArrayList<>();
        }

        List<Map<String, Integer>> list1 = list.stream().filter(e -> testCase.getId().equals(e.get("caseId"))).collect(Collectors.toList());
        List<Map<String, Integer>> list2 = list.stream().filter(e -> !testCase.getId().equals(e.get("caseId"))).collect(Collectors.toList());
        if (list1.size() > 0) {
            Map<String, Integer> map = list1.get(0);
            int number = map.get("number");
            number++;
            map.put("number", number);
            list2.add(map);
            list = list2;
        } else {
            Map<String, Integer> map = new HashMap<>();
            map.put("caseId", testCase.getId());
            map.put("executeNumber", testCase.getExecuteNumber());
            map.put("number", 1);
            list.add(map);
        }
        System.out.println(String.format("CurrentThreadName %s is work over", Thread.currentThread().getName()));
        RedisUtil.setObject("caseList", list);
        return 0;
    }


    public ScriptStepScreenshotExecutionThread(List<ScriptStep> scriptStep, TestCase testCase, String number) {
        this.scriptSteps = scriptStep;
        this.testCase = testCase;
        this.number = number;
    }

    private void outputMp4() {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".mp4";
        try {
            BufferedImage img = ImageIO.read(videos.get(0));
            int width = img.getWidth();
            int height = img.getHeight();
            if (height % 2 != 0) {
                height++;
            }
            if (width % 2 != 0) {
                width++;
            }
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(fileName, width, height);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFrameRate(25.0);
            recorder.setVideoBitrate((int) ((width * height * 25.0) * 1 * 0.07));
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            recorder.setFormat("mp4");
            recorder.start();
            OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
            for (File f : videos) {
                opencv_core.IplImage image = cvLoadImage(f.getPath());
                recorder.record(conveter.convert(image));
                opencv_core.cvReleaseImage(image);
            }
            recorder.stop();
            recorder.release();
            File file = new File(fileName);
            videoName = fileStorageOperate.fileStorageUploadFile(file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                BufferedImage img = ImageIO.read(videos.get(0));
                System.out.println(String.format("image1 width %d height %d", img.getWidth(), img.getHeight()));
            } catch (IOException e1) {

            }
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<ScriptStep> getSteps() {
        return scriptSteps;
    }

    public void setSteps(List<ScriptStep> scriptSteps) {
        this.scriptSteps = scriptSteps;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public List<RecordStep> getRecordSteps() {
        return recordSteps;
    }

    public void setRecordSteps(List<RecordStep> recordSteps) {
        this.recordSteps = recordSteps;
    }

    public List<ScriptStep> getScriptSteps() {
        return scriptSteps;
    }

    public void setScriptSteps(List<ScriptStep> scriptSteps) {
        this.scriptSteps = scriptSteps;
    }
}
