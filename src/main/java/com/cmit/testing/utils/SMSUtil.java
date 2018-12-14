package com.cmit.testing.utils;

import com.cmit.testing.entity.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSUtil {


    /**
     * 根据手机号+时间戳+验证码 截取最新验证码
     *
     * @return
     */
    public static String subStringVerficationCode(Message messages, String matchKeyNecessity,String matchKeySelectivity) {
        int flag = 0;
        StringBuffer code = null;
        String content = messages.getContent();
        if (content.contains("动态密码") || content.contains("短信码") || content.contains("短信随机码") || content.contains("验证码") || content.contains("短信授权码")) {
            if (StringUtils.isEmpty(matchKeyNecessity) && StringUtils.isEmpty(matchKeySelectivity)) {
                return getCode(content).toString();
            }
            if (StringUtils.isNotEmpty(matchKeyNecessity)) {
                String[] matchKeyNecessityArr = matchKeyNecessity.split("_");
                for (int i = 0; i < matchKeyNecessityArr.length; i++) {
                    if (!content.contains(matchKeyNecessityArr[i])) {
                        flag = 1;
                    }
                }
            }else{
                flag = 2;
            }

            if (flag == 0) {
                return getCode(content).toString();
            } else if (flag == 1 || flag == 2) {
                if (StringUtils.isNotEmpty(matchKeySelectivity)) {
                    String[] matchKeySelectivityArr = matchKeyNecessity.split("_");
                    for (int i = 0; i < matchKeySelectivityArr.length; i++) {
                        if (content.contains(matchKeySelectivityArr[i])) {
                            return getCode(content).toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "你的动态密码是:234";
        //System.out.println(subStringVerficationCode(str,""));
    }

    public static String getyzm(String body) {
//     Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + YZMLENGTH+ "})(?![0-9])");
        Pattern p = Pattern.compile("[0-9\\.]+");
        Matcher matcher = p.matcher(body);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static StringBuffer getCode(String content) {
        StringBuffer stringBuffer = new StringBuffer();
        if (content.contains("动态密码") || content.contains("短信码") || content.contains("短信随机码") || content.contains("验证码") || content.contains("短信授权码")) {
            for (int j = 0; j < content.length(); j++) {
                if (content.charAt(j) != '\0' && Character.isDigit(content.charAt(j))) {
                    stringBuffer.append(String.valueOf(content.charAt(j)));
                } else {
                    if (stringBuffer.length() > 3) {
                        if (!"年".equals(String.valueOf(content.charAt(j)))&&!"-".equals(String.valueOf(content.charAt(j)))) {
                            break;
                        }
                    }
                    stringBuffer.setLength(0);
                }
            }
        }
        return stringBuffer;
    }

    /**
     * 校验短信内容
     *
     * @param messages
     * @param title
     */
    public static int checkSmsContent(Message messages, String title) {
            for (int i = title.length(); i > 3; i--) {
                if (messages.getContent().contains(title.substring(0, i))) {
                    return 1;
                }
            }
        return 0;
    }

    /**
     * 校验复杂条件的短信内容
     *  @param messages
     * @param with
     * @param or
     */
    public static Message checkSmsContentByWhere(Message messages, String with, String or) {
        int flag = 1;

        if (with != null) {
            String[] withArr = with.split("_");
            for (int i = 0; i < withArr.length; i++) {
                if (!messages.getContent().contains(withArr[i])) {
                    flag = 0;
                }
            }
        }

        if (flag == 1) {
            return messages;
        } else if (flag == 0) {
            if (or != null) {
                String[] orArr = or.split("_");
                for (int i = 0; i < orArr.length; i++) {
                    if (messages.getContent().contains(orArr[i])) {
                        return messages;
                    }
                }
            }
        }
        return null;
    }

}
