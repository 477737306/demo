package com.cmit.testing.utils.pictures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
/*
 * liumin
 * 滚动截图
 */
import org.openqa.selenium.WebDriver;

public class PicturesScreenShort {

	WebDriver driver;
	String path;

	public PicturesScreenShort(WebDriver driver, String path) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.path = path;
	}

	public List<String> PicturesShort() {
		List<String> img_list = new ArrayList<String>();
		driver.manage().window().maximize();
		driver.get(path);
		Dimension size = driver.manage().window().getSize();
		int height = size.height - 110;
		int width = size.width;
		// 创建一个javascript 执行实例
		JavascriptExecutor je = (JavascriptExecutor) driver;
		String real_scroll_h = null;
		String real_top = null;
		String last_t = null;
		int cishu = 0;
		while (cishu < 20) {
			double high = cishu * (height - 110);
			String js = "var q=document.documentElement.scrollTop=" + high;
			je.executeScript(js);
			String js1 = "return document.body.scrollHeight.toString()+','+document.body.scrollTop.toString()";
			String js1_result = (String) je.executeScript(js1);
			real_scroll_h = js1_result.split(",")[0];
			real_top = js1_result.split(",")[1];
			// System.out.println("cishu"+cishu);
			// System.out.println("real_top"+real_top);
			// System.out.println("cishu*height"+cishu*height);
			double result_h = cishu * (height - 110);
			if (Integer.parseInt(real_scroll_h) >= result_h) {
				last_t = real_top;
				File srcFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				try {
					FileUtils.copyFile(srcFile2, new File(".\\Screenshots\\screenshot" + cishu + ".png"));
					img_list.add(".\\Screenshots\\screenshot" + cishu + ".png");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cishu = cishu + 1;
				// driver.manage().timeouts().implicitlyWait(30,
				// TimeUnit.SECONDS);
			} else {
				if (!real_top.equals(last_t)) {
					last_t = real_top;
				} else {
					break;
				}
			}
		}
		return img_list;
	}

}
