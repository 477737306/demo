package com.cmit.testing.service.impl;

import com.cmit.testing.dao.BusinessMapper;
import com.cmit.testing.dao.ProjectMapper;
import com.cmit.testing.dao.TestCaseMapper;
import com.cmit.testing.entity.Project;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.ProjectService;
import com.cmit.testing.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/12 17:52
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return projectMapper.deleteByPrimaryKey(id);
    }


    @Override
    public int deleteByIds(List<Integer> ids) {
        return projectMapper.deleteByIds(ids);
    }

    @Override
    public int insert(Project record) {
        projectMapper.insert(record);
        return record.getId();
    }

    @Override
    public int insertSelective(Project record) {
        return projectMapper.insertSelective(record);
    }

    @Override
    public Project selectByPrimaryKey(Integer id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Project record) {
        return updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Project record) {
        return projectMapper.updateByPrimaryKey(record);
    }

    @Override
    public int getProjectSum() {
        return projectMapper.getProjectSum();
    }

    @Override
    public List<Project> getAllProject() {
        return projectMapper.getAllProject();
    }

    @Override
    @Transactional
    public int saveProject(Map<String, Object> map,Integer id) {
        String careatBy = ShiroKit.getUser().getName();
        Project project = new Project();
        if(null!=id){
            project = projectMapper.selectByPrimaryKey(id);
            project.setUpdateBy(careatBy);
            project.setUpdateTime(new Date());
        }else {
            project.setCreateBy(careatBy);
            project.setUpdateBy(careatBy);
            project.setUpdateTime(new Date());
            project.setCreateTime(new Date());
        }
        project.setName(map.get("name").toString());
        project.setCode((Integer)map.get("code"));
        project.setSuccessNum(0);
        project.setFailureNum(0);
        project.setIsFinish(2);
        project.setSuccessRate(0.0);

//        project.setStatus();
//        project.setEnable();
        if(null!=id) {
            projectMapper.updateByPrimaryKey(project);
        }else {
            projectMapper.insert(project);
        }
            return project.getId();
    }



    /**
     * 复制项目
     * @param list 所选的项目菜单id
     * @return
     */
    @Override
    public boolean copyProjects(List<SysPermission> list) {
        int result = 0;
        for (SysPermission sysPermission : list) {
            Project project = projectMapper.selectByPrimaryKey(sysPermission.getDataId());
            List<SysPermission> sysList = sysPermissionService.getTreeMenu(sysPermission.getId(),null);

            //保存菜单对应的数据
            project.setId(null);
            project.setName(sysPermission.getName());
            int id = this.insert(project); //获取项目的id
            //保存菜单
            sysPermission.setId(null);
            sysPermission.setDataId(project.getId());
            result += sysPermissionService.saveProjectPermission(sysPermission);

            businessService.copy(sysList,project.getId());
        }
        return result > 0 ;

    }

    @Override
    public List<PbtVO> findByPage(String name) {
        return projectMapper.findByPage(name);
    }
    @Override
    public List<PbtVO>  pbtFindByPage(String name) {
        return  projectMapper.pbtFindByPage(name);
    }
}
