package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.ModelScript;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.AppScriptService;
import com.cmit.testing.utils.JsonResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YangWanLi
 * @date 2018/7/30 10:43
 */
@RestController
@RequestMapping("/api/v1/projectMenu")
@Permission
public class ProjectMenuController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProjectMenuService projectMenuService;

    @Autowired
    private ModelScriptService modelScriptService; //模板service

    @Autowired
    private TestCaseService testCaseService;       //用例service

    @Autowired
    private BusinessService businessService;       //业务service

    @Autowired
    private ProjectService projectService;         //项目service

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private AppScriptService appScriptService;                    //app脚本service
    @Autowired
    private AppCaseService appCaseService;                        //app用例service


    /**
     * 复制
     *copyScript
     */
    @RequestMapping(value = "/copy",method = RequestMethod.POST)
    public JsonResultUtil copy(@RequestBody Map<String ,Object> map){
        List<Map<String , String>> list =(List<Map<String , String>>) map.get("list");
        int parentId = (int)map.get("parentId");
        SysPermission parsysPermission= sysPermissionService.selectByPrimaryKey(parentId);
        for (int i =0 ;i < list.size() ;i++) {
            List<SysPermission> sysList = new ArrayList<>();
            SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(Integer.parseInt(list.get(i).get("id")));
            sysPermission.setNameCopyNum(sysPermission.getNameCopyNum()+1);
            sysPermissionService.updateByPrimaryKey(sysPermission);
            sysPermission.setName(sysPermission.getName()+"_copy_"+(sysPermission.getNameCopyNum()));
            sysList.add(sysPermission);
            String selfType = list.get(i).get("selfType");
            if(selfType.equalsIgnoreCase("scripts")){
                if(parsysPermission.getType().equals("business")){
                    modelScriptService.copy(sysList,parentId);
                }
               /* flag =*/
            }
            if(selfType.equalsIgnoreCase("appScript")){
                if(parsysPermission.getType().equals("business")){
                    appScriptService.copy(sysList,parentId);
                }
                /* flag =*/
            }

            if(selfType.equalsIgnoreCase("case")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag =*/
                    testCaseService.copy(sysList, parentId);
                }
            }
            if(selfType.equalsIgnoreCase("appCase")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag =*/
                    appCaseService.copy(sysList, parentId);
                }
            }
            if(selfType.equalsIgnoreCase("business")){
                if(parsysPermission.getType().equals("project")) {
                    /*flag =*/
                    businessService.copy(sysList, parentId);
                }
            }
            if(selfType.equalsIgnoreCase("project")){
                if(parsysPermission.getType().equals("projectData")) {
                    /*flag =*/
                    projectService.copyProjects(sysList);
                }
            }
        }
            return new JsonResultUtil(200,"操作成功",null);
    }

    /**
     * 剪切
     *shearScript
     */
    @RequestMapping(value = "/shear",method = RequestMethod.POST)
//
    public JsonResultUtil shear(@RequestBody Map<String,Object> map){
        List<Map<String , String>> list =(List<Map<String , String>>) map.get("list");
        int parentId = (int)map.get("parentId");
        SysPermission parsysPermission= sysPermissionService.selectByPrimaryKey(parentId);
        for (int i =0 ;i < list.size() ;i++) {
            List<SysPermission> sysList = new ArrayList<>();
            SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(Integer.parseInt(list.get(i).get("id")));
            sysPermission.setCode(Integer.parseInt(list.get(i).get("number")));
            String selfType = list.get(i).get("selfType");
            sysList.add(sysPermission);
            if(selfType.equalsIgnoreCase("scripts")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag = */
                    modelScriptService.shear(sysList, parentId);
                }else {
                    throw new TestSystemException("脚本只能剪贴在业务下");
                }
            }
            if(selfType.equalsIgnoreCase("appScript")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag = */
                    appScriptService.shear(sysList, parentId);
                }else {
                    throw new TestSystemException("脚本只能剪贴在业务下");
                }
            }
            if(selfType.equalsIgnoreCase("case")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag = */
                    testCaseService.shear(sysList, parentId);
                }else {
                    throw new TestSystemException("用例只能剪贴在业务下");
                }
            }
            if(selfType.equalsIgnoreCase("appCase")){
                if(parsysPermission.getType().equals("business")) {
                    /*flag = */
                    appCaseService.shear(sysList, parentId);
                }else {
                    throw new TestSystemException("用例只能剪贴在业务下");
                }
            }
            if(selfType.equalsIgnoreCase("business")){
                if(parsysPermission.getType().equals("project")) {
                    /*flag = */
                    businessService.shear(sysList, parentId);
                }else {
                    throw new TestSystemException("业务只能剪贴在项目下");
                }
            }
        }
//        if(flag){
            return new JsonResultUtil(200,"操作成功",null);
