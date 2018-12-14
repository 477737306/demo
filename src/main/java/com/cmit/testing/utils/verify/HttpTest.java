package com.cmit.testing.utils.verify;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.service.MessageService;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.SpringContextHolder;
import com.cmit.testing.utils.StringUtils;
import org.apache.commons.codec.binary.Hex;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpTest {
    public static String KEY_STORE_FILE = "keystore.jks";
    public static String KEY_STORE_PASS = "qaz123";
    public static String TRUST_STORE_FILE = "client.jks";
    public static String TRUST_STORE_PASS = "yfcszx_2018";

    /**
     * 接口编号
     */
    //已订购业务查询接口
    public static final String ORDER_CODE = "OSPQ016";
    //账户余额查询接口（新接口）
    public static final String BALANCE_CODE = "OSPQ018";
    //流量使用情况查询接口
    public static final String USERINFO_CODE = "OSPQ019";
    //用户可用积分查询接口
    public static final String INTEGRAL_CODE = "OSPQ022";

    public static final String appKey = "420PD7Q4W7F0DE2R";
    public static final String appSecret = "M9UUN0F10IOO6WNFIHPWUUEE90Z3M5DAN7B5X5E7W046A56X5A45CIRY3J7PRBNC";
    public static final String baseUrl = "https://osp.chinamobile.com:8002/api/v2";

    private static SSLContext sslContext;

    public static String token;
    public static String authCode;

    private static long tokenStarttime = 0;
    private static long authCodeStarttime = 0;

    public static long tokenTime = 1000 * 60 * 60 * 24; //24小时
    public static long authCodeTime = 1000 * 60 * 30; //30分钟

    private static MessageService messageService = SpringContextHolder.getBean("messageServiceImpl");

    public static void main(String[] args) throws Exception {
        String[] serviceNumbers = {"13610827413", "13474730852", "18795073728", "13997094907", "15163872113", "15296769143", "15719298762", "13917405895", "15102891607", "15122432045", "13908987524", "13699387339", "14736523492", "18857871633", "13657659194", "18794797614", "14714009679", "15027728375", "15797799663", "18477166549", "14790431573", "15111179576", "13904307443", "13511037915", "14784202421", "18389622424", "13489984853", "13451834063", "15245107140", "15285093204", "15717157474"};
        String serviceType = "01"; // 01：手机号码 02：固话号码 03：宽带帐号 04：vip卡号 05：集团编码；本期只有01：手机号码
        String busiType = "04";// 01-套餐类 02增值业务类 03服务功能类 04营销活动等其他类
//		String bResult = getBalance(serviceNumber,serviceType);
//		System.out.println("余额result : " + bResult);
        /*for (int i = 0; i < serviceNumbers.length; i++) {
            String oderResult = getIntegral(serviceNumbers[i],serviceType,getAuthCode());
            System.out.println("流量result : " + oderResult);
        }*/
//		String infoResult = getUserinfo(serviceNumber,serviceType);
//		System.out.println("流量result : " + infoResult);
    }

    public static String getIntegral(String serviceNumber, String serviceType, String authCode, String timestamp) {
//        baseUrl/integral/query
//        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String url = baseUrl + "/integral/query";
        Map<String, String> headers = new HashMap<>();
//        String authCode = getAuthCode(serviceNumber, timestamp, INTEGRAL_CODE);
        headers.put("authCode", authCode);
        headers.put("appKey", appKey);
        headers.put("token", getToken(timestamp));
        headers.put("timestamp", timestamp);
        String params = "serviceType=" + serviceType + "&serviceNumber=" + serviceNumber;
        String result = sendGet(url, params, headers);
        System.out.println("getIntegral_result : " + result);
        return result;
    }

    public static String getOrderbusiness(String serviceNumber, String serviceType, String busiType, String authCode, String timestamp) {
        // busiType 01-套餐类 02增值业务类 03服务功能类 04营销活动等其他类
//        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String url = baseUrl + "/orderbusiness/query";
        Map<String, String> headers = new HashMap<>();
//        String authCode = getAuthCode(serviceNumber, timestamp, ORDER_CODE);
        headers.put("authCode", authCode);
        headers.put("appKey", appKey);
        headers.put("token", getToken(timestamp));
        headers.put("timestamp", timestamp);
        String params = "serviceType=" + serviceType + "&serviceNumber=" + serviceNumber + "&busiType=" + busiType;
        String result = sendGet(url, params, headers);
        System.out.println("getOrderbusiness_result : " + result);
        return result;
    }

    public static String getUserinfo(String serviceNumber, String serviceType, String authCode, String timestamp) {
//        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String url = baseUrl + "/flow/userinfo";
        Map<String, String> headers = new HashMap<>();
//        String authCode = getAuthCode(serviceNumber, timestamp, USERINFO_CODE);
        headers.put("authCode", authCode);
        headers.put("appKey", appKey);
        headers.put("token", getToken(timestamp));
        headers.put("timestamp", timestamp);
        String params = "serviceType=" + serviceType + "&serviceNumber=" + serviceNumber;
        String result = sendGet(url, params, headers);
        System.out.println("getUserinfo_result : " + result);
        return result;
    }


    public static String getBalance(String serviceNumber, String serviceType, String authCode, String timestamp) {
//        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String url = baseUrl + "/account/balance2";
        Map<String, String> headers = new HashMap<>();
//        String authCode = getAuthCode(serviceNumber, timestamp, BALANCE_CODE);
        headers.put("token", getToken(timestamp));
        headers.put("appKey", appKey);
        headers.put("timestamp", timestamp);
        headers.put("authCode", authCode);
        String params = "serviceType=" + serviceType + "&serviceNumber=" + serviceNumber;
        String result = sendGet(url, params, headers);
        System.out.println("getBalance_result : " + result);
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, Map<String, String> headers) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 打开和URL之间的连接
            if (headers != null) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    connection.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection)
                        .setSSLSocketFactory(getSSLContext().getSocketFactory());
            }

            // 设置通用的请求属性
