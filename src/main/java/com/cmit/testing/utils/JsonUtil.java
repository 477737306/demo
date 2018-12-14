package com.cmit.testing.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * json处理工具类
 * 
 * @author zhangle
 */
@Component
public class JsonUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final ObjectMapper mapper;

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
	}

	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("转换json字符失败!");
		}
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException("将json字符转换为对象时失败!");
		}
	}

	//使用alibaba fastjson库
	
	public static JSONObject parseJsonObject(String input) {
		try {
			return parseObject(input, JSONObject.class, DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			logger.error(Throwables.getStackTraceAsString(e));
		}
		return new JSONObject();
	}
	
	// 自定义日期格式
	public static final <T> T parseObject(String input, TypeReference<T> type, String dateFormat) {
		return parseObject(input, type.getType(), dateFormat);
	}

	// 自定义日期格式
	public static final <T> T parseObject(String input, Type clazz, String dateFormat) {

		ParserConfig config = new ParserConfig();
		config.putDeserializer(Date.class, new DateDeserializer());
		return JSON.parseObject(input, clazz, config, null, JSON.DEFAULT_PARSER_FEATURE);
	}

	/**
	 * 用fastjson 将json字符串解析为一个 JavaBean
	 *
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}
}
