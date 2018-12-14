package com.cmit.testing.utils.verify;

import com.cmit.testing.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class testVerify {
    WebDriver driver;
    String verifyPath;
    String type;
    String element;
    WebElement ele;

    public testVerify(WebDriver driver, String verifyPath, String type, String element) {
        // TODO Auto-generated constructor stub
        this.driver = driver;
        this.verifyPath = verifyPath;
        this.type = type;
        this.element = element;
    }

    public String verify() throws Exception {
        // TODO Auto-generated method stub

        String host = "http://txyzmsb.market.alicloudapi.com";
        String path = "/yzm";
        String method = "POST";
        String appcode = "51403fcf7d104010bde0f84674155b8c";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (!StringUtils.isEmpty(verifyPath)) {
            driver.get(verifyPath);
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript(String.format("window.scrollTo(%s, %s)", 0, 0));
        if (type.equals("xpath")) {
            ele = driver.findElement(By.xpath(element));
        }
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);
        Point point = ele.getLocation();
        int scrollY = 0;
        int scrollX = 0;
        //如果验证码图片完整的在截图中
        if (fullImg.getHeight() < point.getY() + ele.getSize().getHeight()) {
            scrollY = point.getY() + ele.getSize().getHeight() - fullImg.getHeight();
        }
        if (fullImg.getWidth() < point.getX() + ele.getSize().getWidth()) {
            scrollX = point.getX() + ele.getSize().getWidth() - fullImg.getWidth();
        }
        if (scrollY > 0 || scrollX > 0) {
            jse.executeScript(String.format("window.scrollTo(%s, %s)", scrollX, scrollY));
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            ele = driver.findElement(By.xpath(element));
            point = ele.getLocation();
            fullImg = ImageIO.read(screenshot);
        }
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX() - scrollX, point.getY() - scrollY,
                ele.getSize().getWidth(), ele.getSize().getHeight());
        ImageIO.write(eleScreenshot, "png", screenshot);
        InputStreamReader read = new InputStreamReader(new FileInputStream(screenshot), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = "";
        while ((bufferedReader.readLine()) != null) {
            lineTxt += bufferedReader.readLine();
        }
        read.close();
//        System.out.println("+++++++++++++++++++++++++++++验证码图片流：" + lineTxt);

        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        InputStream inputStream = null;
        byte[] data = null;
        inputStream = new FileInputStream(screenshot);
        data = new byte[inputStream.available()];
        inputStream.read(data);
        inputStream.close();
        BASE64Encoder encoder = new BASE64Encoder();
        bodys.put("v_pic", encoder.encode(data));
        bodys.put("v_type", "ne6");
        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        HttpEntity entity = response.getEntity();
        String ss = EntityUtils.toString(entity, "UTF-8");
        String[] aa = ss.split("\"");
        System.out.println("验证码===>>>>>>>>>>>>>>>>>>>>>>>>>>>>>=<" + aa[7]);
        return aa[7];
    }

}
