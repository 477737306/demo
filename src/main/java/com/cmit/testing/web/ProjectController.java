package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.Project;
import com.cmit.testing.service.ProjectService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/12 17:55
 */
@RestController
@RequestMapping("/api/v1/project")
@SystemLog("项目模块")
@Permission
public class ProjectController extends BaseController{
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/sum",method = RequestMethod.GET)

    public JsonResultUtil getProjectSum(){
        return new JsonResultUtil(200,"获取成功",projectService.getProjectSum());
    }


    /**
     * 项目
     * @return
     */
    @RequestMapping(value = "getAllProject",method = RequestMethod.POST)

    public JsonResultUtil getProjectList(){
        return new JsonResultUtil(200,"获取成功",projectService.getAllProject());
    }


    /**
     * 添加
     * @param project
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.PUT)

    public JsonResultUtil addProject(Project project){
        if(projectService.insertSelective(project) > 0){
            return new JsonResultUtil(200,"添加成功");
        }else{
            return new JsonResultUtil(300,"添加失败");
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)

    public JsonResultUtil deleteProject(@PathVariable("id") int id){
        if(projectService.deleteByPrimaryKey(id) > 0){
            return new JsonResultUtil(200,"删除成功");
        }else{
            return new JsonResultUtil(300,"删除失败");
        }
    }

    /**
     * 批量删除
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteByIds",method = RequestMethod.DELETE)

    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){

        List<Integer> ids = (List<Integer>) map.get("ids");
        if(projectService.deleteByIds(ids)>0){
            return new JsonResultUtil(200,"删除成功");
        }else{
            return new JsonResultUtil(300,"删除失败");
        }
    }

    /**
     * 修改
     * @param project
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)

    public JsonResultUtil updateProject(Project project){
        if(projectService.updateByPrimaryKeySelective(project) > 0){
            return new JsonResultUtil(200,"修改成功");
        }else{
            return new JsonResultUtil(300,"修改失败");
        }
    }

    /**
     * 获取
     * @param id
     * @return
     */
    @RequestMapping(value = "select",method = RequestMethod.GET)

    public JsonResultUtil getProject(int id){
        Project project = projectService.selectByPrimaryKey(id);
        if (project!=null){
            return new JsonResultUtil(200,"获取成功",project);
        }else{
            return new JsonResultUtil(300,"获取失败");
        }
    }
//
//    /**
//     * 项目，业务，用例分类搜索分页
//     * @param pageSize
//     * @return
//     */
////
//    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
//    public JsonResultUtil findPage(@QueryParam("currentPage")Integer currentPage,
//                                   @QueryParam("pageSize")Integer pageSize,
//                                   @QueryParam("name")String name,
//                                   @QueryParam("tableFlag")@NotNull String tableFlag,
//                                   @QueryParam("parentId")Integer parentId){
//        return new JsonResultUtil(200,"操作成功", projectService.findPage(currentPage,pageSize,name,tableFlag,parentId));
//    }

//    /**
//     * 全局搜索分页
//     * @param name
//     * @return
//     */
//    @RequestMapping(value = "/pbtFindPage", method = RequestMethod.GET)
//    public JsonResultUtil pbtFindByPage(@QueryParam("currentPage")Integer currentPage,
//                                       @QueryParam("pageSize")Integer pageSize,
//                                       @QueryParam("name")String name){
//        return new JsonResultUtil(200,"获取成功",projectService.pbtFindByPage(currentPage,pageSize,name));
//    }
}
