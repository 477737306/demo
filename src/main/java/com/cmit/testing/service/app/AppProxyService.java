package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppProxy;
import com.cmit.testing.service.BaseService;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/4 0004 下午 2:22.
 */
public interface AppProxyService extends BaseService<AppProxy>  {

    int deleteByPrimaryKey(Integer id);

    int insert(AppProxy record);

    int insertSelective(AppProxy record);

    AppProxy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppProxy record);

    int updateByPrimaryKey(AppProxy record);

    int updateByProxyIpAndMac(AppProxy ap);

    List<AppProxy> getList(AppProxy ap);

    AppProxy getProxyByIpAndMac(AppProxy ap);

}
