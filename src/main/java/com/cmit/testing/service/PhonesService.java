package com.cmit.testing.service;

import com.cmit.testing.entity.Phones;
import com.cmit.testing.page.PageBean;

import java.util.List;

/**
 * Created by Suviky on 2018/7/18 14:50
 */
public interface PhonesService extends BaseService<Phones>{

    List<Phones> selectAll();

    /**
     * 根据手机Id批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    List<Phones> getPhones(Integer pageNum, Integer pageSize);

    PageBean<Phones> findByPage(PageBean<Phones> pageBean, Phones phones);
}
