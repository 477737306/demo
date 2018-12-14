package com.cmit.testing.dao;

import com.cmit.testing.entity.OrderRelationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderRelationshipMapper extends BaseMapper<OrderRelationship> {

    List<OrderRelationship> findByPage(OrderRelationship orderRelationship);

    int removeByPhoneAndType(@Param("phone") String phone, @Param("type") String type);

    int removeByPhone(@Param("phone") String phone);

    List<OrderRelationship> findByPhone(@Param("phone") String phone);

}