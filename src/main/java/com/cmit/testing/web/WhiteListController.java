package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.vo.WhiteListVO;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author: create by YangWanLi
 * @version: v1.0
 * @description: com.cmit.testing.web 白名单
 * @date:2018/12/11
 */
@RestController
@RequestMapping("/api/v1/whiteList")
@SystemLog("白名单")
@Permission
public class WhiteListController {

    /**
     * 获取全部
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public JsonResultUtil findAll(){
        HashSet<WhiteListVO> whiteListVOList = new HashSet<>();
        Map<String,WhiteListVO> map = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);
        for (WhiteListVO v : map.values()) {
            whiteListVOList.add(v);
        }
        return new JsonResultUtil(200,"操作成功",whiteListVOList);
    }

    /**
     * 获取
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResultUtil get(@PathVariable("id") String id){
        Map<String,WhiteListVO> map = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);
        return new JsonResultUtil(200,"操作成功",map.get(id));
    }

    /**
     * 保存
     * @param whiteListVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResultUtil save(@RequestBody WhiteListVO whiteListVO){
        Map<String,WhiteListVO> map = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);
        String id = whiteListVO.getId();
        if(StringUtils.isEmpty(id)) {
            id = StringUtils.generateUUID();
            whiteListVO.setId(id);
        }
        map.put(id,whiteListVO);
        RedisUtil.setObject(Constants.ORIGIN_KEY,map);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public JsonResultUtil delete(@PathVariable("id") String id){
        Map<String,WhiteListVO> map = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);
        map.remove(id);
        RedisUtil.setObject(Constants.ORIGIN_KEY,map);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 批量删除
     * @param map
     */
    @RequestMapping(value = "/deleteByIds",method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String ,Object> map){
        List<String> ids  = (List<String>) map.get("ids");
        Map<String,WhiteListVO> map_ = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);

        for (String id : ids) {
            map_.remove(id);
        }
        RedisUtil.setObject(Constants.ORIGIN_KEY,map_);
        return new JsonResultUtil(200,"操作成功");
    }
}
