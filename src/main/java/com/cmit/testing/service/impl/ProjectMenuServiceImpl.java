package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.entity.*;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.*;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.AppScriptService;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.ToolUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author YangWanLi
 * @date 2018/8/2 11:05
 */
@Service
public class ProjectMenuServiceImpl  implements ProjectMenuService {

    private final static Logger logger = LoggerFactory.getLogger(Class.class);

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
    private AppCaseService appCaseService;         //APP用例Service

    @Autowired
    private AppScriptService appScriptService;     //APP脚本Service

    @Autowired
    private SysSurveyTaskService sysSurveyTaskService;

    @Autowired
    private SysTaskService sysTaskService;

    /**
     * 递归获取要删除的数据id集
     * @param menus      （菜单Id集）
     * @param projects   （项目id集）
     * @param business   （业务id集）
     * @param testCase   （web用例id集）
     * @param scripts    （web脚本id集）
     * @param appCase    （app用例id集）
     * @param appScript  （app脚本id集）
     * @param set
     */
    private void  getDataIds(List<Integer> menus , List<Integer> projects , List<Integer> business , List<Integer> testCase , List<Integer> scripts,List<Integer> appCase,List<Integer> appScript, List<SysPermission> set){
        if(set.size()>0){
            List<Integer> parentIds = new ArrayList<>();
            for (SysPermission sysPermission : set) {
                parentIds.add(sysPermission.getId());
                switch (sysPermission.getType()) {
                    case "project":
                        projects.add(sysPermission.getDataId());
                        break;
                    case "business":
                        business.add(sysPermission.getDataId());
                        break;
                    case "case":
                        testCase.add(sysPermission.getDataId());
                        break;
                    case "scripts":
                        scripts.add(sysPermission.getDataId());
                        break;
                    case "appCase":
                        appCase.add(sysPermission.getDataId());
                        break;
                    case "appScript":
                        appScript.add(sysPermission.getDataId());
                        break;
                }
            }
            List<SysPermission> sysPermissionList = sysPermissionService.getSysPermissionsByParentIds(parentIds);
            getDataIds(menus,projects,business,testCase, scripts,appCase,appScript, sysPermissionList);
            menus.addAll(parentIds);
        }

    }

    /**
     * 根据菜单id删除相关数据
     * @param ids
     */
    @Transactional
    public void deleteByIds(List<Integer> ids ){

        List<SysPermission> sysPermissionList =sysPermissionService.getPermissionsByIds(ids);
        List<Integer> menus  = new ArrayList<>();
        List<Integer> projects = new ArrayList<>();
        List<Integer> business = new ArrayList<>();
        List<Integer> testCase= new ArrayList<>();
        List<Integer> scripts = new ArrayList<>();
        List<Integer> appCase= new ArrayList<>();
        List<Integer> appScript = new ArrayList<>();
        getDataIds(menus,projects,business,testCase,scripts,appCase,appScript,sysPermissionList);
        if(appCase.size()>0)
        {   //app用例删除
            appCaseService.deleteByIds(appCase);
        }
        if(appScript.size()>0)
        {   //app脚本删除
            appScriptService.delScriptByIds(appScript);
        }
        if(testCase.size()>0) { //web用例删除
            testCaseService.deleteByIds(testCase);
        }
        if(scripts.size()>0) {  //web脚本删除
            modelScriptService.deleteByIds(scripts);
        }

        if(business.size()>0) { //业务删除
            businessService.deleteByIds(business);
        }
        if(projects.size()>0) { //项目删除
            projectService.deleteByIds(projects);
        }
        if(menus.size()>0) {
            sysPermissionService.deleteByIds(menus);
            sysPermissionService.deletePermissionsOfRoleByPermissionIds(menus);
        }
    }

    @Override
    public PageBean<PbtVO> findPage(int currentPage, int pageSize, String name, String tableFlag, Integer parentId) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<PbtVO> pbtVOS = new ArrayList<>();
        switch (tableFlag){
            case "project":
                pbtVOS = projectService.findByPage(name);//项目分页
                break;
            case "business":
                pbtVOS = businessService.findByPage(name,parentId);//业务分页
                break;
            case "case":
                pbtVOS = testCaseService.findByPage(name,parentId);    //用例分页
                break;
            default:
                pbtVOS =  projectService.pbtFindByPage(name); //匹配项目、业务、用例分页
        }

