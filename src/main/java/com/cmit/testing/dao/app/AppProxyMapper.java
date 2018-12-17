package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppProxy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppProxyMapper extends BaseMapper<AppProxy>{

    int deleteByPrimaryKey(Integer proxyId);

    int insert(AppProxy record);

    int insertSelective(AppProxy record);

    AppProxy selectByPrimaryKey(Integer proxyId);

    int updateByPrimaryKeySelective(AppProxy record);

    int updateByPrimaryKey(AppProxy record);

    int updateByProxyIpAndMac(AppProxy ap);

    List<AppProxy> getList(AppProxy ap);

    AppProxy getProxyByIpAndMac(AppProxy ap);
}