package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.OrderRelationship;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.OrderRelationshipService;
import com.cmit.testing.utils.DateUtil;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/api/v1/orderRelationship")
@Permission
public class OrderRelationshipController extends BaseController {

    @Autowired
    private OrderRelationshipService orderRelationshipService;

    /**
     * 分页
     *
     * @param orderRelationship
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(PageBean<OrderRelationship> pageBean, OrderRelationship orderRelationship) {
        return new JsonResultUtil(200, "获取成功", orderRelationshipService.findByPage(pageBean, orderRelationship));
    }

    @RequestMapping(value = "/getOrderFlag", method = RequestMethod.GET)
    public JsonResultUtil getOrderFlag() {
        Object flag = RedisUtil.getObject("updateAllLocalData");
        Map<String, String> result = new ConcurrentHashMap<>();
        if (flag == null || "false".equals(flag)) {
            result.put("status", "false");
        } else {
            result.put("status", "true");
        }
        return new JsonResultUtil(200, "获取成功", result);
    }

    /**
     * 更新
     *
     * @return
     */
    @RequestMapping(value = "/updateAllLocalData", method = RequestMethod.GET)
    public JsonResultUtil updateAllLocalData(String simCardIds) {
        Map<String, String> result = new ConcurrentHashMap<>();
        if (StringUtils.isEmpty(simCardIds)) {
            //根据标识判断是否有在更新
            Object flag = RedisUtil.getObject("updateAllLocalData");
            if (flag == null || "false".equals(flag.toString())) {
                RedisUtil.set("orderRelationshipUpdate", DateUtil.getTime());
                RedisUtil.setObject("updateAllLocalData", "true");
                //更新数据
                orderRelationshipService.updateAllLocalData(null);
                RedisUtil.setObject("updateAllLocalData", "false");
                result.put("status", "false");
                return new JsonResultUtil(200, "操作成功", result);
            }
            result.put("status", "true");
        } else {
            //更新数据
            orderRelationshipService.updateAllLocalData(simCardIds);
        }
        return new JsonResultUtil(200, "操作成功", result);
    }

    @RequestMapping(value = "/orderRelationshipUpdate", method = RequestMethod.GET)
    public JsonResultUtil orderRelationshipUpdate() {
        return new JsonResultUtil(200, "操作成功", RedisUtil.get("orderRelationshipUpdate"));
    }

    /**
     * 根据主键Id获取数据
     *
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResultUtil findById(String id) {
        return new JsonResultUtil(200, "操作成功", orderRelationshipService.selectByPrimaryKey(Integer.parseInt(id)));
    }

    /**
     * 根据主键Id获取数据
     *
     * @return
     */
    @RequestMapping(value = "/findByPhone", method = RequestMethod.GET)
    public JsonResultUtil findByPhone(String phone) {
        return new JsonResultUtil(200, "操作成功", orderRelationshipService.findByPhone(phone));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody OrderRelationship orderRelationship) {
        if (orderRelationshipService.insertSelective(orderRelationship) > 0) {
            return new JsonResultUtil(200, "添加成功");
        }
        return new JsonResultUtil(300, "添加失败");
    }

    /**
     * 修改
     *
     * @param orderRelationship
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public JsonResultUtil update(@RequestBody OrderRelationship orderRelationship) {
        if (orderRelationshipService.updateByPrimaryKey(orderRelationship) > 0)
            return new JsonResultUtil(200, "修改成功");
        return new JsonResultUtil(300, "修改失败,请重试或联系管理员");
    }

    /**
     * 批量删除
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
        int t = 0;
        for (int i = 0; i < ids.size(); i++) {
            t += orderRelationshipService.deleteByPrimaryKey(ids.get(i));
        }
        if (t > 0) {
            return new JsonResultUtil(200, "删除成功");
        } else {
            return new JsonResultUtil(300, "删除失败");
        }
    }


}