        PageBean<PbtVO> page_   = new PageBean<>(currentPage,pageSize,(int)page.getTotal());
        page_.setItems(pbtVOS);
        return page_;
    }

    @Override
    public int save(Map<String,Object> map) {
        String careatBy = ShiroKit.getUser().getName();

        SysPermission sysPermission_ = new SysPermission();
        Integer dataId = null;
        if(map.get("id")!= null && StringUtils.isNotEmpty(map.get("id").toString())){
           sysPermission_ = sysPermissionService.selectByPrimaryKey(Integer.parseInt(map.get("id").toString()));
           dataId = sysPermission_.getDataId();
        }
        String type = map.get("tableFlag").toString();

        //按名字查重
        List<SysPermission> sysPermissions = sysPermissionService.duplicateCheck(null,dataId,null,map.get("name").toString());
        if(CollectionUtils.isNotEmpty(sysPermissions)){
            throw new TestSystemException("名称已存在");
        }
        int id= 0;
        switch (type){
            case "project":
                id= projectService.saveProject(map,dataId);
                break;
            case "business":
                id= businessService.saveBusiness(map,dataId);
                break;
            case "case":
                id= testCaseService.saveTestCase(map,dataId);
                break;
            case "scripts":
                id= modelScriptService.saveModelScript(map,dataId);
                break;
            case "appCase":
                id = appCaseService.saveAppCase(map, dataId);
                break;
            case "appScript":
                id = appScriptService.saveAppScript(map, dataId);
                break;
        }
        if(map.get("id")== null || StringUtils.isEmpty(map.get("id").toString())) {
            //添加测试角色与菜单关联关系（注意：测试角色是固定角色）
            sysPermission_.setDataId(id);
            sysPermission_.setName(map.get("name").toString());
            sysPermission_.setParentId((Integer) map.get("parentId"));
            sysPermission_.setUrl(map.get("url").toString());
            sysPermission_.setCode((Integer) map.get("code"));
            sysPermission_.setIcon(map.get("icon").toString());
            sysPermission_.setType(type);
            sysPermission_.setNameCopyNum(0);
            sysPermission_.setCreateTime(new Date());
            sysPermission_.setCreateBy(careatBy);
            sysPermission_.setUpdateTime(new Date());
            sysPermission_.setUpdateBy(careatBy);
            sysPermissionService.saveProjectPermission(sysPermission_);
        }else {
            sysPermission_.setUpdateTime(new Date());
            sysPermission_.setUpdateBy(careatBy);
            sysPermission_.setName(map.get("name").toString());
            sysPermissionService.updateByPrimaryKeySelective(sysPermission_);
        }
        return sysPermission_.getId();
    }

    @Override
    @Transactional
    public int updateNameById(Integer id,String name) {
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        //按名字查重
        List<SysPermission> sysPermissions = sysPermissionService.duplicateCheck(null,sysPermission.getDataId(),null,name);
        if(CollectionUtils.isNotEmpty(sysPermissions)){
            throw new TestSystemException("名称已存在");
        }
        SysPermission sysPermission_ = new SysPermission();
        sysPermission_.setId(id);
        sysPermission_.setName(name);
        sysPermissionService.updateByPrimaryKeySelective(sysPermission_);
        switch (sysPermission.getType()){
            case "project":
                Project project = projectService.selectByPrimaryKey(sysPermission.getDataId());
                project.setName(name);
                projectService.updateByPrimaryKey(project);
                break;
            case "business":
                Business business = businessService.selectByPrimaryKey(sysPermission.getDataId());
                business.setName(name);
                id= businessService.updateByPrimaryKey(business);
                break;
            case "case":
                TestCase testCase = testCaseService.selectByPrimaryKey(sysPermission.getDataId());
                testCase.setName(name);
                id= testCaseService.updateByPrimaryKey(testCase);
                break;
            case "scripts":
                ModelScript modelScript = modelScriptService.selectByPrimaryKey(sysPermission.getDataId());
                modelScript.setId(sysPermission.getDataId());
                modelScript.setName(name);
                id= modelScriptService.updateByPrimaryKey(modelScript);
                break;
            case "appCase":
                AppCase appCase = appCaseService.selectByPrimaryKey(sysPermission.getDataId());
                appCase.setCaseName(name);
                id = appCaseService.updateByPrimaryKeySelective(appCase);
                break;
            case "appScript":
                AppScript appScript =appScriptService.getScriptById(sysPermission.getDataId());

                 appScript.setScriptName(name);
                id = appScriptService.updateByscriptId(appScript);
                break;
        }

        return sysPermission_.getId();
    }


    /**
     * 用例执行前更改数据
     * @param businessId 业务id
     */
    @Override
    public void exeBeforeUpdateData(Integer businessId){
        Business business = businessService.selectByPrimaryKey(businessId);
        business.setIsFinish(1);
        businessService.updateByPrimaryKey(business);//更改业务执行状态

        Project project = projectService.selectByPrimaryKey(business.getProjectId());
        project.setIsFinish(1);
        projectService.updateByPrimaryKey(project);//更改项目执行状态
    }


    /**
     * 用例执行完成后更改数据
     * @param businessId 业务id
     */
    @Override
    public void exeAfterUpdateData(Integer businessId){
        //业务数据更改
        Business business = businessService.selectByPrimaryKey(businessId);
        Long bSuccessNum = 0L;
        Long bFailureNum = 0L;
        Long bTotalNum = 0L;
        Integer bExecutingNum = 0;

        //计算web
        Map<String,Long> webTestCaseCountNumber =  testCaseService.testCaseCountNumber(businessId);
        bSuccessNum += webTestCaseCountNumber.get("successNum");
        bFailureNum += webTestCaseCountNumber.get("failureNum");
        bExecutingNum = testCaseService.getExecutingCountByBusiness(businessId);//业务下用例执行中的个数

        //计算app
        Map<String,Long> appTestCaseCountNumber = appCaseService.appCaseCountNumber(businessId);
        bSuccessNum += appTestCaseCountNumber.get("successNum");
        bFailureNum += appTestCaseCountNumber.get("failureNum");
        // 统计业务下所有APP用例正在执行的个数
        bExecutingNum += appCaseService.getExecutingCountByBusiness(businessId);//业务下用例执行中的个数

        Double successRate = 0d;
        bTotalNum = bSuccessNum+bFailureNum;
        if(bTotalNum>0){
            successRate = bSuccessNum/bTotalNum*1d;
        }
        business.setSuccessNum(bSuccessNum.intValue());
        business.setFailureNum(bFailureNum .intValue());
        business.setSuccessRate(ToolUtil.numericalPrecision(successRate,2));
        business.setIsFinish(2);
        if(bExecutingNum>0){
            business.setIsFinish(1);
        }
        businessService.updateByPrimaryKey(business);

        //项目数据更改
        Project project = projectService.selectByPrimaryKey(business.getProjectId());
        Long pSuccessNum = 0L;
        Long pFailureNum = 0L;
        Long pTotalNum = 0L;
        //计算业务
        Map<String,Long> businessCountNumber =  businessService.businessCountNumber(business.getProjectId());
        pSuccessNum += businessCountNumber.get("successNum");
        pFailureNum += businessCountNumber.get("failureNum");
        pTotalNum = pSuccessNum+pFailureNum;
        Integer pExecutingNum = 0;
        Double pSuccessRate = 0d;
        pExecutingNum = businessService.getExecutingCountByProjectId(businessId);//项目下用例执行中的个数

        if(pTotalNum>0){
            pSuccessRate = pSuccessNum/pTotalNum*1d;
        }
        project.setSuccessNum(pSuccessNum.intValue());
        project.setFailureNum(pFailureNum.intValue());
        project.setSuccessRate(ToolUtil.numericalPrecision(pSuccessRate,2));
        project.setIsFinish(2);
        if(pExecutingNum>0){
            project.setIsFinish(1);
        }
        projectService.updateByPrimaryKey(project);
    }

    /**
     * 检测用例于批量任务或众测任务是否关联
     * @param id
     * @return
     */
    public boolean checkCorrelation(Integer id){
        List<Integer> webIds = new ArrayList<>();
        List<Integer> appIds = new ArrayList<>();
        SysPermission sysPermission = sysPermissionService.selectByPrimaryKey(id);
        if ("case".equals(sysPermission.getType())) {
            webIds.add(sysPermission.getDataId());
        }else if ("appCase".equals(sysPermission.getType())) {
            appIds.add(sysPermission.getDataId());
        }
        List<SysPermission> sysPermissions = sysPermissionService.getPermissions(0,10000);
        sysPermissionService.getCaseIds(id,sysPermissions,webIds,appIds);

        //查看众测任务有没有关联
        List<SysSurveyTask> sysSurveyTasks = sysSurveyTaskService.findAll(null,null);
        try {
            for (SysSurveyTask sysSurveyTask : sysSurveyTasks) {
                if(StringUtils.isEmpty(sysSurveyTask.getCaseIds())){
                    continue;
                }

                JSONObject jsonObject = JsonUtil.parseJsonObject(sysSurveyTask.getCaseIds());
                List<Integer> webCaseIds =(List<Integer>) jsonObject.get("web");
                List<Integer> appCaseIds = (List<Integer>) jsonObject.get("app");

                webCaseIds.retainAll(webIds);
                appCaseIds.retainAll(appIds);
                if (!webCaseIds.isEmpty()||!appCaseIds.isEmpty()) {
                    throw new TestSystemException();
                }
            }
            List<SysTask> sysTasks = sysTaskService.getAll();
            for (SysTask sysTask : sysTasks) {
                if(StringUtils.isEmpty(sysTask.getIds())){
                    continue;
                }
                JSONObject jsonObject = JsonUtil.parseJsonObject(sysTask.getIds());
                List<Integer> webCaseIds = (List<Integer>) jsonObject.get("web");
                List<Integer> appCaseIds = (List<Integer>) jsonObject.get("app");
                webCaseIds.retainAll(webIds);
                appCaseIds.retainAll(appIds);
                if (!webCaseIds.isEmpty()||!appCaseIds.isEmpty()) {
                    throw new TestSystemException();
                }
            }
        }catch (TestSystemException t){
            return false;
        }
       return true;
    }

    public static void main(String[] args) {
        List<Integer> webIds = new ArrayList<>();
        webIds.add(1);
        List<Integer> appIds = new ArrayList<>();
        appIds.add(2);
        if(webIds.retainAll(appIds)){
            System.out.println(true);
        }else {
            System.out.println(false);
        }
    }
}
