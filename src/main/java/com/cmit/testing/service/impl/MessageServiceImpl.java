package com.cmit.testing.service.impl;

import com.cmit.testing.dao.MessageMapper;
import com.cmit.testing.dao.SendMessageTaskMapper;
import com.cmit.testing.dao.SimCardMapper;
import com.cmit.testing.dao.SimEquipmentMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.enums.ResultEnum;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.MessageService;
import com.cmit.testing.service.app.ProvideWebService;
import com.cmit.testing.utils.DateUtil;
import com.cmit.testing.utils.JDBCUtils;
import com.cmit.testing.utils.SMSUtil;
import com.cmit.testing.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suviky on 2018/7/24 10:03
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SimEquipmentMapper simEquipmentMapper;

    @Autowired
    private SendMessageTaskMapper sendMessageTaskMapper;
    @Autowired
    private SimCardMapper simCardMapper;
    @Autowired
    private ProvideWebService provideWebService;
    private static final int minute = 60000;
    private static final int order = 0;
    private static final int unsubscribe = 1;
    private static final int fail = 0;
    private static final int success = 1;

    @Override
    public PageInfo<Message> getMsgList(int pageNum, int pageSize,Message record) {
        PageHelper.startPage(pageNum,pageSize);
        List<Message> list = messageMapper.selectByBlurInfo(record);
        PageInfo<Message> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public String getSmsVerifyCode(String phone, String matchKeyNecessity,String matchKeySelectivity,Date date) {
        String startTime = DateUtil.reduceTime(date, 3000);
        String endTime = DateUtil.addTime(date, 60000);
        String rtn = null;
        List<Message> messages = messageMapper.selectBySmsVerifyCode(phone,startTime,endTime);
        if (messages != null && messages.size() > 0) {
            for (Message message : messages) {
                rtn = SMSUtil.subStringVerficationCode(message, matchKeyNecessity,matchKeySelectivity);
                if(StringUtils.isNotEmpty(rtn)){
                    message.setType(0);
                    messageMapper.updateByPrimaryKey(message);
                    break;
                }
            }
        }
        return rtn;
    }

    @Override
    public String getSmsVerifyCodeForJdbc(String phone, String title,Date date) throws SQLException {
        String rtn = null;
        String startTime = DateUtil.reduceTime(date, 3000);
        String endTime = DateUtil.addTime(date, 60000);

        Connection connection = JDBCUtils.getConnection();
        Statement statement = connection.createStatement();
        String querySql = "select * from message where receivetime >= '"+startTime+"' and receivetime <= '"+endTime+"' and phone = '"+phone+"' and type = '1'";
        ResultSet rs = statement.executeQuery(querySql);

        ArrayList<Message> messagesList = new ArrayList<>();
        while (rs.next()){
            String content = rs.getString("content");
            String id = rs.getString("id");
            Message message = new Message();
            message.setId(Integer.parseInt(id));
            message.setContent(content);
            messagesList.add(message);
        }

        if (messagesList != null && messagesList.size() > 0) {
            for (Message message : messagesList) {
                rtn = SMSUtil.subStringVerficationCode(message, null,null);
                if(StringUtils.isNotEmpty(rtn)){
                    String updateSql = "update message set type = '0' where id = '"+message.getId()+"'";
                    statement.execute(updateSql);
                    break;
                }
            }
        }

        JDBCUtils.colseResource(connection,statement,rs);
        return rtn;
    }

    public PageBean<Message> selectAll(PageBean<Message> pageBean , Message message){
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<Message> list = messageMapper.selectAll();
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public PageBean<Message> findByPage(PageBean<Message> pageBean , Message message) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<Message> list = messageMapper.findByPage(message);
        pageBean.setTotalNum((int)page.getTotal());
        for(Message message1 : list){
           SimEquipment simEquipment = simEquipmentMapper.selectByPrimaryKey(message1.getEquipmentid());
           message1.setSimEquipment(simEquipment);
        }
        pageBean.setItems(list);
        return pageBean;
    }

    @Override
    public int checkSmsContent(String phone, String title, Date date) {
        String startTime = DateUtil.reduceTime(date, 3000);
        String endTime = DateUtil.addTime(date, 60000);

        List<Message> messages = messageMapper.selectBySmsVerifyCode(phone,startTime,endTime);
        if(messages!= null&&messages.size()>0){
            for (Message message : messages) {
                int number = SMSUtil.checkSmsContent(message, title);
                if(number == 1){
                    message.setType(0);
                    messageMapper.updateByPrimaryKey(message);
                    return 1;
                }
            }

        }
        return 0;
    }

    @Override
    public int sendWlanMessage(SendWlanMessage sendWlanMessage) throws InterruptedException {
        boolean flag = false;
        if(sendWlanMessage== null){
            return 0;
        }
        //订购
        if(sendWlanMessage.getType()==order){
            //订购
            SendMessage sendMessageTask = new SendMessage();
            sendMessageTask.setEquipmentType(2);
            sendMessageTask.setReceiverPhone(sendWlanMessage.getReceiverPhone());
            sendMessageTask.setSenderPhone(sendWlanMessage.getSenderPhone());
            sendMessageTask.setText(sendWlanMessage.getSendOrderText());
            sendMessageTaskMapper.insert(sendMessageTask);
            Date confirmOrderDate = new Date();
            String startTime = DateUtil.reduceTime(confirmOrderDate, 3000);
            String endTime = DateUtil.addTime(confirmOrderDate, 60000*5);

            for (int i = 0; i < 180 ; i++) {
                    //校验回复短信内容
                    List<Message> messages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(),startTime,endTime);
                    for (Message message : messages) {
                        //校验是否是确认短信
                        int isConfirmOrder = SMSUtil.checkSmsContent(message, sendWlanMessage.getConfirmOrderString());
                        if(isConfirmOrder == 1){
                            SendMessage sendConfirmOrderMessage = new SendMessage();
                            sendConfirmOrderMessage.setEquipmentType(2);
                            sendConfirmOrderMessage.setSenderPhone(sendWlanMessage.getSenderPhone());
                            sendConfirmOrderMessage.setReceiverPhone(message.getOthernumber());
                            sendConfirmOrderMessage.setText(sendWlanMessage.getSendConfirmOrderText());
                            sendMessageTaskMapper.insert(sendConfirmOrderMessage);
                            Date confirmOrderSuccessDate = new Date();
                            String start2 = DateUtil.reduceTime(confirmOrderSuccessDate, 3000);
                            String end2 = DateUtil.addTime(confirmOrderSuccessDate, 60000*5);


                            for (int j = 0; j < 180 ; j++) {
                                flag = true;
                                //校验订购成功短信内容
                                List<Message> successMessages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(),start2,end2);
                                for (Message successMessage : successMessages) {
                                    int isConfirmOrderSuccess = SMSUtil.checkSmsContent(successMessage, sendWlanMessage.getConfirmOrderResulString());
                                    if(isConfirmOrderSuccess == 1){
                                        return 1;
                                    }
                                }
                                Thread.sleep(2000);
                            }
                        }
                    }

                if(flag == true){
                    break;
                }
                Thread.sleep(2000);
            }
        }else if(sendWlanMessage.getType() == unsubscribe){
            //退订
            SendMessage sendMessageTask = new SendMessage();
            sendMessageTask.setEquipmentType(2);
            sendMessageTask.setReceiverPhone(sendWlanMessage.getReceiverPhone());
            sendMessageTask.setSenderPhone(sendWlanMessage.getSenderPhone());
            sendMessageTask.setText(sendWlanMessage.getUnsubscribeText());
            sendMessageTaskMapper.insert(sendMessageTask);
            Date date = new Date();
            String startTime = DateUtil.reduceTime(date, 3000);
            String endTime = DateUtil.addTime(date, 60000*5);

            //校验是否退订成功
            for (int o = 0; o < 180 ; o++) {
                List<Message> messages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(), startTime,endTime);
                for (Message message : messages) {
                    int isUnsubscribeSuccess = SMSUtil.checkSmsContent(message, sendWlanMessage.getUnsubscribeConfirmResulString());
                    if(isUnsubscribeSuccess == 1){
                        return 1;
                    }
                }
                Thread.sleep(2000);
            }
        }
        return 0;
    }

    @Override
    public int orderBusiness(SendWlanMessage sendWlanMessage) {
        try {
            boolean flag = false;
            if(sendWlanMessage.getType() == order){
                Date confirmOrderDate = new Date();
                String startTime = DateUtil.reduceTime(confirmOrderDate, 3000);
                String endTime = DateUtil.addTime(confirmOrderDate, 60000*5);
                for (int i = 0; i < 150 ; i++) {
                    //校验回复短信内容
                    List<Message> messages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(),startTime,endTime);
                    for (Message message : messages) {
                        //校验是否是确认短信
                        int isConfirmOrder = SMSUtil.checkSmsContent(message, sendWlanMessage.getConfirmOrderString());
                        if(isConfirmOrder == 1){
                            SendMessage sendConfirmOrderMessage = new SendMessage();
                            sendConfirmOrderMessage.setSenderPhone(sendWlanMessage.getSenderPhone());
                            sendConfirmOrderMessage.setReceiverPhone(message.getOthernumber());
                            sendConfirmOrderMessage.setText(sendWlanMessage.getSendConfirmOrderText());
                            sendConfirmOrderMessage.setEquipmentType(2);
                            sendMessageTaskMapper.insert(sendConfirmOrderMessage);
                            Date confirmOrderSuccessDate = new Date();
                            String start2 = DateUtil.reduceTime(confirmOrderSuccessDate, 3000);
                            String end2 = DateUtil.addTime(confirmOrderSuccessDate, 60000*5);

                            for (int j = 0; j < 150 ; j++) {
                                flag = true;
                                //校验订购成功短信内容
                                List<Message> successMessages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(),start2,end2);
                                for (Message successMessage : successMessages) {
                                    int isConfirmOrderSuccess = SMSUtil.checkSmsContent(successMessage, sendWlanMessage.getConfirmOrderResulString());
                                    if(isConfirmOrderSuccess == 1){
                                        return success;
                                    }
                                }
                                Thread.sleep(2000);
                            }
                        }
                    }

                    if(flag == true){
                        break;
                    }
                    Thread.sleep(2000);
                }
            }else if(sendWlanMessage.getType() == unsubscribe){
                Date date = new Date();
                String startTime = DateUtil.reduceTime(date, 5000);
                String endTime = DateUtil.addTime(date, 60000*5);

                //校验是否退订成功
                for (int i = 0; i < 150 ; i++) {
                    List<Message> messages = messageMapper.selectBySmsVerifyCode(sendWlanMessage.getSenderPhone(), startTime,endTime);
                    for (Message message : messages) {
                        int isUnsubscribeSuccess = SMSUtil.checkSmsContent(message, sendWlanMessage.getUnsubscribeConfirmResulString());
                        if(isUnsubscribeSuccess == 1){
                            return success;
                        }
                    }
                    Thread.sleep(2000);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fail;
    }

    @Override
    public void sendMessage(String receiverPhone, String text, String senderPhone) {
        SimCard simCard = simCardMapper.getByPhone(senderPhone);
        if (simCard != null) {
            if (simCard.getDisableStatus() == 1) {
                // web
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.setReceiverPhone(receiverPhone);
                sendMessageTask.setSenderPhone(senderPhone);
                sendMessageTask.setText(text);
                sendMessageTask.setEquipmentType(2);
                sendMessageTaskMapper.insert(sendMessageTask);
            } else if (simCard.getDisableStatus() == 2) {
                //app
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.setReceiverPhone(receiverPhone);
                sendMessageTask.setSenderPhone(senderPhone);
                sendMessageTask.setText(text);
                sendMessageTask.setEquipmentType(1);
                sendMessageTaskMapper.insert(sendMessageTask);
                // 这里只要往表中插入数据即可，无需调发送短信接口
                //provideWebService.sendMessageTaskToProxy(sendMessageTask);
            } else if (simCard.getDisableStatus() == 0) {
            	throw new TestSystemException(ResultEnum.PHONE_DISABLE_STATUS_CLOSED);
			}
        }

    }

    @Override
    public Message checkMessage(String phone,String matchKeyNecessity,String matchKeySelectivity,int time) throws InterruptedException {
        //循环次数
        int number = time * 12;
        for (int i = 0; i < number ; i++) {
            Date date = new Date();
            String startTime = DateUtil.reduceTime(date, 5000);
            String endTime = DateUtil.addTime(date, minute * time);
            List<Message> messages = messageMapper.selectBySmsVerifyCode(phone, startTime, endTime);
            for (Message message : messages) {
                Message messageResult = SMSUtil.checkSmsContentByWhere(message, matchKeyNecessity, matchKeySelectivity);
                if (messageResult != null) {
                    return messageResult;
                }
            }
            Thread.sleep(5000);
        }
        return null;
    }
}
