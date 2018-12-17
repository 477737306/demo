package com.cmit.testing.service;

import com.cmit.testing.entity.Project;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.vo.PbtVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/12 17:51
 */
public interface ProjectService extends BaseService<Project>{

    int getProjectSum();

    List<Project> getAllProject();

    int saveProject(Map<String, Object> map, Integer id);



    /**
     * 按照名称匹配项目，业务，用例
     *
     * @param name
     * @return
     */
    List<PbtVO>  pbtFindByPage(String name);


    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    boolean copyProjects(List<SysPermission> list);

    /**
     * 分页
     * @param name 用户名
     * @return
     */
    List<PbtVO> findByPage(String name);
}
