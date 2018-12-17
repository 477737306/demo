package com.cmit.testing.utils.verify;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class maintest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 System.setProperty("webdriver.chrome.driver","C:\\Users\\hasee\\Desktop\\Hub-Node\\chromedriver.exe");
		 WebDriver driver=new ChromeDriver();
		 String path="http://192.168.125.206:8000/login";
		 String type="xpath";
		 String xpath="//*[@id=\"root\"]/div/div/div/div/div/form/div[3]/div/div/span/div[1]/img";
		 testVerify  ts=new testVerify(driver,path,type,xpath);
		 String cerifyCode=ts.verify();
		 System.out.println("cerifyCode"+cerifyCode);
	}
	
}
