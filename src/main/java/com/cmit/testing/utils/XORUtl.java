package com.cmit.testing.utils;

public class XORUtl {
    public static final int KEY = 7;

    public static void main(String[] args) {
        String account = "lwj";
        String password = "1234";
        String secretKey= XORUtl.encry(account+"@"+password);
        System.out.println("原始 的字符串为:" + account+"@"+password);
        System.out.println("原始 的字符串为:" +"sbtsGFdc&576?");
        System.out.println("加密后 的字符串为:" + secretKey);
        System.out.println("解密后 的字符串为:" + XORUtl.decryPwd("sbtsGFdc&576?"));
        String[] split = XORUtl.decryPwd(secretKey).split("@");
        System.out.println(split[0]);
        System.out.println(split[1]);
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encry(String str) {
        StringBuilder str2 = new StringBuilder();
        //加密过程
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ KEY);
            str2.append(c);
        }
        return str2.toString();
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static String decryPwd(String str) {
        StringBuilder str3 = new StringBuilder();
        //解密过程
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ KEY);
            str3.append(c);
        }
        return str3.toString();
    }
}