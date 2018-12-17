package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.Phones;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.MessageService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/24 10:24
 */
@RestController
@RequestMapping("/api/v1/message")
@Permission
public class MessageController extends BaseController{
    @Autowired
    private MessageService messageService;

    @RequestMapping("/getSmsVefCode")
    public JsonResultUtil getSmsVerifyCode(String iphone,String matchKeyNecessity,String matchKeySelectivity,Date date){
        String code=messageService.getSmsVerifyCode(iphone,matchKeyNecessity,matchKeySelectivity,date);
        if(code!=null&&!code.equals("")){
            return new JsonResultUtil(200,"获取验证码成功",code);
        }else{
            return new JsonResultUtil(300,"获取验证码失败",code);
        }

    }
    /**
     * 分页查询
     * @param
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(PageBean<Message> pageBean , Message message) {
        return new JsonResultUtil(200, "操作成功", messageService.findByPage(pageBean , message));
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public JsonResultUtil selectAll(PageBean<Message> pageBean , Message message) {
        return new JsonResultUtil(200, "操作成功", messageService.selectAll(pageBean , message));
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResultUtil get(String id){
        return new JsonResultUtil(200,"操作成功", messageService.selectByPrimaryKey(Integer.parseInt(id)));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody Message message){
        if (messageService.insert(message) > 0){
            return new JsonResultUtil(200,"添加成功");
        }
        return new JsonResultUtil(300,"添加失败");
    }

    @RequestMapping(value = "/update/{id}" ,method = RequestMethod.PUT)
    public JsonResultUtil update(@RequestBody Message message){
        if(messageService.updateByPrimaryKey(message) > 0)
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
        int t = 0;
        for(int i = 0 ; i <ids.size() ; i++){
            t +=  messageService.deleteByPrimaryKey(ids.get(i));
        }
        if(t > 0){
            return new JsonResultUtil(200,"删除成功");
        }else{
            return new JsonResultUtil(300,"删除失败");
        }
    }
}
