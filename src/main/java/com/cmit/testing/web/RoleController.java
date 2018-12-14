package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.service.SysRoleService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

/**
 * 角色相关接口
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Controller
@RequestMapping("/api/v1/role")
@ResponseBody
@SystemLog("角色模块")
@Permission
public class RoleController extends BaseController{

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 分页
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)

    public JsonResultUtil findPage(@QueryParam("currentPage") Integer currentPage,
                                   @QueryParam("pageSize")Integer pageSize,
                                   @QueryParam("roleName") String roleName){
        return new JsonResultUtil(200,"操作成功",sysRoleService.findPage(currentPage,pageSize,roleName));
    }

    /**
     * 添加
     * @param map
     * @return
     */
    @RequestMapping( method = RequestMethod.POST)

    public JsonResultUtil add(@RequestBody Map<String, String> map){
        return new JsonResultUtil(200,"操作成功",sysRoleService.insert(map));
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResultUtil update(@PathVariable("id") Integer id, @RequestBody Map<String,String> map){
        return new JsonResultUtil(200,"操作成功",sysRoleService.update(id,map));
    }

    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)

    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){
        List<Integer> ids =(List<Integer>) map.get("ids");
        sysRoleService.deleteByIds(ids);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)

    public JsonResultUtil delete(@PathVariable("id")Integer id){
        sysRoleService.delete(id);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 获取
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)

    public JsonResultUtil get(@PathVariable("id")Integer id){
        return new JsonResultUtil(200,"操作成功",sysRoleService.get(id));
    }

    /**
     * 获取findAll
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)

    public JsonResultUtil findAll(){
        return new JsonResultUtil(200,"操作成功",sysRoleService.findAll());
    }
}
