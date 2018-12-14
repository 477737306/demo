package com.cmit.testing.service;

import com.cmit.testing.entity.OrderRelationship;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;

import java.util.List;

public interface OrderRelationshipService extends BaseService<OrderRelationship>{

    /**
     * 分页查询
     * @param pageBean
     * @param orderRelationship
     * @return
     */
    PageBean<OrderRelationship> findByPage(PageBean<OrderRelationship> pageBean, OrderRelationship orderRelationship);

    void updateAllLocalData(String simCardIds);

    List<OrderRelationship> findByPhone(String phone);
}
