package com.cmit.testing.utils.selenium;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cmit.testing.exception.NoSuchWebDriverException;

/**
 * 驱动类
 *
 * @author XXM
 */
public abstract class Driver {
    private static WebDriver instance;
    private static String ip = "192.168.41.68";
//    private static String ip = "localhost";
    private static String port = "4444";
    private static String url = "http://" + ip + ":" + port + "/wd/hub";

    public Driver() {

    }

    /**
     * 启动
     *
     * @param driverName
     */
    public static WebDriver setup(String driverName) {
        Driver.instance = getDriver(driverName);
        return instance;
    }

    public static String getIpAddress(RemoteWebDriver webDriver) throws IOException {
        return new GridNodeLocator(ip, port).locate(webDriver);
    }

    /**
     * 选择浏览器
     *
     * @param tag
     * @return
     */
    public static WebDriver getDriver(String tag) {
        if (tag == null) {
            throw new NullPointerException();
        }
        if (tag.toLowerCase().equals("ie")) {
            return createRemoteIEDriver();
        } else if (tag.toLowerCase().equals("chrome")) {
            return createRemoteChromeDriver();
        } else if (tag.toLowerCase().equals("firefox")) {
            return createRemoteFirefoxDriver();
        } else {
            throw new NoSuchWebDriverException();
        }
    }

    /**
     * 远程节点 Firefox
     */
    public static WebDriver createRemoteFirefoxDriver() {
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setCapability("node_screen_recording", true);
        capability.setCapability("node_recording_timeout", 60);
        capability.setBrowserName("firefox");
        capability.setPlatform(Platform.WINDOWS);
        capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);  // 设置alert警示框级别
        try {
            instance = new RemoteWebDriver(new URL(url), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 远程节点 chrome
     *
     * @return
     */
    public static WebDriver createRemoteChromeDriver() {
        DesiredCapabilities capability = DesiredCapabilities.chrome();
        capability.setCapability("node_screen_recording", true);
        capability.setCapability("node_recording_timeout", 60);
        capability.setBrowserName("chrome");
        capability.setVersion("");
        capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);  // 设置alert警示框级别
        try {
            instance = new RemoteWebDriver(new URL(url), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 远程节点IE
     *
     * @return
     */
    public static WebDriver createRemoteIEDriver() {
        // 指定调用IE进行测试
        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
        // 避免IE安全设置里，各个域的安全级别不一致导致的错误
        capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        // 连接到selenium hub，远程启动浏览器
        capability.setPlatform(Platform.WINDOWS);
        capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);  // 设置alert警示框级别
        try {
            instance = new RemoteWebDriver(new URL(url), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 获取OS
     *
     * @return
     */
    public static String getOperatingSystem() {
        String operatingSystem = null;

        if (SystemUtils.IS_OS_LINUX) {
            operatingSystem = "linux";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            operatingSystem = "windows";
        } else if (SystemUtils.IS_OS_MAC) {
            operatingSystem = "mac";
        }

        return operatingSystem;
    }

    public static void main(String[] args) throws IOException {
        WebDriver driver = Driver.createRemoteChromeDriver();
        System.out.println(Driver.getIpAddress((RemoteWebDriver) driver));
        driver.quit();
    }
}
