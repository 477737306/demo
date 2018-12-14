package com.cmit.testing.dao;

import com.cmit.testing.entity.ScriptStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScriptStepMapper extends BaseMapper<ScriptStep>{
    int deleteByPrimaryKey(Integer id);

    int insert(ScriptStep record);

    int insertSelective(ScriptStep record);

    ScriptStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScriptStep record);

    int updateByPrimaryKey(ScriptStep record);


    /**
     *  根据脚本scriptId查询或ParamType不等于
     * @param scriptId
     * @param noParamTypes  ParamType不等于
     * @return
     */
    List<ScriptStep> selectByScriptId(@Param("scriptId") Integer scriptId, @Param("paramTypes") List<Integer> noParamTypes);

    int deleteByModelScript(Integer id);

    /**
     * 根据脚本Id和参数类型获取数据集
     * @param scriptId
     * @param paramType 0默认1变量2全局
     * @return
     */
    List<ScriptStep> getByScriptIdAndParamType(@Param("scriptId") Integer scriptId, @Param("paramType") List<Integer> paramType);


}