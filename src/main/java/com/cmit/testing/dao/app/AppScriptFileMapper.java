package com.cmit.testing.dao.app;

import com.cmit.testing.entity.app.AppScriptFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppScriptFileMapper {
    int deleteByPrimaryKey(Integer scriptFileId);
    int deleteByscriptId(Integer scriptId);

    int insert(AppScriptFile record);

    int insertSelective(AppScriptFile record);

    AppScriptFile selectByPrimaryKey(Integer scriptFileId);
    AppScriptFile selectByscriptId(Integer scriptId);
   List<AppScriptFile> listFileByscriptId(Integer scriptId);
    int updateByPrimaryKeySelective(AppScriptFile record);

    int updateByPrimaryKey(AppScriptFile record);
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
     * @param scriptIds
     * @return
     */
    List<AppScriptFile> listByScriptIds(@Param("ids") List<Integer> scriptIds);

    /**
     * 根据脚本ID查询对应的脚本文件
     * @param scriptId 脚本ID
     * @return
     */
    List<AppScriptFile> getScriptFileByScriptId(@Param("scriptId") Integer scriptId);

    /**
     * 根据脚本ID删除对应的脚本文件信息
     * @param scriptId
     * @return
     */
    int deleteScriptFileByScriptId(@Param("scriptId") Integer scriptId);
}