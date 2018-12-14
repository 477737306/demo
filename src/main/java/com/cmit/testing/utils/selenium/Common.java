package com.cmit.testing.utils.selenium;

import com.cmit.testing.utils.ToolUtil;
import com.cmit.testing.utils.selenium.Table.Row;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.unitils.reflectionassert.ReflectionAssert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * selenium 工具类
 *
 * @author XXM
 */
public class Common {
    public WebDriver driver;

    public ExceptionHandler exceptionHandler = new ExceptionHandler();

    public Wait wait = new Wait();

    // 默认等待时间
    public final static int STANDARD_TIMEOUT_IN_SECONDS = 30;

    /**
     * 访问URL(通过异常类，如果访问失败抛出超时异常)
     *
     * @param url
     */
    public void visitUrl(String url) {

        exceptionHandler.apply(driver::get, exceptionHandler::getTimeoutMessage, url);
    }

    /**
     * 填入文本
     *
     * @param location
     * @param value
     */
    public <T> void fillField(By location, T value) {
        wait.getVisibleWebElement(location).clear();
        wait.getVisibleWebElement(location).sendKeys(String.valueOf(value));
    }

    /**
     * 点击元素
     *
     * @param location
     * @return
     */
    public void clickElement(By location) {
        wait.getClickableWebElement(location).click();
    }

    /**
     * 双击
     *
     * @param location
     */
    public void doubleClickElement(By location) {
        new Actions(driver).doubleClick(wait.getClickableWebElement(location)).perform();
    }

    /**
     * 选择下拉框
     *
     * @param location
     * @param item
     */
    public void chooseDropDownItem(By location, String item) {
        Select select = exceptionHandler.apply((WebElement webElement) -> new Select(webElement),
                exceptionHandler::getUnexpectedTagNameMessage, wait.getClickableWebElement(location));
        exceptionHandler.apply(select::selectByVisibleText, exceptionHandler::getNoSuchElementMessage, item);
    }

    /**
     * 选中默认iframe
     */
    public void selectDefaultFrame() {
        driver.switchTo().defaultContent();
    }

    /**
     * 选中iframe
     *
     * @param iFrameNameOrId
     */
    public void selectFrame(String iFrameNameOrId) {
        wait.frameToBeAvailableAndSwitchToIt(iFrameNameOrId);
    }

    /**
     * 点击Radio按钮
     *
     * @param location
     */
    public void clickRadioButton(By location) {
        wait.getClickableWebElement(location).click();
    }

    /**
     * 模拟enter键
     */
    public void pressEnter() {
        pressKey(Keys.ENTER);
    }

    /**
     * 模拟esc键
     */
    public void pressEscape() {
        pressKey(Keys.ESCAPE);
    }

    /**
     * 模拟按键
     *
     * @param key
     */
    public void pressKey(Keys key) {
        new Actions(driver).sendKeys(key).perform();
    }

    /**
     * 选中Checkbox
     *
     * @param location
     */
    public void checkCheckbox(By location) {
        if (!isChecked(location)) {
            wait.getClickableWebElement(location).click();
        }
    }

    /**
     * 取消选中Checkbox
     *
     * @param location
     */
    public void uncheckCheckbox(By location) {
        if (isChecked(location)) {
            wait.getClickableWebElement(location).click();
        }
    }

    /**
     * 移动并拖动 (像素)
     *
     * @param location
     * @param pixel
     */
    public void moveSlider(By location, int pixel) {
        new Actions(driver).clickAndHold(wait.getVisibleWebElement(location)).moveByOffset(pixel, 0).release()
                .perform();
    }

    /**
     * 鼠标移动到指定By
     *
     * @param location
     */
    public void mouseOver(By location) {
        new Actions(driver).moveToElement(driver.findElement(location)).build().perform();
    }

    /**
     * 移动并拖动 (随机位置测试)
     *
     * @param location
     * @param percent
     */
    public void moveSlider(By location, float percent) {
        int pixel = Math.round((((float) wait.getVisibleWebElement(location).getSize().width / 100) * percent));
        moveSlider(location, pixel);
    }

