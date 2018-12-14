/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmit.testing.utils;


import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.entity.DownloadFileDto;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 高频方法集合类
 */
public class ToolUtil {

    public static TestCaseService testCaseService = SpringContextHolder.getBean("testCaseServiceImpl");
    public static AppCaseService appCaseService = SpringContextHolder.getBean("appCaseService");


    /**
     * 根据勾选的用例排序
     *
     * @param caseMap
     * @return List
     */
    public static List<Map<String, List<TestCase>>> reorderByCaseIds(Map<String, List<Integer>> caseMap) {
        List<Integer> webList = caseMap.get("web");
        List<Integer> appList = caseMap.get("app");
        List<TestCase> webCaseList = new ArrayList<>();
        List<TestCase> appCaseList = new ArrayList<>();
        for (Integer id : webList) {
            TestCase testCase = testCaseService.selectByPrimaryKey(id);
            webCaseList.add(testCase);
        }
        // 获取App用例相关信息
        if (CollectionUtils.isNotEmpty(appList)) {
            List<Map<String, Object>> appMapList = appCaseService.getAppCaseByCaseIds(appList);
            for (Map<String, Object> map : appMapList) {
                TestCase testCase = new TestCase();
                testCase.setId((Integer) map.get("caseId"));
                testCase.setType("app");
                String followIds = (String) map.get("followIds");
                testCase.setFollowId(followIds);
                appCaseList.add(testCase);
            }
        }
        Map<String, List<TestCase>> reorderCaseMap = new HashMap<>();
        reorderCaseMap.put("app", appCaseList);
        reorderCaseMap.put("web", webCaseList);
        return reorder(reorderCaseMap);
    }

    private static List<Map<String, List<TestCase>>> reorder(Map<String, List<TestCase>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        List<Map<String, List<TestCase>>> arr = new ArrayList<>();
        List<TestCase> webList = map.get("web");
        List<TestCase> appList = map.get("app");
        if (webList == null) {
            webList = new ArrayList<>();
        }
        if (appList == null) {
            appList = new ArrayList<>();
        }
        while (webList.size() > 0 || appList.size() > 0) {
            System.out.println("------------------------------------");
            TestCase temp = null;
            Map<String, List<Integer>> childIdMap = new HashMap<>();
            //类型 0 web 1 app
            int type = 0;
            if (webList.size() > 0) {
                temp = webList.get(0);
                List<Integer> childIdList = new ArrayList<>();
                childIdList.add(Integer.valueOf(temp.getId()));
                childIdMap.put("web", childIdList);
                childIdMap.put("app", new ArrayList<>());
                type = 0;
            } else if (appList.size() > 0) {
                temp = appList.get(0);
                List<Integer> childIdList = new ArrayList<>();
                childIdList.add(Integer.valueOf(temp.getId()));
                childIdMap.put("web", new ArrayList<>());
                childIdMap.put("app", childIdList);
                type = 1;
            } else {
                //没有数据了
                break;
            }
            Map<String, List<TestCase>> result = getNonFollow(map);
            arr.add(result);
            webList.removeAll(result.get("web"));
            appList.removeAll(result.get("app"));
            map.put("web", webList);
            map.put("app", appList);
        }
        for (int i = 0; i < arr.size(); i++) {
            Map<String, List<TestCase>> temp = arr.get(i);
            for (String key : temp.keySet()) {
                int finalIndex = i + 1;
                temp.get(key).forEach(e -> {
                    System.out.println(String.format("index %s: type %s Bean id is %s", finalIndex, key, e.getId()));
                });
            }
        }
        return arr;
    }

    /**
     * @param map
     * @return
     */
    private static Map<String, List<TestCase>> getNonFollow(Map<String, List<TestCase>> map) {
        Map<String, List<TestCase>> result = new ConcurrentHashMap<>();
        List<TestCase> webResult = new ArrayList<>();
        List<TestCase> appResult = new ArrayList<>();
        result.put("web", webResult);
        result.put("app", appResult);
        if (null == map || map.size() == 0) {
            return null;
        }
        List<TestCase> webList = map.get("web");
        List<TestCase> appList = map.get("app");
        webResult = getList(webList, appList, 0);
        appResult = getList(appList, webList, 1);
        result.put("web", webResult);
        result.put("app", appResult);
        return result;
    }

