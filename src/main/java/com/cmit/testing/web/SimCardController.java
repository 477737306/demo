package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.SimCardService;
import com.cmit.testing.utils.DateUtil;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Suviky on 2018/7/19 11:08
 */
@RestController
@RequestMapping("/api/v1/simCard")
@Permission
public class SimCardController extends BaseController {
    @Autowired
    private SimCardService simCardService;

    @Autowired
    private OrderRelationshipController orderRelationshipController;


    /**
     * 分页查询所有sim卡信息
     *
     * @param pageBean
     * @param record
     * @return
     */
    @RequestMapping(value = "/findPageRecordsBySimCard", method = RequestMethod.GET)
    public JsonResultUtil findPageRecordsBySimCard(PageBean<SimCard> pageBean, SimCard record) {
        return new JsonResultUtil(200, "获取成功", simCardService.findPageRecordsBySimCard(pageBean, record));
    }

    /**
     * 更新所有用户资费信息
     *
     * @return
     */
    @RequestMapping(value = "/updateAllLocalData", method = RequestMethod.GET)
    public JsonResultUtil updateAllLocalData(String simcardIds) {
        Map<String, String> result = new ConcurrentHashMap<>();
        Object simCardFlag = RedisUtil.getObject("simCardFlag");
        if (simCardFlag == null || ("false").equals(simCardFlag.toString())) {
            RedisUtil.set("simCardUpdate", DateUtil.getTime());
            RedisUtil.setObject("simCardFlag", "true");
            simCardService.updateAllLocalData(simcardIds);
            orderRelationshipController.updateAllLocalData(simcardIds);
            RedisUtil.setObject("simCardFlag", "false");
            result.put("status", "false");
            return new JsonResultUtil(200, "更新成功", result);
        }
        result.put("status", simCardFlag.toString());
        return new JsonResultUtil(200, "更新成功", result);
    }

    @RequestMapping(value = "/simCardUpdate", method = RequestMethod.GET)
    public JsonResultUtil simCardUpdate() {
        return new JsonResultUtil(200, "操作成功", RedisUtil.get("simCardUpdate"));
    }

    /**
     * 获取更新标识
     * false为页面更新标签不转
     * true为页面更新标签转动
     *
     * @return
     */
    @RequestMapping(value = "/getSimCardFlag", method = RequestMethod.GET)
    public JsonResultUtil getSimCardFlag() {
        Map<String, String> map = new HashMap<>();
        Object simCardFlag = RedisUtil.getObject("simCardFlag");
        if (simCardFlag == null || ("false").equals(simCardFlag.toString())) {
            map.put("status", "false"); //false为页面更新标签不转
        } else {
            map.put("status", "true");
        }
        return new JsonResultUtil(200, "获取成功", map);
    }

    /**
     * 根据主键Id获取数据
     *
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResultUtil get(Integer id) {
        return new JsonResultUtil(200, "操作成功", simCardService.selectByPrimaryKey(id));
    }

    /**
     * 添加
     *
     * @param simCard
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody SimCard simCard) {
        if (simCardService.insert(simCard) > 0) {
            return new JsonResultUtil(200, "添加成功");
        }
        return new JsonResultUtil(300, "添加失败");
    }

    /**
     * SimCard批量删除
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
        int t = 0;
        for (int i = 0; i < ids.size(); i++) {
            t += simCardService.deleteByPrimaryKey(ids.get(i));
        }
        if (t > 0) {
            return new JsonResultUtil(200, "删除成功");
        } else {
            return new JsonResultUtil(300, "删除失败");
        }
    }

    /**
     * SimCard修改
     *
     * @param simCard
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public JsonResultUtil update(@RequestBody SimCard simCard) {
        if (simCardService.updateByPrimaryKeySelective(simCard) > 0)
            return new JsonResultUtil(200, "修改成功");
        return new JsonResultUtil(300, "修改失败,请重试或联系管理员");
    }

    @RequestMapping(value = "/getRecords", method = RequestMethod.POST)

    public JsonResultUtil getRecords(Integer pageNum, Integer pageSize) {
        List<SimCard> list = simCardService.getRecords(pageNum, pageSize);
        if (list.size() > 0) {
            return new JsonResultUtil(200, "获取成功", list);
        }
        return new JsonResultUtil(300, "获取失败");
    }

    /**
     * 根据 SimCard查询对应的数据
     *
     * @param record
     * @return
     */
    @RequestMapping(value = "/getAllRecordsBySimCard", method = RequestMethod.POST)
    public JsonResultUtil getAllRecordsBySimCard(@RequestBody SimCard record) {
        return new JsonResultUtil(200, "获取成功", simCardService.getAllRecordsBySimCard(record));
    }

    /**
     * 导入数据
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public JsonResultUtil importExcel(@RequestParam("filename") MultipartFile multipartFile) {
        try {
            simCardService.importExcel(multipartFile, 1, 0);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResultUtil(300, "导入失败!");
        }
        return new JsonResultUtil(200, "导入成功!");
    }
}
