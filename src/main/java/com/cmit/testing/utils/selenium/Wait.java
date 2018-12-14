package com.cmit.testing.utils.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cmit.testing.utils.selenium.Table.Row;

/**
 * 流畅等待类  (参考WebDriverWait API)
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/ui/ExpectedConditions.html
 * @author XXM
 * JDK1.8新特性   Lambda表达式  :: 作用：(对象：：实例方法名、类：：静态方法名、类：：实例方法名)
 */
public class Wait
{
    private final int INTERVALL_IN_MILLISECONDS = 200;  //间隔时间 毫秒
    
    private FluentWait<WebDriver> wait;   // 流畅等待对象
    
    private com.cmit.testing.utils.selenium.ExpectedConditions expectedConditions = new com.cmit.testing.utils.selenium.ExpectedConditions();
    
    private ExceptionHandler exceptionHandler = new ExceptionHandler(); 
    
    public void setup(WebDriver driver) { // 启动
        wait = new WebDriverWait(driver, 0);
    }
    
    public void setTimeout(int seconds){  // 超时
        wait.withTimeout(seconds, TimeUnit.SECONDS)
        .pollingEvery(INTERVALL_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 检查页面的DOM上是否存在元素
     * @param location
     * @return
     */
    public WebElement getPresentWebElement(By location){
        return exceptionHandler.apply((Function<ExpectedCondition<WebElement>, WebElement>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.presenceOfElementLocated(location));
    }
    
    /**
     * 检查网页上是否存在至少一个元素
     * @param location
     * @return
     */
    public List<WebElement> getPresentWebElements(By location){
        return exceptionHandler.apply((Function<ExpectedCondition<List<WebElement>>, List<WebElement>>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.presenceOfAllElementsLocatedBy(location));
    }
    
    /**
     * 检查元素是否存在于页面的DOM上并且可见。
     * @param location
     * @return
     */
    public WebElement getVisibleWebElement(By location){
        return exceptionHandler.apply((Function<ExpectedCondition<WebElement>, WebElement>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.visibilityOfElementLocated(location));
    }
    
    /**
     * 查看与定位器匹配的Web页面上的元素是否都可见
     * @param location
     * @return
     */
    public List<WebElement> getVisibleWebElements(By location){
        return exceptionHandler.apply((Function<ExpectedCondition<List<WebElement>>, List<WebElement>>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.visibilityOfAllElementsLocatedBy(location));
    }
    
    /**
     * 检查元素是否可见，并且可以点击
     * @param location
     * @return
     */
    public WebElement getClickableWebElement(By location){
        return exceptionHandler.apply((Function<ExpectedCondition<WebElement>, WebElement>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.elementToBeClickable(location));
    }
    
    /**
     * 判断是否找到Alert
     */
    public Alert getAlertIsPresent(){
    	return exceptionHandler.apply((Function<ExpectedCondition<Alert>, Alert>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.alertIsPresent());
    }
    
    /**
     * 验证iframe是否可用及切换
     * @param iFrameNameOrId
     */
    public void frameToBeAvailableAndSwitchToIt(String iFrameNameOrId){
        exceptionHandler.apply((Function<ExpectedCondition<WebDriver>, WebDriver>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.frameToBeAvailableAndSwitchToIt(iFrameNameOrId));
    }
    
    /**
     * 验证指定属性
     * @param location
     * @param attributeName
     * @param expectedValue
     */
    public void attributeContains(By location, String attributeName, String expectedValue){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.attributeContains(location, attributeName, expectedValue));
    }
    
    /**
     * 下拉框
     * @param dropdownMenuLocation
     * @param text
     */
    public void selectionToBe(By dropdownMenuLocation, String text){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, expectedConditions.selectionToBe(dropdownMenuLocation, text));
    }
    
    /**
     * 验证元素选中状态
     * @param location
     */
    public void elementToBeSelected(By location){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.elementToBeSelected(location));
    }
    
    
    /**
     * 检查一个元素在DOM上是否可见
     * @param location
     */
    public void invisibilityOfElementLocated(By location){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.invisibilityOfElementLocated(location));
    }
    
    /**
     * 获取相反的预期值
     * @param location
     */
    public void presenceOfElementLocated(By location){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(location)));
    }
    
    /**
     * 验证页面元素未选中状态
     * @param location
     */
    public void notElementToBeSelected(By location){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.not(ExpectedConditions.elementToBeSelected(location)));
    }
    
    /**
     * 验证页面元素选中可用和可被单击
     * @param location
     */
    public void elementToBeClickable(By location){
        exceptionHandler.apply((Function<ExpectedCondition<WebElement>, WebElement>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.elementToBeClickable(location));
    }
    
    /**
     * 验证页面元素不可选中和不可被单击
     * @param location
     */
    public void notElementToBeClickable(By location){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.not(ExpectedConditions.elementToBeClickable(location)));
    }
    
    /**
     * 验证excel数量
     * @param actualTableLocation
     * @param expectedTable
     */
    public void tableToBe(By actualTableLocation, Table expectedTable){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, expectedConditions.tableToBe(actualTableLocation, expectedTable));
    }
    
    /**
     * 验证excel行数数量
     * @param actualTableLocation
     * @param expectedRow
     */
    public void tableRowToBe(By actualTableLocation, Row expectedRow){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, expectedConditions.tableRowToBe(actualTableLocation, expectedRow));
    }
    
    /**
     * 验证预期值
     * @param location
     * @param expectedText
     */
    public void text(By location, String expectedText){
        exceptionHandler.apply((Function<ExpectedCondition<Boolean>, Boolean>)wait::until, exceptionHandler::getTimeoutMessage, ExpectedConditions.textToBe(location, expectedText));
    }
}
