package com.cmit.testing.utils.selenium;

import org.openqa.selenium.By;

import com.cmit.testing.exception.OrientFailException;

/**
 * 定位方式
 * @author XXM
 *
 */
public class ByMethod {

	/**
	 * By方法
	 * 
	 * @param Location
	 * @param Value
	 * @return
	 */
	public static By getBy(String Location, String Value) {
		if (Location.equals("id")) {
			return By.id(Value);
		} else if (Location.equals("xpath")) {
			return By.xpath(Value);
		} else if (Location.equals("css")) {
			return By.cssSelector(Value);
		} else if (Location.equals("name")) {
			return By.name(Value);
		} else if (Location.equals("tagName")) {
			return By.tagName(Value);
		} else if (Location.equals("className")) {
			return By.className(Value);
		} else if (Location.equals("linkText")) {
			return By.linkText(Value);
		} else {
			throw new OrientFailException();
		}
	}
}
