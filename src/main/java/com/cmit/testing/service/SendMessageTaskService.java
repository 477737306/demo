package com.cmit.testing.service;

import com.cmit.testing.entity.SendMessage;

import java.util.List;

public interface SendMessageTaskService {

    /**
     * 查询短信任务
     *
     * @return
     */
    List<SendMessage> queryTask();

    /**
     * 更新
     *
     * @param sendMessage
     */
    void update(SendMessage sendMessage);

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
