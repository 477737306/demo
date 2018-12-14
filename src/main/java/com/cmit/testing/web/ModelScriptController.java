package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.ModelScript;
import com.cmit.testing.entity.ScriptStep;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.ModelScriptService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 脚本模板
 *
 * Created by Suviky on 2018/7/26 15:06
 */
@RestController
@RequestMapping("/api/v1/modelScript")
@SystemLog("脚本模块")
@Permission
public class ModelScriptController extends BaseController{
    @Autowired
    private ModelScriptService modelScriptService;

    @Autowired
    private TestCaseService testCaseService;

    @RequestMapping(value = "/getModelScripts",method = RequestMethod.GET)

    public JsonResultUtil getModelScripts(Business business){
        List<ModelScript> scripts = modelScriptService.getModelScripts(business);
        if(scripts!=null&&scripts.size()>0){
            return new JsonResultUtil(200,"获取成功",scripts);
        }else {
            return new JsonResultUtil(300,"获取失败");
        }
    }
    @RequestMapping(value = "/addModelScript",method = RequestMethod.POST)

    public JsonResultUtil addModelScript(ModelScript script, List<ScriptStep> steps){
        if(modelScriptService.addModelScript(script,steps)>0){
            return new JsonResultUtil(200,"添加成功");
        }else {
            return new JsonResultUtil(300,"添加失败");
        }
    }
    @RequestMapping(value = "/deleteModelScript",method = RequestMethod.DELETE)

    public JsonResultUtil deleteModelScript(ModelScript script){
        if(modelScriptService.deleteModelScript(script)){
            return new JsonResultUtil(200,"删除成功");
        }else {
            return new JsonResultUtil(300,"删除失败");
        }
    }
    @RequestMapping(value = "/updateModelScript",method = RequestMethod.POST)

    public JsonResultUtil updateModelScript(ModelScript script, List<ScriptStep> steps){
        if(modelScriptService.updateModelScript(script,steps)){
            return new JsonResultUtil(200,"修改成功");
        }else {
            return new JsonResultUtil(300,"修改失败");
        }
    }

    /**
     * 脚本关联的用例分页
     * @param testCase
     * @return
     */
    @RequestMapping(value = "/getPageByScriptId", method = RequestMethod.GET)
    public JsonResultUtil getPageByScriptId(PageBean pageBean, TestCase testCase){
        testCase.setOldTestcaseId(0);
        return new JsonResultUtil(200,"操作成功", testCaseService.getPageByScriptId(pageBean,testCase));
    }



    /**
     * 根据Id获取脚本
     *
     * @return
     */
    @RequestMapping(value = "/selectByScriptId/{id}",method = RequestMethod.GET)
//
    public JsonResultUtil selectByScriptId(@PathVariable("id")Integer id){
        List<Integer> paramTypes = new ArrayList<>();
        paramTypes.add(1);
        paramTypes.add(2);
        return new JsonResultUtil(200,"操作成功",modelScriptService.getByScriptIdAndParamType(id,paramTypes));
    }
}
