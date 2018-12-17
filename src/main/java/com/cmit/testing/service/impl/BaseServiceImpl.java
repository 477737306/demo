package com.cmit.testing.service.impl;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.BaseService;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * ServiceImpl 基础类
 *
 * @author YangWanLi
 * @date 2018/8/7 15:55
 */
@Service

public class BaseServiceImpl<T> implements BaseService<T> {

    public static Logger logger = LoggerFactory.getLogger(Class.class);

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        return baseMapper.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return baseMapper.insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(Integer id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public ShiroUser getShiroUser(){
        return ShiroKit.getUser();
    }

    /**
     * 获取Session
     * @return
     */
    public Session getSession(){
        return ShiroKit.getSession();
    }

    /**
     * 获取SubJect
     * @return
     */
    public Subject getSubJect(){
        return ShiroKit.getSubject();
    }
}
