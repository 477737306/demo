package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysUserService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;


/**
 * 用户相关接口
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */
@Controller
@RequestMapping(value = "/api/v1/user")
@ResponseBody
@SystemLog("用户模块")
@Permission
public class UserController extends BaseController{

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(@QueryParam("currentPage")Integer currentPage,
                                    @QueryParam("pageSize")Integer pageSize,
                                    @QueryParam("userName")String userName,
                                    @QueryParam("roleName")String roleName){
        return new JsonResultUtil(200,"操作成功", sysUserService.findPage(currentPage,pageSize,userName,roleName));
    }

    /**
     * 添加
     * @param map
     * @return
     */
    @RequestMapping
    public JsonResultUtil add(@RequestBody Map<String,Object> map){
        sysUserService.insert(map);
      return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 修改
     * @return
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResultUtil update(@PathVariable("id") Integer id,@RequestBody Map<String,Object> map){
        sysUserService.updateSysUser(id,map);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 修改密码
     * @return
     */

    @RequestMapping(value = "/updatePwd", method = RequestMethod.PUT)
    public JsonResultUtil updatePwd(@RequestBody Map<String,String> map){
        ShiroUser shiroUser = getShiroUser();

        sysUserService.updatePwd(shiroUser.getId(),map);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 批量删除
     * @return
     */

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){
        List<Integer> ids = (List<Integer>)map.get("ids");
        sysUserService.deleteByIds(ids);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 删除
     * @return
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResultUtil delete(@PathVariable("id")Integer id){
        sysUserService.delete(id);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 获取
     * @return
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResultUtil get(@PathVariable("id")Integer id){
        return new JsonResultUtil(200,"操作成功", sysUserService.get(id));
    }

    /**
     * 用户关联角色
     * @return
     */

    @RequestMapping(value = "/addUserRoles", method = RequestMethod.POST)
    public JsonResultUtil get(@RequestBody Map<String,Object> map){
        Integer userId =(Integer) map.get("userId");
        String[] roleIds =map.get("roleIds").toString().split(",");
        sysUserService.addRoles(userId,roleIds);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 重置密码
     * @return
     */

    @RequestMapping(value = "/resetPassword/{userId}", method = RequestMethod.POST)
    public  JsonResultUtil resetPassword(@PathVariable("userId")Integer userId){
        sysUserService.resetPassword(userId);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 解锁
     * @return
     */

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    public  JsonResultUtil unlock(@RequestBody Map<String,Object> map){
        List<Integer> userIds =( List<Integer>) map.get("userIds");
        sysUserService.unlock(userIds);
        return new JsonResultUtil(200,"操作成功");
    }

}
