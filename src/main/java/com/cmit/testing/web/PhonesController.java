package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.Phones;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.PhonesService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/18 15:00
 */
@RestController
@RequestMapping("/api/v1/phones")
@Permission
public class PhonesController extends BaseController{
    @Autowired
    private PhonesService phonesService;

    /**
     * 分页
     * @param phones
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(PageBean<Phones> pageBean,Phones phones) {
        return new JsonResultUtil(200, "获取成功", phonesService.findByPage(pageBean,phones));
    }

    /**
     * 根据主键Id获取数据
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResultUtil get(String id){
        return new JsonResultUtil(200,"操作成功", phonesService.selectByPrimaryKey(Integer.parseInt(id)));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody Phones phone){
        if (phonesService.insertSelective(phone) > 0){
            return new JsonResultUtil(200,"添加成功");
        }
        return new JsonResultUtil(300,"添加失败");
    }

    /**
     * SimCard修改
     * @param phones
     * @return
     */
    @RequestMapping(value = "/update/{id}" ,method = RequestMethod.PUT)
    public JsonResultUtil update(@RequestBody Phones phones){
        if(phonesService.updateByPrimaryKey(phones) > 0)
            return new JsonResultUtil(200,"修改成功");
        return new JsonResultUtil(300,"修改失败,请重试或联系管理员");
    }

    /**
     * 批量删除
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete" ,method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){
        List<Integer> ids = (List<Integer>)map.get("ids");
        phonesService.deleteByIds(ids);
        return new JsonResultUtil(200,"操作成功");
    }

}
