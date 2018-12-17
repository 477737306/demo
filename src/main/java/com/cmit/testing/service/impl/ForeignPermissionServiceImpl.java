package com.cmit.testing.service.impl;

import com.cmit.testing.exception.NoForeignPermissionException;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.ForeignPermissionService;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对外鉴权
 *
 * @author YangWanLi
 * @date 2018/9/12 20:06
 */
@Service
public class ForeignPermissionServiceImpl implements ForeignPermissionService {


    /**
     * 获取token
     * @param key
     * @return
     */
    @Override
    public String getForeignToken(String key,String time,String token) {
        if(!Constants.FOREIGN_PERMISSION_KEY.equals(key)){
            throw new NoForeignPermissionException("鉴权失败！");
        }
        Map<String,Object> map = new HashMap<>();
        //生成foreignToken
//        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        String foreignToken = ShiroKit.md5(key+time,Constants.FOREIGN_PERMISSION_KEY);
        if(!foreignToken.equals(token)){
            throw new NoForeignPermissionException("鉴权失败！");
        }
        map.put("key",key);
        map.put("time",time);
        map.put("date",new Date());
        //reids缓存token
        RedisUtil.setObject(foreignToken,map);
        return foreignToken;
    }

    /**
     * 验证权限
     * @param foreignToken
     * @return
     */
    @Override
    public Boolean checkForeignPermission(String foreignToken) {
        Map<String,Object> map = (Map<String,Object>) RedisUtil.getObject(foreignToken);
        if(map==null){
            return false;
        }
        Date oldDate =(Date) map.get("date");
        Date nowDate = new Date();
        Long longTmie = nowDate.getTime() - oldDate.getTime();
        if(longTmie>=Constants.FAILURE_TIME){ //token失效
            RedisUtil.delkeyObject(foreignToken);
            return false;
        }
        return true;
    }
}
