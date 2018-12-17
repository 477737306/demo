package com.cmit.testing.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 基础类
 * @author YangWanLi
 * @date 2018/8/7 15:58
 */
@Mapper
public interface BaseMapper<T> {
    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
