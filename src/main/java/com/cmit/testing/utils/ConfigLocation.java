package com.cmit.testing.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 提供了用于获取classpath之外资源路径的方法
 * @author XXM
 *
 */
public class ConfigLocation
{
	// web工程根路径
	private static final String WEB_CONTENT;

	// web工程下的WEB-INF路径
	private static final String WEB_INF_PATH;

	// web工程下的etc/config/路径
	private static final String CONFIG_PATH;
	
	//取当前操作系统版本名
	private static final String SYSTEM_NAME = System.getProperty("os.name");
	
	private static final String LINUX ="Linux";

	private static Log logger = LogFactory.getLog(ConfigLocation.class);
	static
	{
		WEB_INF_PATH = chopLastSection(getClassPath());
		WEB_CONTENT = chopLastSection(WEB_INF_PATH);
		CONFIG_PATH = WEB_CONTENT.concat("etc/config/"
				.replace("/", File.separator));
	}

	/**
	 * 获取web工程根路径，该路径以文件分隔符结束
	 * 
	 * @return web工程根路径
	 */
	public static String getWebContent() {
		return WEB_CONTENT;
	}

	/**
	 * 获取web工程下的WEB-INF路径，该路径以文件分隔符结束
	 * 
	 * @return web工程下的WEB-INF路径
	 */
	public static String getWebInfPath()
	{
		return WEB_INF_PATH;
	}

	/**
	 * 获取web工程下etc/config/的路径，该路径为项目配置文件路径
	 * 
	 * @return web工程下etc/config/的路径
	 */
	public static String getConfigPath()
	{
		return CONFIG_PATH;
	}

	private static String chopLastSeparator(String src)
	{
		if (src.endsWith(File.separator))
		{
			src = src.substring(0, src.lastIndexOf(File.separator));
		}
		return src;
	}

	private static String chopLastSection(String src)
	{
		String temp = chopLastSeparator(src);
		return temp.substring(0, temp.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 获取classpath
	 * 
	 * @return classpath
	 */
	public static String getClassPath()
	{
		String url = Thread.currentThread().getContextClassLoader()
				.getResource("").toString().replace("/", File.separator);
		try
		{
			url = URLDecoder.decode(url, Charset.defaultCharset().toString());
			String prefix = "file:".concat(File.separator);
			if (url.startsWith(prefix))
			{
				url = url.substring(prefix.length());
				if(SYSTEM_NAME.startsWith(LINUX)){
					if(url.indexOf(":") > -1){
						url = url.substring(File.separator.length());
					}
				}
			}
			if(SYSTEM_NAME.startsWith(LINUX)){
				url="/"+url;
			}
			 
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("The system should always have the platform default");
		}
		return url;
	}

	public static void main(String[] args)
	{
		System.out.println(ConfigLocation.getClassPath());
		System.out.println(ConfigLocation.getWebInfPath());
		System.out.println(ConfigLocation.getWebContent());
		System.out.println(ConfigLocation.getConfigPath());
		System.out.println(System.getProperty("os.name"));
		System.out.println(ConfigLocation.getClassPath().concat("template/example.xls"));
	}
}
