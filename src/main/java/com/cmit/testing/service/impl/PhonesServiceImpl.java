package com.cmit.testing.service.impl;

import com.cmit.testing.dao.PhonesMapper;
import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.Phones;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.PhonesService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Suviky on 2018/7/18 14:51
 */
@Service
public class PhonesServiceImpl extends BaseServiceImpl<Phones> implements PhonesService {

    @Autowired
    private PhonesMapper phonesMapper;

    @Override
    public List<Phones> selectAll() {
        return null;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return phonesMapper.deleteByIds(ids);
    }

    @Override
    public List<Phones> getPhones(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Phones> list = phonesMapper.selectAll();
        return list;
    }

    @Override
    public PageBean<Phones> findByPage(PageBean<Phones> pageBean, Phones phones) {
        Page page = PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        List<Phones> list = phonesMapper.findByPage();
        pageBean.setTotalNum((int)page.getTotal());
        pageBean.setItems(list);
        return pageBean;
    }
}
