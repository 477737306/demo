package com.cmit.testing.dao;

import com.cmit.testing.entity.Project;
import com.cmit.testing.entity.vo.PbtVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper extends BaseMapper<Project>{
    int deleteByPrimaryKey(Integer id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    int getProjectSum();

    List<Project> getAllProject();

    /**
     * 分页
     * @param name 用户名
     * @return
     */
    List<PbtVO> findByPage(@Param("name") String name);

    /**
     * 按照名称匹配项目，业务，用例
     * @param name
     * @return
     */
    List<PbtVO> pbtFindByPage(@Param("name") String name);


    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 项目统计
     * @return  项目数据统计（projetCount：项目总数，noexecute：未执行，executing：执行中，executed：已执行，filed：已归档，unfiled：未归档
     */
    Map<String,Long> projetCount();

}