    private static List<TestCase> getList(List<TestCase> list1, List<TestCase> list2, int type) {
        List<TestCase> result = new ArrayList<>();
        for (TestCase bean : list1) {
            String follow = bean.getFollowId();
            if (StringUtils.isEmpty(follow)) {
                result.add(bean);
            } else {
                Map object = null;
                try {
                    object = JsonUtil.toObject(bean.getFollowId(), Map.class);
                    //解析依赖的id集合
                    List<String> webIds = Arrays.asList(JSONObject.parseArray(object.get("web").toString()).toArray()).stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
                    List<String> appIds = Arrays.asList(JSONObject.parseArray(object.get("app").toString()).toArray()).stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
                    if (type == 0) {
                        if (list1.stream().filter(e -> webIds.contains(e.getId().toString())).count() > 0 || list2.stream().filter(e -> appIds.contains(e.getId().toString())).count() > 0) {
                            continue;
                        } else {
                            result.add(bean);
                        }
                       /* boolean flag = false;
                        for (TestCase temp : list1) {
                            if (webIds.contains(temp.getId().toString())) {
                                flag = true;
                                break;
                            }
                        }
                        for (TestCase temp : list2) {
                            if (appIds.contains(temp.getId())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            continue;
                        } else {
                            result.add(bean);
                        }*/
                    } else if (type == 1) {
                        if (list1.stream().filter(e -> appIds.contains(e.getId().toString())).count() > 0 || list2.stream().filter(e -> webIds.contains(e.getId().toString())).count() > 0) {
                            continue;
                        } else {
                            result.add(bean);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 获取随机位数的字符串
     *
     * @author fengshuonan
     * @Date 2017/8/24 14:09
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 判断一个对象是否是时间类型
     *
     * @author stylefeng
     * @Date 2017/4/18 12:55
     */
    public static String dateType(Object o) {
        if (o instanceof Date) {
            return DateUtil.getDay((Date) o);
        } else {
            return o.toString();
        }
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * 1. obj1 == null && obj2 == null; 2. obj1.equals(obj2)
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
    }

    /**
     * 计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度
     *
     * @param obj 被计算长度的对象
     * @return 长度
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * 对象中是否包含元素
     *
     * @param obj     对象
     * @param element 元素
     * @return 是否包含
     */
    public static boolean contains(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).values().contains(element);
        }

        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equals(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对象是否不为空(新增)
     *
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return o.toString().trim().equals("");
        } else if (o instanceof List) {
            return ((List) o).size() == 0;
        } else if (o instanceof Map) {
            return ((Map) o).size() == 0;
        } else if (o instanceof Set) {
            return ((Set) o).size() == 0;
        } else if (o instanceof Object[]) {
            return ((Object[]) o).length == 0;
        } else if (o instanceof int[]) {
            return ((int[]) o).length == 0;
        } else if (o instanceof long[]) {
            return ((long[]) o).length == 0;
        }
        return false;
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return
     */
    public static boolean isOneEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @param os
     * @return
     */
    public static boolean isAllEmpty(Object... os) {
        for (Object o : os) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为数字
     *
     * @param obj
     * @return
     */
    public static boolean isNum(Object obj) {
        try {
            Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 如果为空, 则调用默认值
     *
     * @param str
     * @return
     */
    public static Object getValue(Object str, Object defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        return str;
    }

    /**
     * 强转->string,并去掉多余空格
     *
     * @param str
     * @return
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转->string,并去掉多余空格
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return str.toString().trim();
    }

    /**
     * 强转->int
     *
     * @param obj
     * @return
     */
//	public static int toInt(Object value) {
//		return toInt(value, -1);
//	}

    /**
     * 强转->int
     *
     * @param obj
     * @param defaultValue
     * @return
     */
//	public static int toInt(Object value, int defaultValue) {
//		return Convert.toInt(value, defaultValue);
//	}

    /**
     * 强转->long
     *
     * @param obj
     * @return
     */
//	public static long toLong(Object value) {
//		return toLong(value, -1);
//	}

    /**
     * 强转->long
     *
     * @param obj
     * @param defaultValue
     * @return
     */
//	public static long toLong(Object value, long defaultValue) {
//		return Convert.toLong(value, defaultValue);
//	}
//
//	public static String encodeUrl(String url) {
//		return URLKit.encode(url, CharsetKit.UTF_8);
//	}
//
//	public static String decodeUrl(String url) {
//		return URLKit.decode(url, CharsetKit.UTF_8);
//	}

    /**
     * map的key转为小写
     *
     * @param map
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    public static Map<String, Object> caseInsensitiveMap(Map<String, Object> map) {
        Map<String, Object> tempMap = new HashMap<>();
        for (String key : map.keySet()) {
            tempMap.put(key.toLowerCase(), map.get(key));
        }
        return tempMap;
    }

    /**
     * 获取map中第一个数据值
     *
     * @param <K> Key的类型
     * @param <V> Value的类型
     * @param map 数据源
     * @return 返回的值
     */
    public static <K, V> V getFirstOrNull(Map<K, V> map) {
        V obj = null;
        for (Entry<K, V> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder(String... strs) {
        final StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static void builder(StringBuilder sb, String... strs) {
        for (String str : strs) {
            sb.append(str);
        }
    }

    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffix(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str;
        }

        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * 当前时间
     *
     * @author stylefeng
     * @Date 2017/5/7 21:56
     */
    public static String currentTime() {
        return DateUtil.getTime();
    }


    /**
     * 把一个数转化为int
     *
     * @author fengshuonan
     * @Date 2017/11/15 下午11:10
     */
    public static Integer toInt(Object val) {
        if (val instanceof Double) {
            BigDecimal bigDecimal = new BigDecimal((Double) val);
            return bigDecimal.intValue();
        } else if (val instanceof Long) {
            BigDecimal bigDecimal = new BigDecimal((Long) val);
            return bigDecimal.intValue();
        } else {
            return Integer.valueOf(val.toString());
        }
    }


    /**
     * 给list的元素计数
     *
     * @param map
     * @param array
     * @return
     */
    public static Map<String, Integer> listCount(Map<String, Integer> map, Object[] array) {

        for (int i = 0; i < array.length; i++) {
            String temp = (String) array[i];
            Integer count = map.get(temp);
            if (null == count) {
                map.put(temp, 2);
            } else {
                map.put(temp, map.get(temp) + 1);
            }
        }
        return map;
    }

    /**
     * 控制数值的精度
     * @param number 数值
     * @param precision 保留小数点位数
     * @return
     */
    public static double numericalPrecision(double number, int precision) {
		return new BigDecimal(number).setScale(precision, RoundingMode.UP).doubleValue();
	}
}