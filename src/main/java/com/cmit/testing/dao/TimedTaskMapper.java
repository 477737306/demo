package com.cmit.testing.dao;

import com.cmit.testing.entity.TimedTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TimedTaskMapper extends BaseMapper<TimedTask>{
    int deleteByPrimaryKey(Integer id);

    int insert(TimedTask record);

    int insertSelective(TimedTask record);

    TimedTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimedTask record);

    int updateByPrimaryKey(TimedTask record);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据用例id和用例类型修改
     */
    int updateByTestCaseIdAndType(TimedTask timedTask);

    int updateStatusByCaseIdAndType(TimedTask timedTask);
    
   /**
    * 根据条件查询定时任务信息
    * @param timedTask
    * @return
    */
    List<TimedTask> findAll(TimedTask timedTask);

    /**
     * 根据用例id和用例类型删除定时任务
     * @param id
     * @param type
     * @return
     */
    int deleteByTestCaseIdAndType(@Param("id") Integer id, @Param("type") String type);

}