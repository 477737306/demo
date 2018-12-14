package com.cmit.testing.web;


import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController extends BaseController{
    @Autowired
    private SysPermissionService permissionService;

    /**
     * 删除资源
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePermission/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Permission
    public JsonResultUtil deleteByPrimaryKey(@PathVariable Integer id){
        int n = permissionService.deleteByPrimaryKey(id);
        if(n>0){
            return new JsonResultUtil(200,"删除成功");
        }
        return new JsonResultUtil(300,"删除出错,请重试或联系管理员");
    }

    /**
     * 增加资源
     * @param record
     * @return
     */
    @RequestMapping(value = "/insertPermission", method = RequestMethod.PUT)
    @ResponseBody
    @Permission
    public JsonResultUtil insertPermission(SysPermission record){
        if(permissionService.insertSelective(record) > 0){
            return new JsonResultUtil(200,"添加成功");
        }
        return new JsonResultUtil(300,"添加失败,请重试或联系管理员");
    }

    /**
     * 修改资源
     * @param record
     * @return
     */
    @RequestMapping(value = "/updatePermission", method = RequestMethod.POST)
    @ResponseBody
    @Permission
    public JsonResultUtil updatePermission(SysPermission record){
        try {
            if(permissionService.updateByPrimaryKeySelective(record) > 0){
                return new JsonResultUtil(200,"修改成功");
            }
            return new JsonResultUtil(300,"修改失败,请重试或联系管理员");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResultUtil(300,"修改失败,请重试或联系管理员");
        }
    }

    /**
     * 获取资源
     * @return List<SysPermission>
     */
    @RequestMapping(value = "/getPermissions", method = RequestMethod.GET)
    @ResponseBody
    @Permission
    public JsonResultUtil getPermissions(int pageNum, int pageSize){
        List<SysPermission> list = permissionService.getPermissions(pageNum,pageSize);
        return new JsonResultUtil(200,"获取成功",list);
    }

    /**
     * 获取资源
     * @return
     */
    @RequestMapping(value = "/getPermissionById", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil getPermissionById(int id){
        SysPermission permission = permissionService.getPermissionById(id);
        return new JsonResultUtil(200,"获取成功",permission);
    }

    /**
     * 根据当前登录用户获取资源
     * @return
     */
    @RequestMapping(value = "/loginPermission", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil loginPermission(HttpServletRequest request){
        List<SysPermission> permissions = permissionService.loginPermission();
        return new JsonResultUtil(200,"获取成功",permissions);
    }

    /**
     * 为角色绑定资源
     * @return
     */
    @RequestMapping(value = "/updatePermissionsOfRole", method = RequestMethod.POST)
    @ResponseBody
    @Permission
    public JsonResultUtil updatePermissionsOfRole(@RequestBody Map<String,Object> map){
        Integer role_id = Integer.parseInt(map.get("role_id").toString());
        String pids = map.get("pids").toString();
        return permissionService.updatePermissionOfRole(role_id,pids);
    }

    /**
     * 获取指定角色资源
     * @param roleId
     * @return List<SysPermission>
     */
    @RequestMapping(value = "/getPermissionByRole/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    @Permission
    public JsonResultUtil getPermissionByRole(@PathVariable("roleId") int roleId){
        List<SysPermission> list = permissionService.getPermissionByRole(roleId);
        return new JsonResultUtil(200,"获取成功",list);
    }

    /**
     * 根据角色获取树形菜单
     *
     * @return
     */
    @RequestMapping(value = "/getTreeMenuByRoleId/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    @Permission
    public JsonResultUtil getTreeMenu(@PathVariable("roleId")Integer roleId){
        Map<String,Object> result = new HashMap<>();
        result.put("treeMenu",permissionService.getAllTree(0,null));
//        if(roleId!=null) {
//            List<SysPermission>  ownerMenu = permissionService.getPermissionByRole(roleId);
//        }
        result.put("ownerMenu", permissionService.getPermissionByRole(roleId));
        return new JsonResultUtil(200,"获取成功",result);
    }

    /**
     * 根据父级菜单Id获取树形菜单
     *
     * @return
     */
    @RequestMapping(value = "/getTreeMenuByParentId/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    @Permission
    public JsonResultUtil getTreeMenuByParentId(@PathVariable("parentId")Integer parentId){
        Map<String,Object> result = new HashMap<>();
        result.put("ownerMenu", permissionService.getTreeMenu(parentId,null));
        return new JsonResultUtil(200,"获取成功",result);
    }
}
