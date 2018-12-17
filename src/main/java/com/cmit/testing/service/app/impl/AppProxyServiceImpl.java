package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppProxyMapper;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppProxy;
import com.cmit.testing.service.app.AppProxyService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/4 0004 下午 2:37.
 */
@Service("appProxyService")
public class AppProxyServiceImpl extends BaseServiceImpl<AppProxy> implements AppProxyService {

    @Autowired
    private AppProxyMapper appProxyMapper;

    @Override
    public int insert(AppProxy record) {
        return appProxyMapper.insert(record);
    }

    @Override
    public int insertSelective(AppProxy record) {
        return appProxyMapper.insertSelective(record);
    }

    @Override
    public AppProxy selectByPrimaryKey(Integer id) {
        return appProxyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AppProxy record) {
        return appProxyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AppProxy record) {
        return appProxyMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByProxyIpAndMac(AppProxy ap) {
        return appProxyMapper.updateByProxyIpAndMac(ap);
    }

    @Override
    public List<AppProxy> getList(AppProxy ap) {
        return appProxyMapper.getList(ap);
    }

    @Override
    public AppProxy getProxyByIpAndMac(AppProxy ap) {
        return appProxyMapper.getProxyByIpAndMac(ap);
    }
}
