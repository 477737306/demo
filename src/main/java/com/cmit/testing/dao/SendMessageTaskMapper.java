package com.cmit.testing.dao;

import com.cmit.testing.entity.SendMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SendMessageTaskMapper {

    /**
     * 查询短信任务
     *
     * @return
     */
    List<SendMessage> queryTask();

    /**
     * 查询手机设备的短信任务
     * @return
     */
    List<SendMessage> queryAppTask();

    /**
     * 更新
     *
     * @param sendMessage
     */
    void updateStatus(SendMessage sendMessage);

    /**
     * 更新
     * @param sendMessage
     * @return
     */
    int update(SendMessage sendMessage);

    /**
     * 插入
     *
     * @param sendMessage
     */
    void insert(SendMessage sendMessage);

    /**
     * 删除
     *
     * @param id
     */
    void deleteByPrimaryKey(Integer id);

    /**
     * 根据条件查询
     *
     * @param sendMessage
     * @return
     */
    List<SendMessage> queryByWhere(SendMessage sendMessage);
}
