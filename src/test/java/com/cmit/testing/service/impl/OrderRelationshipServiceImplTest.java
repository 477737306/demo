package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.entity.OrderRelationship;
import com.cmit.testing.service.OrderRelationshipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weiBin
 * @date 2018/9/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRelationshipServiceImplTest {
    @Autowired
    private OrderRelationshipService orderRelationshipService ;
    @Test
    public void test(){
        OrderRelationship orderRelationship = orderRelationshipService.selectByPrimaryKey(1636);
        System.out.println(JSON.toJSON(orderRelationship));
    }
}