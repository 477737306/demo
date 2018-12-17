package com.cmit.testing.web.app;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.DownloadFileDto;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.vo.CaseExcResultVO;
import com.cmit.testing.entity.vo.CommonResultVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.AppScriptService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.file.ZipUtils;
import com.cmit.testing.web.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/28 0028 下午 2:41.
 */
@Controller
@RequestMapping("/api/v1/app/case")
@Permission
public class AppCaseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(AppCaseController.class);

    @Autowired
    private AppCaseService appCaseService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/getAppCaseById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil get(@PathVariable("id") Integer id){
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        return new JsonResultUtil(200, "操作成功",appCaseService.getAppCaseById(sysPermission.getDataId()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResultUtil deleteByIds(@PathVariable("id")Integer id){
        if (null == id)
        {
            return new JsonResultUtil(300,"请求参数不能为空");
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        int i = appCaseService.deleteByIds(ids);
        if (i > 0)
        {
            return new JsonResultUtil(200,"删除成功");
        }
        else
        {
            return new JsonResultUtil(300,"删除失败");
        }
    }


    @RequestMapping(value = "/executeAppCase", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultUtil executeSingleAppCase(@RequestBody AppCase appCase) {
        if (null == appCase.getCaseId())
        {
            throw new TestSystemException("执行用例的ID不能不为空");
        }
        Map<String, String> map = appCaseService.handExecuteCase(appCase.getCaseId());
        if (map == null)
        {
            return new JsonResultUtil(200, "用例下发成功");
        }
        else
        {
            return new JsonResultUtil(300, "用例下发失败", map);
        }
    }

    /**
     * 获取业务下的用例(去除当前用例)
     * @param id 用例id
     * @param businessId 业务ID
     * @return
     */
    @RequestMapping(value = "/getAllByBusinessId/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil getAllByBusinessId(@PathVariable("id")Integer id, Integer businessId){
        AppCase appCase = new AppCase();
        appCase.setBusinessId(businessId);
        appCase.setOldCaseId(0);

        List<AppCase> caseList = appCaseService.getAllByAppCase(appCase);
        if (CollectionUtils.isNotEmpty(caseList))
        {
            for (AppCase ac : caseList)
            {
                // 移除当前用例
                if (id.equals(ac.getCaseId()))
                {
                    caseList.remove(ac);
                    break;
                }
            }
        }
        return new JsonResultUtil(200,"操作成功", caseList);
    }


    /**
     * 分页查询系统中所有执行中的APP用例
     */
    @RequestMapping(value = "/getAllRunningAppCase",method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil getAllExecutingCase(PageBean<AppCase> pageBean, CaseExcResultVO resultVO)
    {
        return new JsonResultUtil(200, "查询成功", appCaseService.getAllRunningAppCase(pageBean, resultVO));
    }

    /**
     * 校验项目下所有用例的编号是唯一的
     * @return
     */
    @RequestMapping(value = "/checkCaseNumUnique/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultUtil checkCaseNumUnique(@PathVariable("id") Integer id, String caseNum, Integer caseId)
    {
        if (StringUtils.isEmpty(caseNum))
        {
            throw new TestSystemException("用例编号不能为空");
        }
        if (id == null)
        {
            throw new TestSystemException("参数缺失");
        }
        Business b = null;
        if (caseId != null)
        {
            b =  businessService.selectByPrimaryKey(id);
        }
        else
        {
            SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
            b =  businessService.selectByPrimaryKey(sysPermission.getDataId());
        }

        Map<String, Object> map = new HashMap<>();
        String msg = "";
        if (testCaseService.isExistSerialNumber(b.getProjectId(), caseId, caseNum))
        {
            map.put("unique", false);
            msg = "用例编号已存在";
        }
        else
        {
            map.put("unique", true);
            msg = "校验成功";
        }
        return new JsonResultUtil(200, msg, map);
    }


    /*@RequestMapping(value = "/downloadZip", method = RequestMethod.GET)
    public void downloadCompressPicZip(HttpServletResponse response, CommonResultVO caseVO)
    {

        List<DownloadFileDto> downloadFileDtoList = appCaseService.downloadCompressPicZip(caseVO);

        if (CollectionUtils.isEmpty(downloadFileDtoList))
        {
            throw new TestSystemException("无截图文件");
        }
        String fileName = "用例执行结果截图压缩包.zip";
        // 必要地清除response中的缓存信息
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin","*");
        try
        {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            // 在浏览器提示用户是保存还是下载
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        // 根据个人需要,这个是下载文件的类型
        //response.setContentType("application/octet-stream; charset=UTF-8");

        try
        {
            // 将截图文件压缩成zip包
            byte[] zipByteFile = ZipUtils.zipFile(downloadFileDtoList);
            // 告诉浏览器下载文件的大小
            //response.setHeader("Content-Length", String.valueOf(zipByteFile.length));
            response.getOutputStream().write(zipByteFile);
            response.flushBuffer();
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("截图文件压缩成zip包出现异常");
        }
    }*/
}
