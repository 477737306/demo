package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysReport;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.SysReportService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/report")
@Permission
public class SysReportController  extends BaseController{

    @Autowired
    private SysReportService sysReportService;

    @RequestMapping(value="getReportAll",method = RequestMethod.GET)
    public JsonResultUtil getReportAll(PageBean<SysReport> pageBean , SysReport sysReport){

        return new JsonResultUtil(200,"操作成功",sysReportService.getReportAll(pageBean ,sysReport));
    }

    @RequestMapping(value="getReportBySysTaskId",method = RequestMethod.GET)
    public JsonResultUtil getReportBySysTaskId( SysReport sysReport){
        List<SysReport> sysReports = sysReportService.getAll(sysReport);
        if(!CollectionUtils.isEmpty(sysReports)){
            sysReport = sysReports.get(0);
        }
        return new JsonResultUtil(200,"操作成功",sysReport);
    }


    @RequestMapping(value="add",method = RequestMethod.POST)
    public JsonResultUtil add(@RequestBody Map<String,Object> map){
        String reportName = (String) map.get("reportName");
//        Integer projectId = (Integer) map.get("projectId");
        SysReport sysReport = new SysReport();
        sysReport.setReportName(reportName);
        sysReport.setCreateBy(getShiroUser().getAccount());
        sysReport.setCreateTime(new Date());
//        sysReport.setProjectId(projectId);
//        List<Map<String,String>> list = (List<Map<String,String>>)map.get("businessList");
        Map<String, Object> listMap = sysReportService.add(sysReport,map);

        return new JsonResultUtil(listMap);
    }

    @RequestMapping(value="getReportById",method = RequestMethod.GET)
    public JsonResultUtil getReportById(String id){
        SysReport sysReport = sysReportService.selectByPrimaryKey(Integer.parseInt(id));
        return new JsonResultUtil(sysReport);
    }

    @RequestMapping(value="update/{id}",method = RequestMethod.PUT)
    public JsonResultUtil add(@PathVariable("id")Integer id,@RequestBody Map<String,Object> map){
        SysReport sysReport = sysReportService.selectByPrimaryKey(id);
        sysReport.setId(null);
        sysReport.setReportName(map.get("name").toString());
        sysReport.setCreateTime(new Date());
        sysReport.setSysTaskId(null);
        sysReport.setCreateBy(getShiroUser().getName());
        sysReportService.insert(sysReport);
        return new JsonResultUtil(200,"操作成功");
    }


//    /**
//     * 导出报告
//     * @param id
//     * @param response
//     */
//    @RequestMapping(value="exportReport" ,method = RequestMethod.GET)
//    public void exportReport(String id, HttpServletResponse response){
//        SysReport sysReport = sysReportService.selectByPrimaryKey(Integer.parseInt(id));
//        Map<String,Object> map = (Map<String,Object>) JSON.parse(sysReport.getData().toString());
//        try {
//            ExportReportUtil.exportExelMerge(map,response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
