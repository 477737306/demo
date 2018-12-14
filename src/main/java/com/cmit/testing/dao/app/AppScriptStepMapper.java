package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppScriptStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppScriptStepMapper extends BaseMapper<AppScriptStep>{

    int deleteByPrimaryKey(Integer scriptStepId);

    int insert(AppScriptStep record);

    int insertSelective(AppScriptStep record);

    AppScriptStep selectByPrimaryKey(Integer scriptStepId);
    AppScriptStep selectByscriptId(Integer scriptId);
    int deleteByscriptId(Integer scriptId);
    int updateByPrimaryKeySelective(AppScriptStep record);

    int updateByPrimaryKey(AppScriptStep record);

    int deleteByIds(@Param("ids") List<Integer> ids);
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteByScriptIds(@Param("ids") List<Integer> ids);

    /**
     * 批量查询
     *
     * @param ids
     * @return
     */
    List<AppScriptStep> listByScriptIds(List<Integer> ids);

    /**
     * 根据脚本XML文件ID删除脚本步骤
     */
    int deleteByScriptFileId(@Param("scriptFileIds") List<Integer> xmlFileIds);

    /**
     * 根据脚本ID删除脚本步骤
     */
    int deleteByScriptId(@Param("scriptId") Integer scriptId);
    /**
     * 根据脚本id获取脚本信息
     * @param sid 脚本id
     * @return 查询的脚本步骤集合
     */
    List<AppScriptStep> listStepByScriptId(@Param("scriptId") Integer sid);


    List<AppScriptStep> getStepByScriptFileId(@Param("scriptFileId") Integer scriptFileId);

    List<AppScriptStep> selectByAppStep(AppScriptStep step);

    /**
     * 获取短信内容校验的关键字
     * @param params
     * @return
     */
    AppScriptStep getSmsVerifyByMap(Map<String, Object> params);

}