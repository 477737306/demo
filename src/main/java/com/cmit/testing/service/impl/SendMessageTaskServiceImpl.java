package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SendMessageTaskMapper;
import com.cmit.testing.entity.SendMessage;
import com.cmit.testing.service.SendMessageTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SendMessageTaskServiceImpl implements SendMessageTaskService {

    @Autowired
    private SendMessageTaskMapper sendMessageTaskMapper;

    @Override
    public List<SendMessage> queryTask() {
        return sendMessageTaskMapper.queryTask();
    }

    @Override
    public void update(SendMessage sendMessage) {
        sendMessageTaskMapper.update(sendMessage);
    }

    @Override
    public void insert(SendMessage sendMessage) {
        sendMessageTaskMapper.insert(sendMessage);
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        sendMessageTaskMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SendMessage> queryByWhere(SendMessage sendMessage) {
        return sendMessageTaskMapper.queryByWhere(sendMessage);
    }
}
