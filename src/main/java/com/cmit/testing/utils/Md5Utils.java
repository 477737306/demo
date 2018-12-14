package com.cmit.testing.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author XieZuLiang
 * @description TODO MD5工具类，提供字符串MD5加密（校验）、文件MD5值获取（校验）功能。
 * @date 2018/8/30 0030 上午 10:02.
 */
public class Md5Utils {
    /**
     * 16进制字符集
     *
     */
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    /**
     * 指定算法为MD5的MessageDigest
     *
     */
    private static MessageDigest messageDigest = null;
    /**
     * 初始化messageDigest的加密算法为MD5
     *
     */
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件的MD5值
     * @param path  目标文件路径
     * @return  MD5字符串
     */
    public static String getFileMD5Str(String path){
        FileInputStream fis = null;
        String md5Code = "";
        try {
            fis = new FileInputStream(path);
            md5Code = DigestUtils.md5Hex(fis);
            return md5Code;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     *            目标文件
     * @return MD5字符串
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     *
     * 获取文件的MD5值
     *
     * @param fileName
     *            目标文件的完整名称
     * @return MD5字符串
     */
    public static String getFileMD5String(String fileName){
        //return getFileMD5(new File(fileName));
        return getFileMD5Str(fileName);
    }

    /**
     *
     * MD5加密字符串
     *
     * @param str
     *            目标字符串
     * @return MD5加密后的字符串
     */
    public static String getMD5String(String str) {
        return getMD5String(str.getBytes());
    }

    /**
     *
     * MD5加密以byte数组表示的字符串
     *
     * @param bytes
     *            目标byte数组
     * @return MD5加密后的字符串
     */
    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }

    /**
     *
     * 校验密码与其MD5是否一致
     *
     * @param pwd
     *            密码字符串
     * @param md5
     *            基准MD5值
     * @return 检验结果
     */
    public static boolean checkPassword(String pwd, String md5) {
        return getMD5String(pwd).equalsIgnoreCase(md5);
    }

    /**
     *
     * 校验密码与其MD5是否一致
     *
     * @param pwd
     *            以字符数组表示的密码
     * @param md5
     *            基准MD5值
     * @return 检验结果
     */
    public static boolean checkPassword(char[] pwd, String md5) {
        return checkPassword(new String(pwd), md5);
    }

    /**
     *
     * 检验文件的MD5值
     *
     * @param file
     *            目标文件
     *
     * @param md5
     *            基准MD5值
     *
     * @return 检验结果
     *
     */
    public static boolean checkFileMD5(File file, String md5) {
        return getFileMD5(file).equalsIgnoreCase(md5);
    }

    /**
     *
     * 检验文件的MD5值
     *
     * @param fileName
     *            目标文件的完整名称
     *
     * @param md5
     *            基准MD5值
     *
     * @return 检验结果
     *
     */
    public static boolean checkFileMD5(String fileName, String md5) {
        return checkFileMD5(new File(fileName), md5);
    }

    /**
     *
     * 将字节数组转换成16进制字符串
     *
     * @param bytes
     *            目标字节数组
     *
     * @return 转换结果
     *
     */
    public static String bytesToHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    /**
     *
     * 将字节数组中指定区间的子数组转换成16进制字符串
     *
     * @param bytes
     *            目标字节数组
     *
     * @param start
     *            起始位置（包括该位置）
     *
     * @param end
     *            结束位置（不包括该位置）
     * @return 转换结果
     *
     */
    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();
    }
    public static String getSimenMD5(String strSrc) {
        String str1 = getMD5String(strSrc).toLowerCase();
        StringBuffer str2 = new StringBuffer();
        str2.append(str1, 0, 2).append("|")
                .append(str1, 4, 10).append("|")
                .append(str1, 8, 21).append("|")
                .append(str1, 14, 19).append("|")
                .append(str1, 1, 9).append("|")
                .append(str1, 24, 27);
        return getMD5String(str2.toString());
    }

    /**
     *
     * 将单个字节码转换成16进制字符串
     *
     * @param bt
     *            目标字节
     * @return 转换结果
     */
    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

}