//        }else{
//            return new JsonResultUtil(300,"失败",null);
//        }
    }

    /**
     * 保存
     * @param map
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
//
    public JsonResultUtil saveProjectMenu(@RequestBody Map<String,Object> map){
    	Integer result = projectMenuService.save(map);
        return new JsonResultUtil(200, "操作成功", result);
    }

    /**
     * 通过菜单Id删除菜单目录和数据
     * @param map
     */
    @RequestMapping(value = "/deleteByIds",method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String ,Object> map){
        List<Integer> ids  = (List<Integer>) map.get("ids");
        projectMenuService.deleteByIds(ids);

        return new JsonResultUtil(200,"操作成功");
    }



    /**
     * 分页
     * @param pageSize
     * @return
     */
//
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(@QueryParam("currentPage")Integer currentPage,
                                   @QueryParam("pageSize")Integer pageSize,
                                   @QueryParam("name")String name,
                                   @QueryParam("tableFlag")@NotNull String tableFlag,
                                   @QueryParam("parentId")Integer parentId){
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(parentId);
        return new JsonResultUtil(200,"操作成功", projectMenuService.findPage(currentPage,pageSize,name,tableFlag,sysPermission==null?null:sysPermission.getDataId()));
    }

    /**
     * 根据Id获取脚本
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getModelScriptsById/{id}",method = RequestMethod.GET)
//
    public JsonResultUtil getModelScriptsById(@PathVariable("id")Integer id){
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        ModelScript script = modelScriptService.getModelScriptById(sysPermission.getDataId());
        script.setId(id);
        return new JsonResultUtil(200,"获取成功",script);
    }

    /**
     * 根据Id获取用例
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getTestCaseById/{id}",method = RequestMethod.GET)
//
    public JsonResultUtil getTestCaseById(@PathVariable("id")Integer id){
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        TestCase testCase = testCaseService.selectByPrimaryKey(sysPermission.getDataId());
        testCase.setId(id);
        return new JsonResultUtil(200,"获取成功",testCase);
    }

    /**
     * 重命名
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateNameById/{id}",method = RequestMethod.PUT)
//
    public JsonResultUtil updateNameById(@PathVariable("id")Integer id,@RequestBody Map<String,String> map){
        return new JsonResultUtil(200,"获取成功",projectMenuService.updateNameById(id,map.get("name")));
    }

    /**
     * 根据父级菜单Id获取树形菜单（批量执行）
     *
     * @return
     */
    @RequestMapping(value = "/getTreeMenuByParentId/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil getTreeMenuByParentId(@PathVariable("parentId")Integer parentId){
        Map<String,Object> result = new HashMap<>();
        List<String> noType = new ArrayList<>();
        noType.add("case");
        noType.add("scripts");
        noType.add("scriptsData");
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(parentId);
        sysPermission.setPermissions(sysPermissionService.getTreeMenu(parentId,noType));
        List<SysPermission> sysPermissionList = new ArrayList<>();
        sysPermissionList.add(sysPermission);
        result.put("ownerMenu",sysPermissionList );
        return new JsonResultUtil(200,"获取成功",result);
    }

    /**
     * 根据父级菜单Id获取树形菜单(众测执行)
     *
     * @return
     */
    @RequestMapping(value = "/getAllTreeMenu", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil getAllTreeMenu(String notTypeList){
        Map<String,Object> result = new HashMap<>();
        List<String> noType = new ArrayList<>();
//        noType.add("case");
//        noType.add("scripts");
//        noType.add("appScript");
//        noType.add("scriptsData");
        noType = java.util.Arrays.asList(notTypeList.split(","));
        SysPermission sysPermission = sysPermissionService.getByType("projectData").get(0);
        sysPermission.setPermissions(sysPermissionService.getTreeMenu(sysPermission.getId(),noType));
        List<SysPermission> sysPermissionList = new ArrayList<>();
        sysPermissionList.add(sysPermission);
        result.put("ownerMenu",sysPermissionList );
        return new JsonResultUtil(200,"获取成功",result);
    }

    /**
     * 根据父级菜单Id获取树形菜单（项目目录树）
     *
     * @return
     */
    @RequestMapping(value = "/getTreeByParentId/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    @Permission
    public JsonResultUtil getTreeParentId(@PathVariable("parentId")Integer parentId){
        Map<String,Object> result = new HashMap<>();
        result.put("ownerMenu", sysPermissionService.getTreeMenu(parentId,null));
        return new JsonResultUtil(200,"获取成功",result);
    }

    /**
     * 检测用例是否被关联
     *
     * @return
     */
    @RequestMapping(value = "/checkCorrelation", method = RequestMethod.POST)
    @ResponseBody
    @Permission
    public JsonResultUtil checkCorrelation(@RequestBody Map<String ,Object> map){
        List<Integer> ids  = (List<Integer>) map.get("ids");
        boolean bool = true;
        String message = "操作成功";
        for (Integer id : ids) {
            bool = projectMenuService.checkCorrelation(id);
            if(!bool){
                message = "用例已被任务所关联，请确认是否强制删除！";
                break;
            }
        }
        return new JsonResultUtil(200,message,bool);
    }

}
