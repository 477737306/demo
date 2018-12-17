package com.cmit.testing.service.impl;

import com.cmit.testing.dao.BusinessMapper;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.ModelScriptService;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.AppScriptService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务ServiceImpl
 * @author YangWanLi
 * @date 2018/7/25 10:16
 */
@Service
public class BusinessServiceImpl extends BaseServiceImpl<Business> implements BusinessService{

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private ModelScriptService modelScriptService;

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private  AppScriptService appScriptService;
    @Autowired
    private AppCaseService appCaseService;

    @Override
    public int deleteByIds(List<Integer> ids) {
        return businessMapper.deleteByIds(ids);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return businessMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int saveBusiness(Map<String,Object> map,Integer id) {
        String careatBy  = getShiroUser().getName();
        Business record = new Business();
        if(null!=id){
            record = businessMapper.selectByPrimaryKey(id);
            record.setUpdateBy(careatBy);
            record.setUpdateTime(new Date());
        }else {
            record.setProjectId((Integer)map.get("dataId"));
            record.setCreateBy(careatBy);
            record.setUpdateBy(careatBy);
            record.setUpdateTime(new Date());
            record.setCreateTime(new Date());
        }
        record.setName(map.get("name").toString());
        record.setCode((Integer)map.get("code"));
        record.setSuccessNum(0);
        record.setFailureNum(0);
        record.setIsFinish(2);
        record.setSuccessRate(0.0);
        if(null!=id) {
            businessMapper.updateByPrimaryKey(record);
        }else {
            businessMapper.insert(record);
        }
            return record.getId();
    }

    @Override
    public int insertSelective(Business record) {
        businessMapper.insertSelective(record);
        return record.getId();
    }

    @Override
    public Business selectByPrimaryKey(Integer id) {
        return businessMapper.selectByPrimaryKey(id);
    }


    /**
     *  分页
     *
     * @param pageBean
     * @param business
     * @return
     */
    @Override
    public PageBean<PbtVO> findPage(PageBean<Object> pageBean, Business business) {
        Page<Object> page = PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<PbtVO> pbtVOS = businessMapper.findByPage(business);
        PageBean<PbtVO> page_   = new PageBean<>(pageBean.getCurrentPage(),pageBean.getPageSize(),(int)page.getTotal());
        page_.setItems(pbtVOS);
        return page_;
    }

    @Override
    public int updateByPrimaryKeySelective(Business record) {
        return businessMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Business record) {
        return businessMapper.updateByPrimaryKey(record);
    }


    @Override
    public boolean copy(List<SysPermission> list, int menuParentId) {
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        int result = 0;
        for (SysPermission sysPermission : list) {
            List<SysPermission> sysList = sysPermissionService.getTreeMenu(sysPermission.getId(),null);
            List<SysPermission> modelList = new ArrayList<>(); //业务下面的脚本
            List<SysPermission> testCaseList = new ArrayList<>();//业务下面的用例
            List<SysPermission> appScriptList = new ArrayList<>(); //业务下面的app脚本
            List<SysPermission> appCaseList = new ArrayList<>();//业务下面的app用例
            for (SysPermission sysPermission1:sysList) {
                if("scripts".equals(sysPermission1.getType())){
                    modelList.add(sysPermission1);
                }
                if("case".equals(sysPermission1.getType())){
                    testCaseList.add(sysPermission1);
                }
                if("appScript".equals(sysPermission1.getType())){
                    appScriptList.add(sysPermission1);
                }
                if("appCase".equals(sysPermission1.getType())){
                    appCaseList.add(sysPermission1);
                }
            }

            Business business = this.selectByPrimaryKey(sysPermission.getDataId());//根据业务菜单id获取业务
            /*List<ModelScript> modelScriptList = modelScriptMapper.selectBusinessScript(parentSysPermission.getDataId());
            List<TestCase> testCastList= testCaseService.getTestCasesByBusiness(business.getId());*/
            business.setId(null);
            business.setName(sysPermission.getName());
            business.setProjectId(parentSysPermission.getDataId());//设置父类id
            this.insertSelective(business); //保存业务
            //保存业务菜单
            sysPermission.setId(null);
            sysPermission.setDataId(business.getId());
            sysPermission.setParentId(parentSysPermission.getId());
            int id = sysPermissionService.saveProjectPermission(sysPermission);

            modelScriptService.copy(modelList,id);
            appScriptService.copy(appScriptList,id);
            testCaseService.copy(testCaseList,id);
            appCaseService.copy(appCaseList,id);
        }
        return true;
    }

    /**
     * 移动业务
     * @param list 选中的所有业务菜单id
     * @param menuParentId   移动到目标项目菜单id
     * @return
     */
    @Override
    public boolean shear(List<SysPermission> list, int menuParentId) {
        int sysResult = 0;
        int busResult = 0;
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission : list) {
            //更新菜单
            sysPermission.setParentId(menuParentId);
            sysResult += sysPermissionService.updateByPrimaryKey(sysPermission);
            //更新业务数据
            Business business = businessMapper.selectByPrimaryKey(sysPermission.getDataId());
            business.setProjectId(parentSysPermission.getDataId());
            business.setCode(sysPermission.getCode());
            busResult += businessMapper.updateByPrimaryKey(business);
        }
        return sysResult>0 && busResult>0;
    }

    @Override
    public List<Business> getBusinessListByProjectId(int projectId) {
        return businessMapper.getBusinessListByProjectId(projectId);
    }

    @Override
    public List<PbtVO> findByPage(String name, Integer projectId) {
        Business business = new Business();
        business.setProjectId(projectId);
        business.setName(name);
        return businessMapper.findByPage(business);
    }

    /**
     * 统计项目下业务的成功数与失败数
     * @param projectId
     * @return
     */
    @Override
    public Map<String,Long> businessCountNumber(Integer projectId){
        return businessMapper.businessCountNumber(projectId);
    }

    @Override
    public Map<String, String> getProjectNameByBusinessName(String name) {
        return businessMapper.getProjectNameByBusinessName(name);
    }

    @Override
    public Integer getExecutingCountByProjectId(Integer projectId) {
        return businessMapper.getExecutingCountByProjectId(projectId);
    }
}