//
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                //System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应

            if (connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), "utf-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String getAuthCode(String mobile, String timestamp/*, String scope*/) throws Exception {
        String str = (String) RedisUtil.get("AuthCode_" + mobile);
        if (!StringUtils.isEmpty(str)) {
            return str;
        }
        String scope = ORDER_CODE + "\",\"" + BALANCE_CODE + "\",\"" + USERINFO_CODE + "\",\"" + INTEGRAL_CODE;
        String url = baseUrl + "/authcode/sms";
        String token = getToken(timestamp);
        String params = "mobile=" + mobile + "&scope=[\"" + scope + "\"]";
        Map<String, String> headers = new HashMap<>();

        headers.put("token", token);
        headers.put("appKey", appKey);
        headers.put("timestamp", timestamp);

        String result = sendGet(url, params, headers);
        System.out.println("authCodeResult ：" + result);
        Map<String, Object> map = JSON.parseObject(result);
        String randomCode = (String) map.get("randomCode");
        System.out.println("RandromCode : " + randomCode);
        Date date = new Date();
        String smsCode = null;
        try {
            for (int i = 0; i < 30; i++) {
                smsCode = messageService.getSmsVerifyCodeForJdbc(mobile, null, date);
                if (StringUtils.isNotEmpty(smsCode)) {
                    break;
                }
                Thread.sleep(2000l);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (smsCode == null) {
            throw new Exception("没有获取到验证码，请检查是否接收到验证码");
        }
        String authCode = randomCode + smsCode;
        RedisUtil.set("AuthCode_" + mobile, authCode, (long) (30  * 60));
        return authCode;
    }

    public static String getSecret(String timestamp) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String tag = appSecret + appKey + timestamp;
        MessageDigest digest;
        digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(tag.getBytes("UTF-8"));
        String output = Hex.encodeHexString(hash);
        return output;
    }

    public static String getParams(String timestamp) {
        String params = "appKey=" + appKey + "&timestamp=" + timestamp;
        return params;
    }

    //获取token   baseUrl/token // 参数：secret ，appKey，timestamp
    public static String getToken(String timestamp) {
        long curtime = System.currentTimeMillis();
        if (curtime - tokenTime < tokenStarttime) {
            return token;
        }
        String url = baseUrl + "/token";
        System.out.println("获取token的url :" + url);
        Map<String, String> headerMap = new HashMap<>();
        String params = "";
        try {
            params = "appKey=" + appKey + "&timestamp=" + timestamp + "&secret=" + getSecret(timestamp);
            System.out.println("获取token的url :" + url + "?" + params);
            String result = sendGet(url, params, null);
            System.out.println("tokeResult : " + result);
            Map<String, Object> map = JSON.parseObject(result);
            String token = (String) map.get("token");
            HttpTest.token = token;
            HttpTest.tokenStarttime = curtime;
            System.out.println("token : " + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn)
                        .setSSLSocketFactory(getSSLContext().getSocketFactory());
            }
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
            } else {
                in = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream()));
            }
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static SSLContext getSSLContext() {
        long time1 = System.currentTimeMillis();
        try {
            sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");
            ks.load(HttpTest.class.getResourceAsStream("/" + KEY_STORE_FILE), KEY_STORE_PASS.toCharArray());
            tks.load(HttpTest.class.getResourceAsStream("/" + TRUST_STORE_FILE), TRUST_STORE_PASS.toCharArray());
            kmf.init(ks, KEY_STORE_PASS.toCharArray());
            tmf.init(tks);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long time2 = System.currentTimeMillis();
        System.out.println("SSLContext 初始化时间：" + (time2 - time1));
        return sslContext;
    }

    public static SSLContext getSSLContext1() {
        long time1 = System.currentTimeMillis();
        if (sslContext == null) {
            try {
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(getKeyStore(), KEY_STORE_PASS.toCharArray());
                KeyManager[] keyManagers = kmf.getKeyManagers();

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(getTrustStore());
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, new SecureRandom());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        long time2 = System.currentTimeMillis();
        System.out.println("SSLContext 初始化时间：" + (time2 - time1));
        return sslContext;
    }


    public static KeyStore getKeyStore() {
        KeyStore keySotre = null;
        try {
            keySotre = KeyStore.getInstance("JKS");
            InputStream is = HttpTest.class.getResourceAsStream("/" + KEY_STORE_FILE);
            keySotre.load(is, KEY_STORE_PASS.toCharArray());
            is.close();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keySotre;
    }

    public static KeyStore getTrustStore() throws IOException {
        KeyStore trustKeyStore = null;
        FileInputStream fis = null;
        try {
            trustKeyStore = KeyStore.getInstance("JKS");
            fis = new FileInputStream(new File(TRUST_STORE_FILE));
            trustKeyStore.load(fis, TRUST_STORE_PASS.toCharArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return trustKeyStore;
    }
}