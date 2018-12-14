package com.cmit.testing.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

public class MyBeanUtils {

	public static void populate(Object bean,Map<String,String[]> map) throws Exception{
		
		Class clazz = bean.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			String fieldName = field.getName();
			String[] values = map.get(fieldName);
			
			String valueStr = Arrays.toString(values);
			String valuett = valueStr.substring(1, valueStr.length()-1);
			
			String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
			Class type = field.getType();
			String name = type.getName();
			System.out.println("type="+name);
			Object value = null;
			if(name.contains("String")){
				value = valuett;
			}else if(name.contains("Date")){
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				value = format.parse(valuett);
			}else if(name.contains("Integer")){
				value = Integer.parseInt(valuett);
			}
			Method method = clazz.getDeclaredMethod(methodName, type);
			
			method.invoke(bean, value);
			
		}
		
	}
}