    /**
     * 等待方法
     *
     * @param time
     */
    public void Waiting(int Millisecond) {
        try {
            Thread.sleep(Millisecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后退
     */
    public void goBack() {
        driver.navigate().back();
    }

    /**
     * 刷新
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * alert点击确定
     */
    public void accept() {
        try {
            Alert alert = wait.getAlertIsPresent();
            alert.accept();
        } catch (UnhandledAlertException e) {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }

    }

    /**
     * 选择alert并关闭
     */
    public void dismiss() {
        try {
            Alert alert = wait.getAlertIsPresent();
            alert.dismiss();
        } catch (UnhandledAlertException e) {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        }
    }

    /**
     * 返回alert中的内容
     *
     * @return
     */
    public String assertAlert() {
        String assertAlertText = null;
        try {
            Alert alert = wait.getAlertIsPresent();
            assertAlertText = alert.getText();
            alert.accept();
            return assertAlertText;
        } catch (UnhandledAlertException e) {
            Alert alert = driver.switchTo().alert();
            assertAlertText = alert.getText();
            alert.accept();
            return assertAlertText;
        }
    }

    /**
     * 鼠标右键点击
     */
    public void contextMenu(By location) {
        new Actions(driver).contextClick(driver.findElement(location));
    }

    /**
     * 查找IFRAME 并且检索IFRAME内是否存在元素 不存在时返回默认IFRAME 存在则返回IFRAME ID 循环后返回默认IFRAME
     * 开始数据查找 每个iframe进行查找 (不报错) 当所有iframe都循环遍历完 未找到时 默认iframe 继续进行查找操作
     *
     * @param location
     * @return
     */
    /*
     * public WebElement returnFrameId(By location) { WebElement frameId = null;
     * List<WebElement> iList = driver.findElements(By.tagName("iframe")); for
     * (int i = 0; i < iList.size(); i++) {
     * driver.switchTo().frame(iList.get(i)); if (doesWebElementExist(location))
     * { driver.switchTo().parentFrame(); frameId = iList.get(i); return
     * frameId; } else { List<WebElement> iList2 =
     * driver.findElements(By.tagName("iframe")); for (int j = 0; j <
     * iList2.size(); j++) { System.out.println("当前内iframe"+iList2.get(i)); } }
     * } return frameId; }
     */

    /**
     * 两层iframe兼容
     *
     * @param location
     * @return
     */
    public List<WebElement> selectIfarme(By location) {
        List<WebElement> fList = new ArrayList<>();
        List<WebElement> iList = driver.findElements(By.tagName("iframe"));
        for (WebElement we1 : iList) {
            driver.switchTo().defaultContent();
            driver.switchTo().frame(we1);
            if (!doesWebElementExist(location)) {
                List<WebElement> iList2 = driver.findElements(By.tagName("iframe"));
                for (WebElement we2 : iList2) {
                    driver.switchTo().defaultContent();
                    driver.switchTo().frame(we1);
                    driver.switchTo().frame(we2);
                    if (doesWebElementExist(location)) {
                        fList.add(we1);
                        fList.add(we2);
                        driver.switchTo().defaultContent();
                        return fList;
                    }
                }
            } else if (doesWebElementExist(location)) {
                driver.switchTo().defaultContent();
                fList.add(we1);
                return fList;
            }
        }
        return fList;
    }

    /**
     * 递归 多层兼容
     *
     * @param location
     * @param fList
     * @return
     */
    public List<WebElement> selectIfarme(By location, List<WebElement> fList) {
        driver.switchTo().defaultContent();
        if (fList != null) {
            List<WebElement> yList = fList;
            List<WebElement> iList = null;
            for (WebElement we1 : fList) {
                driver.switchTo().frame(we1);
            }
            iList = driver.findElements(By.tagName("iframe"));
            if (iList != null && iList.size() > 0) {
                for (WebElement we2 : iList) {
                    driver.switchTo().defaultContent();
                    for (WebElement we1 : fList) {
                        driver.switchTo().frame(we1);
                    }
                    driver.switchTo().frame(we2);
                    if (doesWebElementExist(location)) {
                        driver.switchTo().defaultContent();
                        fList.add(we2);
                        break;
                    } else {
                        yList.add(we2);
                        driver.switchTo().defaultContent();
                        yList = selectIfarme(location, yList);
                        if (fList != null && fList.size() > 0) {
                            break;
                        }
                    }
                }
                return yList;
            } else {
                return iList;
            }
        } else {
            List<WebElement> reList = new ArrayList<>();
            List<WebElement> iList = driver.findElements(By.tagName("iframe"));
            if (iList != null && iList.size() > 0) {
                for (WebElement we2 : iList) {
                    driver.switchTo().defaultContent();
                    driver.switchTo().frame(we2);
                    if (doesWebElementExist(location)) {
                        driver.switchTo().defaultContent();
                        reList.add(we2);
                        break;
                    } else {
                        reList.add(we2);
                        driver.switchTo().defaultContent();
                        reList = selectIfarme(location, reList);
                        if (reList != null && reList.size() > 0) {
                            break;
                        }
                    }
                }
                return reList;
            } else {
                return iList;
            }
        }
    }

    /**
     * 查找元素是否存在 不抛错
     *
     * @param location
     * @return
     */
    public boolean doesWebElementExist(By location) {
        try {
            driver.findElement(location);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 截取全图
     *
     * @param out
     * @throws IOException
     * @throws InterruptedException
     */
    public void Screenshot(OutputStream out) {
        try {
            JavascriptExecutor jse = ((JavascriptExecutor) driver);
            Long dh = (Long) jse.executeScript("return document.body.scrollHeight;");
            if (0 == dh.intValue()) {
                File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                BufferedImage bi = ImageIO.read(file);
                ImageIO.write(bi, "PNG", out);
                return;
            }
            Dimension size = driver.manage().window().getSize();
            // 屏幕高度
            int wh = size.height - 180;
            // 屏幕宽度
            int ww = size.width;
            // 文档高度
            // 需要滚动次数
            int page = (int) Math.ceil(Float.valueOf(dh) / wh);
            // 最终图
            BufferedImage image = new BufferedImage(ww, dh.intValue(), TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            int browserY = 0;
            for (int i = 0; i < page; i++) {
                // 截图
                File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                browserY += wh;
                int tmp = 0;
                if (browserY > dh) {
                    tmp = browserY - dh.intValue();
                    browserY = dh.intValue();
                }
                BufferedImage bi = ImageIO.read(file);
                if (tmp > 0) {
                    bi = bi.getSubimage(0, tmp, bi.getWidth(), bi.getHeight() - tmp);
                }
                // 拼图/合图
                g.drawImage(bi, 0, (browserY - wh) + tmp, bi.getWidth(), bi.getHeight(), null);
                jse.executeScript(String.format("window.scrollTo(%s, %s)", 0, browserY));
            }
            // 释放此图形的上下文以及它使用的所有系统资源。
            g.dispose();
            // 将绘制的图像生成至输出流
            ImageIO.write(image, "PNG", out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证元素是否可见
     *
     * @param location
     */
    public void appeared(By location) {
        wait.getVisibleWebElement(location);
    }

    /**
     * 验证元素是否消失
     *
     * @param location
     */
    public void disappeared(By location) {
        wait.invisibilityOfElementLocated(location);
    }

    /**
     * 验证元素是否存在
     *
     * @param location
     */
    public void available(By location) {
        wait.getPresentWebElement(location);
    }

    /**
     * 验证元素是否存在以及元素是否可见
     *
     * @param location
     */
    public void unavailable(By location) {
        wait.presenceOfElementLocated(location);
    }

    /**
     * 验证地址是否相等
     *
     * @param url
     */
    public void url(String url) {
        equals(url, driver.getCurrentUrl());
    }

    /**
     * 验证页面元素处于被选中状态
     *
     * @param location
     */
    public void selected(By location) {
        wait.elementToBeSelected(location);
    }

    /**
     * 验证页面元素处于未选中状态
     *
     * @param location
     */
    public void unselected(By location) {
        wait.notElementToBeSelected(location);
    }

    /**
     * 验证页面元素选中可用和可被单击
     *
     * @param location
     */
    public void enabled(By location) {
        wait.elementToBeClickable(location);
    }

    /**
     * 验证页面元素不可用及不可被单击
     *
     * @param location
     */
    public void disabled(By location) {
        wait.notElementToBeClickable(location);
    }

    /**
     * 验证数量
     *
     * @param location
     * @param expected 预期值
     */
    public void visibleCount(By location, int expected) {
        equals(expected, getVisibleCount(location));
    }

    /**
     * 验证当前数量
     *
     * @param location
     * @param expected 预期值
     */
    public void presentCount(By location, int expected) {
        equals(expected, getPresentCount(location));
    }

    /**
     * 验证excel数量
     *
     * @param location
     * @param expected 预期值
     */
    public void table(By actualTableLocation, Table expectedTable) {
        wait.tableToBe(actualTableLocation, expectedTable);
    }

    /**
     * 验证excel行数数量
     *
     * @param location
     * @param expected 预期值
     */
    public void tableRow(By actualTableLocation, Row expectedRow) {
        wait.tableRowToBe(actualTableLocation, expectedRow);
    }

    /**
     * 验证预期值
     *
     * @param location
     * @param expected 预期值
     */
    public void text(By location, String expectedText) {
        wait.text(location, expectedText);
    }

    /**
     * 验证预期值(多行)
     *
     * @param location
     * @param expected 预期值
     */
    public void texts(By location, List<String> expectedTexts) {
        for (String expectedText : expectedTexts) {
            text(location, expectedText);
        }
    }

    /**
     * 验证指定属性
     *
     * @param location
     * @param attributeName
     * @param expectedValue
     */
    public void attributeValue(By location, String attributeName, String expectedValue) {
        wait.attributeContains(location, attributeName, expectedValue);
    }

    /**
     * 验证指定属性(循环)
     *
     * @param location
     * @param attributeName
     * @param expectedValues
     */
    public void attributeValues(By location, String attributeName, List<String> expectedValues) {
        for (String expectedValue : expectedValues) {
            attributeValue(location, attributeName, expectedValue);
        }
    }

    /**
     * 验证下拉框内容
     *
     * @param dropdownMenuLocation
     * @param text
     */
    public void selected(By dropdownMenuLocation, String text) {
        wait.selectionToBe(dropdownMenuLocation, text);
    }

    /**
     * 验证可见元素count
     *
     * @param location
     * @return
     */
    public int getVisibleCount(By location) {
        return wait.getVisibleWebElements(location).size();
    }

    /**
     * 验证当前可见元素count
     *
     * @param location
     * @return
     */
    public int getPresentCount(By location) {
        return wait.getPresentWebElements(location).size();
    }

    /**
     * 获取文本
     *
     * @param location
     * @return
     */
    public String getText(By location) {
        return getText(wait.getVisibleWebElement(location));
    }

    /**
     * 获取表单内容
     *
     * @param tableLocation
     * @return
     */
    public Table getTable(By tableLocation) {
        Table table = new Table();

        WebElement tableWebElement = wait.getVisibleWebElement(tableLocation);
        List<WebElement> rowWebElements = tableWebElement
                .findElements(By.xpath(StringCollection.XPath.ROW_SELECTOR_INSIDE));

        for (WebElement rowWebElement : rowWebElements) {
            table.add(getRow(rowWebElement));
        }

        return table;
    }

    /**
     * 获取文本(多行)
     *
     * @param location
     * @return
     */
    public List<String> getTexts(By location) {
        List<String> results = new ArrayList<>();

        for (WebElement webElement : wait.getVisibleWebElements(location)) {
            results.add(getText(webElement));
        }

        return results;
    }

    /**
     * 获取参数名
     *
     * @param webElement
     * @param attributeName
     * @return
     */
    public String getAttributeValue(WebElement webElement, String attributeName) {
        return webElement.getAttribute(attributeName);
    }

    /**
     * 获取当前属性值
     *
     * @param location
     * @param attributeName
     * @return
     */
    public String getAttributeValue(By location, String attributeName) {
        return getAttributeValue(wait.getPresentWebElement(location), attributeName);
    }

    /**
     * 获取当前属性值（多行）
     *
     * @param location
     * @param attributeName
     * @return
     */
    public List<String> getAttributeValues(By location, String attributeName) {
        List<String> results = new ArrayList<>();

        for (WebElement webElement : wait.getPresentWebElements(location)) {
            results.add(getAttributeValue(webElement, attributeName));
        }

        return results;
    }

    /**
     * 获取文本内容 自动判断类型
     *
     * @param webElement
     * @return
     */
    public String getText(WebElement webElement) {
        String result = null;

        String inputType = webElement.getTagName();

        switch (inputType) {
            case StringCollection.ControlType.INPUT:
            case StringCollection.ControlType.TEXTAREA:
                result = webElement.getAttribute(StringCollection.AttributeType.VALUE);
                break;

            case StringCollection.ControlType.SELECT:
                result = new Select(webElement).getFirstSelectedOption().getText();
                break;

            default:
                result = webElement.getText();
                break;
        }

        return result;
    }

    /**
     * 获取表单行
     *
     * @param rowWebElement
     * @return
     */
    public Row getRow(WebElement rowWebElement) {
        Row row = new Row();

        List<WebElement> colWebElements = rowWebElement.findElements(By.xpath(StringCollection.XPath.CELL_SELECTOR));

        for (WebElement colWebElement : colWebElements) {
            row.add(getCol(colWebElement));
        }

        return row;
    }

    /**
     * 获取表单列
     *
     * @param colWebElement
     * @return
     */
    public String getCol(WebElement colWebElement) {
        return colWebElement.getText();
    }

    /**
     * 判断是否显式
     *
     * @param location
     * @return
     */
    public boolean isAppeared(By location) {
        return wait.getVisibleWebElement(location).isDisplayed();
    }

    /**
     * 判断是否消失
     *
     * @param location
     * @return
     */
    public boolean isDisappeared(By location) {
        return !isAppeared(location);
    }

    /**
     * 获取表单行
     *
     * @param table
     * @param rowIndex
     * @return
     */
    public Row getTableRow(By table, int rowIndex) {
        return getTable(table).get(rowIndex);
    }

    /**
     * 判断是否可用
     *
     * @param location
     * @return
     */
    public boolean isEnabled(By location) {
        return wait.getVisibleWebElement(location).isEnabled();
    }

    /**
     * 判断是否显示
     *
     * @param location
     * @return
     */
    public boolean isDisabled(By location) {
        return !isEnabled(location);
    }

    /**
     * 判断是否有效
     *
     * @param location
     * @return
     */
    public boolean isAvailable(By location) {
        return getPresentCount(location) != 0;
    }

    /**
     * 判断是否无效
     *
     * @param location
     * @return
     */
    public boolean isUnavailable(By location) {
        return !isAvailable(location);
    }

    /**
     * 获取访问的URL
     *
     * @return
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * 判断是否选中
     *
     * @param location
     * @return
     */
    public boolean isSelected(By location) {
        return wait.getVisibleWebElement(location).isSelected();
    }

    /**
     * 判断未选中
     *
     * @param location
     * @return
     */
    public boolean isUnselected(By location) {
        return !isSelected(location);
    }

    /**
     * 当前选中
     *
     * @param location
     * @return
     */
    public boolean isChecked(By location) {
        return isSelected(location);
    }

    /**
     * 未选中
     *
     * @param location
     * @return
     */
    public boolean isUnchecked(By location) {
        return !isSelected(location);
    }

    /**
     * 下拉框选择器
     *
     * @param dropdownMenu
     * @return
     */
    public String getSelection(By dropdownMenu) {
        return new Select(wait.getVisibleWebElement(dropdownMenu)).getFirstSelectedOption().getText();
    }

    /**
     * 判断是否为true
     *
     * @param condition
     */
    public void _true(boolean condition) {
        equals(true, condition);
    }

    /**
     * 判断是否flase
     *
     * @param condition
     */
    public void _false(boolean condition) {
        equals(false, condition);
    }

    /**
     * 判断是否为空
     *
     * @param values
     */
    public <T> void empty(List<T> values) {
        equals(0, values.size());
    }

    /**
     * 判断是否不为空
     *
     * @param values
     */
    public <T> void notEmpty(List<T> values) {
        notEquals(0, values.size());
    }

    /**
     * 判断是否包含
     *
     * @param expectedValue
     * @param actualValues
     */
    public void contains(String expectedValue, List<String> actualValues) {
        if (!actualValues.contains(expectedValue)) {
            exceptionHandler.errorMessage(exceptionHandler.getNotContainsMessage(expectedValue, actualValues));
        }
    }

    /**
     * 判断是否包含(多对多)
     *
     * @param expectedValue
     * @param actualValues
     */
    public void contains(List<String> expectedValues, List<String> actualValues) {
        for (String expectedValue : actualValues) {
            if (!actualValues.contains(expectedValue)) {
                exceptionHandler.errorMessage(exceptionHandler.getNotContainsMessage(expectedValue, actualValues));
            }
        }
    }

    /**
     * 判断是否包含（单行）
     *
     * @param containmentText
     * @param actualText
     */
    public void contains(String containmentText, String actualText) {
        if (!actualText.contains(containmentText)) {
            exceptionHandler.errorMessage(exceptionHandler.getNotContainsMessage(containmentText, actualText));
        }
    }

    /**
     * 判断是否相等
     *
     * @param expected
     * @param actual
     */
    public <T> void equals(T expected, T actual) {
        equalsAssert(expected, actual);
    }

    /**
     * 断言不相等
     *
     * @param unexpected
     * @param actual
     */
    public <T> void notEquals(T unexpected, T actual) {
        Assert.assertNotEquals(unexpected, actual);
    }

    /**
     * 启动驱动 并删除所有Cookies
     *
     * @param driver
     */
    public void setup(WebDriver driver) {
        this.driver = driver;
        driver.manage().deleteAllCookies();
        wait.setup(this.driver);
        setTimeout(Common.STANDARD_TIMEOUT_IN_SECONDS);
    }

    /**
     * 重载Driver
     *
     * @param driver
     */
    public void load(WebDriver driver) {
        this.driver = driver;
        wait.setup(this.driver);
        setTimeout(Common.STANDARD_TIMEOUT_IN_SECONDS);
    }

    /**
     * 超时时间
     *
     * @param seconds
     */
    public void setTimeout(int seconds) {
        driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
        wait.setTimeout(seconds);
    }

    /**
     * 重新设置超时时间
     */
    public void resetTimeout() {
        setTimeout(Common.STANDARD_TIMEOUT_IN_SECONDS);
    }

    /**
     * 判断是否相等（断言）
     *
     * @param expectedValue
     * @param actualValue
     */
    public <T> void equalsAssert(T expectedValue, T actualValue) {
        ReflectionAssert.assertReflectionEquals(expectedValue, actualValue);
    }

    /**
     * 元素是否在浏览器屏幕上可见,可见返回ture,不可见返回false
     *
     * @param driver
     * @param locator
     */
    public boolean assertIsShow(WebDriver driver, By locator) {
        WebElement ele = driver.findElement(locator);
        JavascriptExecutor je = (JavascriptExecutor) driver;
        // 滚动条高度+视窗高度 = 可见区域底部高度
        Object visibleBottom = je.executeScript("return window.scrollY + document.documentElement.clientHeight;");
        // 可见区域顶部高度
        Object visibleTop = je.executeScript("return window.scrollY;");
        // 元素距离页面顶部的距离
        Object eleHeight = je.executeScript("return arguments[0].getBoundingClientRect().top;", ele);
        // 元素距离页面顶部的距离+元素高度
        Object eleHeight2 = je
                .executeScript("return arguments[0].getBoundingClientRect().top + arguments[0].offsetHeight;", ele);
        // 元素的坐标大于显示窗口的顶部坐标,且小于显示窗口的底部高度时,说明元素可见
        if (ToolUtil.toInt(eleHeight) > ToolUtil.toInt(visibleTop) && ToolUtil.toInt(eleHeight2) < ToolUtil.toInt(visibleBottom)) {
            return true;
        }
        return false;
    }
}
