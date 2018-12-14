package com.cmit.testing.service;

import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.SendWlanMessage;
import com.cmit.testing.page.PageBean;
import com.github.pagehelper.PageInfo;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Suviky on 2018/7/24 10:03
 */
public interface MessageService extends BaseService<Message>{

    PageInfo<Message> getMsgList(int pageNum, int pageSize, Message record);

    /**
     * 获取短信验证码
     * @param phone
     * @param matchKeyNecessity
     * @param matchKeySelectivity
     * @return
     */
    String getSmsVerifyCode(String phone, String matchKeyNecessity, String matchKeySelectivity, Date date);

    String getSmsVerifyCodeForJdbc(String phone, String title, Date date) throws SQLException;

    PageBean<Message> selectAll(PageBean<Message> pageBean, Message message);

    /**
     * 分页查询
     * @param pageBean
     * @param message
     * @return
     */
    PageBean<Message> findByPage(PageBean<Message> pageBean, Message message);

    /**
     * 校验短信内容
     * @param phone
     * @param title
     */
    int checkSmsContent(String phone, String title, Date time);

    int  sendWlanMessage(SendWlanMessage sendWlanMessage) throws InterruptedException;

    /**
     * 订购WLAN以及订购结果校验
     * @param sendWlanMessage
     * @return
     */
    int orderBusiness(SendWlanMessage sendWlanMessage);

    /**
     * 发送短信
     * @param receiverPhone 接收方手机号码
     * @param text 短信内容
     * @param senderPhone 发送方手机号码
     * @param
     */
    void sendMessage(String receiverPhone, String text, String senderPhone);

    /**
     * 校验短信
     * @param phone 手机号码
     * @param matchKeyNecessity 匹配必要条件
     * @param matchKeySelectivity 匹配选择性条件
     * @param time 延时时长
     */
    Message checkMessage(String phone, String matchKeyNecessity, String matchKeySelectivity, int time) throws InterruptedException;
}
