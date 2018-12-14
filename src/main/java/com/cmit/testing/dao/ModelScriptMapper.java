package com.cmit.testing.dao;

import com.cmit.testing.entity.ModelScript;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ModelScriptMapper extends BaseMapper<ModelScript>{
    int deleteByPrimaryKey(Integer id);

    int insert(ModelScript record);

    int insertSelective(ModelScript record);

    ModelScript selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelScript record);

    int updateByPrimaryKey(ModelScript record);

    /**
     * 获取通用脚本
     * @return
     */
    List<ModelScript> selectCommonScript();

    /**
     * 根据业务Id获取普通脚本
     * @param id
     * @return
     */
    List<ModelScript> selectBusinessScript(Integer id);

    /**
     * 获取所有脚本
     * @return
     */
    List<ModelScript> getAllByModelScript(ModelScript modelScript);

    /**
     * 根据modelScriptIds来查询modelScript
     */
    List<ModelScript> getModelScriptByIds(@Param("ids") String modelScriptIds);


    /**
     * 更新modelScript
     */
    int updateModelScript(ModelScript modelScript);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 脚本数据统计
     * @return     脚本数据统计（scriptnum：脚本总数，webnum：web数，appnum：app数
     */
    Map<String,Long> scriptCount();

    /**
     * 获取脚本总数
     */
    Integer getScriptCount();
}