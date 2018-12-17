package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.app.AppScriptFile;
import com.cmit.testing.entity.vo.ScriptVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.app.AppScriptService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 *creat by chenxiaozhang
 */
@RestController
@RequestMapping(value = "/api/v1/app/script")
@Permission
public class AppScriptController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppScriptController.class);

    @Autowired
    private AppScriptService appScriptService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @RequestMapping(value = "/getAppScriptsById/{id}", method = RequestMethod.GET)
    public JsonResultUtil getAppScriptById(@PathVariable("id") Integer id){
        if (id == null)
        {
            return new JsonResultUtil(300, "请求参数不能为空");
        }
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        Map<String, Object> scriptMap = appScriptService.getAppScriptById(sysPermission.getDataId());
        return new JsonResultUtil(200, "操作成功", scriptMap);
    }

    /**
     * 校验脚本的名称是否唯一
     * @return
     */
   @RequestMapping(value = "/checkUniqueName",method =RequestMethod.POST )
    public JsonResultUtil checkScriptName(@RequestBody AppScript as){

        if (as == null || StringUtils.isEmpty(as.getScriptName()))
        {
           return new JsonResultUtil(300, "请求参数不能为空");
        }
        String scriptId = "";
        if (as.getScriptId() != null)
        {
            scriptId = as.getScriptId().toString();
        }
        String scriptName = as.getScriptName();
        return  new JsonResultUtil(200, "操作成功", appScriptService.checkScriptName(scriptId, scriptName));

   }

    /**
     *  APP脚本文件上传
     */
    @RequestMapping(value = "/uploadScript",method =RequestMethod.POST )
    public JsonResultUtil importTestCase(@RequestParam(required = false, value = "file") MultipartFile file,
                                         String businessId, String scriptId, String scriptTag, HttpServletRequest request) {

        try
        {

            if (null == file || file.isEmpty())
            {
                return new JsonResultUtil(300, "请选择上传的脚本文件");
            }
            if (StringUtils.isEmpty(scriptTag) || "null".equals(scriptTag))
            {
                return new JsonResultUtil(300, "请求参数不能为空");
            }
            ScriptVO vo = new ScriptVO();
            //脚本 0-通用，1-普通
            if ("1".equals(scriptTag))
            {
                vo.setBusinessId(Integer.parseInt(businessId));
            }
            vo.setScriptTag(Integer.parseInt(scriptTag));
            if (StringUtils.isNotEmpty(scriptId) && !("null".equals(scriptId)))
            {
                vo.setScriptId(Integer.parseInt(scriptId));
            }
            return new JsonResultUtil(200, "上传成功", appScriptService.uploadScript(file, vo));
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            return new JsonResultUtil(500, e.getMessage());
        }
    }

   @RequestMapping(value = "/deleteByIds",method = RequestMethod.DELETE)
   public JsonResultUtil delScriptByIds(@RequestBody Map<String, Object> map) {
       try {


           List<Integer> ids = null;
           if (map == null || map.get("ids") == null || (ids = (List<Integer>) map.get("ids")).isEmpty()) {
               return new JsonResultUtil(300, "参数不能为空");
           } else {
            int i= appScriptService.delScriptByIds(ids);
               if (i<0){
                   return new JsonResultUtil(300, "关联用例的脚本无法删除");
               }
               return new JsonResultUtil(200, "删除成功");

           }
       } catch (Exception e) {
           LOGGER.error(e.getMessage(), e);
           throw new TestSystemException("参数异常");
       }

   }


    @RequestMapping(value = "/capyscript/{id}",method =RequestMethod.POST)
    public JsonResultUtil copyscript(@PathVariable(value = "id") AppScriptFile appScriptFileId){
        return null;
   }


    /**
     * 查询脚本关联的用例信息
     *
     * @return 分页数据
     */
   @RequestMapping(value="/getPageByScriptId", method = RequestMethod.GET)
    public JsonResultUtil getPageByScriptId(PageBean pageBean, AppCase appCase) {
       if (appCase == null || appCase.getScriptId() == null)
       {
           throw new TestSystemException("未获取到脚本ID");
       }
       appCase.setOldCaseId(0);
       return new JsonResultUtil(200, "操作成功", appScriptService.getPageByScriptId(pageBean, appCase));
   }

    /**
     * 获取当前业务下的普通脚本和所有通用脚本
     *
     * @return
     */
    @RequestMapping(value = "/getAllByBusinessId/{id}",method = RequestMethod.GET)
    public JsonResultUtil getAllByBusinessId(@PathVariable("id") Integer id){
        return new JsonResultUtil(200,"操作成功",appScriptService.getAllByBusinessId(id));
    }

}
