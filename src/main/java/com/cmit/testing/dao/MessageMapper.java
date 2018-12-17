package com.cmit.testing.dao;

import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.SimCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Suviky on 2018/7/24 10:03
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message>{

    List<Message> selectAll();

    List<Message> selectByBlurInfo(Message record);

    List<Message> selectBySmsVerifyCode(@Param("phone") String phone, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 分页查询所有
     * @param message
     * @return
     */
    List<Message> findByPage(Message message);
}