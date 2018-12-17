package com.cmit.testing.service;

import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.ModelScript;
import com.cmit.testing.entity.ScriptStep;
import com.cmit.testing.entity.SysPermission;

import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/26 11:07
 */
public interface ModelScriptService extends BaseService<ModelScript>{
    int addModelScript(ModelScript script, List<ScriptStep> steps);

    boolean updateModelScript(ModelScript script, List<ScriptStep> steps);

    List<ModelScript> getModelScripts(Business business);

    boolean deleteModelScript(ModelScript script);

    /**
     * 获取所有脚本
     *
     * @return
     */
    List<ModelScript> getAllByBusinessId(Integer id);

    /**
     * 根据modelScriptId来查询modelScript
     */
    ModelScript getModelScriptById(int modelScriptId);


    boolean copy(List<SysPermission> list, int menuParentId);

    int copyDetail(SysPermission sysPermission, SysPermission parentSysPermission);

    int copyData(ModelScript modelScript);

    int updateByPrimaryKey(ModelScript record);

    /**
     * shearModelScript
     * @param list //需要剪切的菜单list
     * @param menuParentId 剪切到目标对象的id
     */
    boolean shear(List<SysPermission> list, int menuParentId);

    Integer saveModelScript(Map<String, Object> map, Integer id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    int deleteByPrimaryKey(Integer id);

    /**
     * 根据脚本Id获取脚本步骤
     * @param modelScriptId
     * @return
     */
    List<ScriptStep> selectByScriptId(Integer modelScriptId);

    ModelScript selectByPrimaryKey(Integer id);

    /**
     * 根据脚本Id和参数类型获取数据集
     * @param scriptId
     * @param paramType 0默认1变量2全局
     * @return
     */
    List<ScriptStep> getByScriptIdAndParamType(Integer scriptId, List<Integer> paramType);
}
