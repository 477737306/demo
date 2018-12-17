package com.cmit.testing.service;

/**
 * Service基础类
 *
 * @author YangWanLi
 * @date 2018/8/7 15:53
 */
public interface BaseService<T> {
    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
