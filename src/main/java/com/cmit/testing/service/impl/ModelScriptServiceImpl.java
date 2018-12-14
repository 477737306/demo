package com.cmit.testing.service.impl;

import com.cmit.testing.dao.ModelScriptMapper;
import com.cmit.testing.dao.ScriptStepMapper;
import com.cmit.testing.dao.TestCaseMapper;
import com.cmit.testing.entity.*;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.DicConstService;
import com.cmit.testing.service.ModelScriptService;
import com.cmit.testing.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Suviky on 2018/7/26 11:07
 */
@Service
public class ModelScriptServiceImpl extends BaseServiceImpl<ModelScript> implements ModelScriptService {
    @Autowired
    private ModelScriptMapper modelScriptMapper;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private ScriptStepMapper scriptStepMapper;
    @Autowired
    private DicConstService dicConstService;
    @Autowired
    private TestCaseMapper testCaseMapper;


    @Override
    public int addModelScript(ModelScript script, List<ScriptStep> steps) {
        try {
            modelScriptMapper.insert(script);
            steps.stream().forEach(step -> {step.setScriptId(script.getId());scriptStepMapper.insertSelective(step);});
            return script.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public boolean updateModelScript(ModelScript script, List<ScriptStep> steps) {
        try {
            scriptStepMapper.deleteByModelScript(script.getId());
            modelScriptMapper.updateByPrimaryKeySelective(script);
            steps.stream().forEach(step -> scriptStepMapper.insertSelective(step));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ModelScript> getModelScripts(Business business) {
        List<ModelScript> common = modelScriptMapper.selectCommonScript();//通用脚本
        List<ModelScript> biz = modelScriptMapper.selectBusinessScript(business.getId());//普通脚本（挂在业务下）
        if(common.addAll(biz)){
            return common;
        }else{
            return null;
        }
    }

    @Override
    public boolean deleteModelScript(ModelScript script) {
        try {
            modelScriptMapper.deleteByPrimaryKey(script.getId());
            scriptStepMapper.deleteByModelScript(script.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ModelScript> getAllByBusinessId(Integer id) {
        List<ModelScript> modelScripts = new ArrayList<>();
        ModelScript modelScript = new ModelScript();
        modelScript.setTag(0);
        modelScripts.addAll(modelScriptMapper.getAllByModelScript(modelScript)); //通用脚本
        modelScript.setTag(1);
        modelScript.setBusinessId(id);
        modelScripts.addAll(modelScriptMapper.getAllByModelScript(modelScript));  //本业务下的脚本
        for(ModelScript modelScript1 : modelScripts){
            List<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(3);
            modelScript1.setStep(scriptStepMapper.selectByScriptId(modelScript1.getId(),list));
        }
        return modelScripts;
    }

    @Override
    public ModelScript getModelScriptById(int modelScriptId) {
        ModelScript modelScript= modelScriptMapper.selectByPrimaryKey(modelScriptId);
        List<ScriptStep> scriptSteps =  scriptStepMapper.selectByScriptId(modelScriptId,null);
        modelScript.setStep(scriptSteps);
        return modelScript;
    }

    @Override
    public boolean copy(List<SysPermission> list, int menuParentId) {
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission: list) {
            this.copyDetail(sysPermission,parentSysPermission);
        }
        return true;
    }

    @Override
    public int copyDetail(SysPermission sysPermission,SysPermission parentSysPermission) {
        ModelScript modelScript = modelScriptMapper.selectByPrimaryKey(sysPermission.getDataId());
        //复制脚本复制脚本参数
        modelScript.setBusinessId(parentSysPermission.getDataId());
        modelScript.setName(sysPermission.getName());
        int scriptid = this.copyData(modelScript);
        sysPermission.setId(null);
        sysPermission.setDataId(scriptid);
        sysPermission.setNameCopyNum(0);
        sysPermission.setParentId(parentSysPermission.getId());
        sysPermissionService.saveProjectPermission(sysPermission);

        return scriptid;
    }

    @Override
    public int copyData(ModelScript modelScript) {
        List<ScriptStep> scriptSteps = scriptStepMapper.selectByScriptId(modelScript.getId(),null);
        for (ScriptStep scriptStep : scriptSteps) {
            scriptStep.setId(null);
        }
        modelScript.setId(null);
        modelScript.setName(modelScript.getName());
        return this.addModelScript(modelScript,scriptSteps);
    }


    /**
     * 剪切脚本
     * @param list 剪切的菜单list
     * @param menuParentId 剪切到目标对象的id
     * @return
     */
    @Override
    public boolean shear(List<SysPermission> list, int menuParentId) {
        int sysResult = 0;
        int scriptResult = 0;
        SysPermission parentSysPermission = sysPermissionService.selectByPrimaryKey(menuParentId);
        for (SysPermission sysPermission: list) {
            //更新脚本菜单
            sysPermission.setParentId(menuParentId);
            sysResult = sysPermissionService.updateByPrimaryKey(sysPermission);
            //更新脚本数据
            ModelScript modelScript = modelScriptMapper.selectByPrimaryKey(sysPermission.getDataId());
            modelScript.setBusinessId(parentSysPermission.getDataId());
            modelScript.setCode(sysPermission.getCode());
            scriptResult = modelScriptMapper.updateByPrimaryKey(modelScript);
        }
        return (sysResult > 0 && scriptResult > 0);
    }

    @Override
    public Integer saveModelScript(Map<String, Object> map,Integer id){
        String careatBy  = ShiroKit.getUser().getName();
        Integer tag = (Integer) map.get("tag");
        ModelScript modelScript = new ModelScript();
        if(null!=id){
            modelScript = modelScriptMapper.selectByPrimaryKey(id);
            modelScript.setUpdateBy(careatBy);
            modelScript.setUpdateTime(new Date());

            //删除的参数记录
            scriptStepMapper.deleteByModelScript(id);
        }else {
            if (tag==1) {
                modelScript.setBusinessId((Integer) map.get("dataId"));
            }
            modelScript.setCreateBy(careatBy);
            modelScript.setUpdateBy(careatBy);
            modelScript.setUpdateTime(new Date());
            modelScript.setCreateTime(new Date());
        }
        modelScript.setName(map.get("name").toString());


        modelScript.setTag(tag);
        modelScript.setType((Integer) map.get("type"));
        modelScript.setRetry((Integer) map.get("retry"));
        modelScript.setDescription((String) map.get("description"));
        if(null!=id) {
            modelScriptMapper.updateByPrimaryKey(modelScript);
        }else {
            modelScriptMapper.insert(modelScript);
        }

        List<Map<String,Object>> mapList =(List<Map<String,Object>>) map.get("step");
        int a=1;
        boolean bool = false;
        for (Map<String,Object> map1 : mapList) {
            ScriptStep scriptStep = new ScriptStep();
            try {
                int paramType =(int) map1.get("paramType");
                String paramValue = map1.get("paramValue")+"";
                String paramKey = (String)map1.get("paramKey");
                String paramName = (String)map1.get("paramName");
                String remark = map1.get("remark")==null?null:map1.get("remark").toString();
                if(!bool && "open".equals(map1.get("command"))){
                    bool =true;
                }
                if("url".equals(paramKey)){
                    if(paramValue.length()>=4) {
                        String http = paramValue.substring(0, 4);
                        if(!"http".toLowerCase().equals(http.toLowerCase())){
                            paramValue= "http://"+paramValue;
                        }
                    }else {
                        paramValue= "http://"+paramValue;
                    }

                }else if(!"browser".equals(paramKey)){
                    paramKey = paramValue;
                }
                if(2==paramType){
                    DicConst dicConst =new DicConst();
                    dicConst.setDname(paramValue);
                    dicConst.setDvalue(paramValue);
                    dicConst.setDtype("constant");
                    dicConst.setStatus(1);
                    dicConst.setRemark("脚本参数");
                    dicConstService.insertSelective(dicConst);
                }

                scriptStep.setRemark(remark);
                scriptStep.setParamValue(paramValue);
                scriptStep.setParamKey(paramKey);
                scriptStep.setCommand((String)map1.get("command"));
                scriptStep.setParamName(paramName);
                scriptStep.setOrientType( (String)map1.get("orientType"));
                scriptStep.setOrientValue((String)map1.get("orientValue"));
                scriptStep.setStep(a);
                scriptStep.setScriptId(modelScript.getId());
                scriptStep.setParamType(paramType);
                scriptStep.setMatchKey((String)map1.get("matchKey"));

                scriptStepMapper.insert(scriptStep);
                a++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(bool && mapList.size()>0 && !"quit".equals(mapList.get(mapList.size()-1).get("command"))) { //最后一步不是退出浏览器，就自动加上该步骤
            ScriptStep scriptStep = new ScriptStep();
            scriptStep.setRemark("退出浏览器");
            scriptStep.setParamValue(null);
            scriptStep.setParamKey("");
            scriptStep.setCommand("quit");
            scriptStep.setParamName("退出浏览器");
            scriptStep.setOrientType("");
            scriptStep.setOrientValue("");
            scriptStep.setStep(a + 1);
            scriptStep.setScriptId(modelScript.getId());
            scriptStep.setParamType(0);
            scriptStep.setMatchKey("");
            scriptStepMapper.insert(scriptStep);
        }
        return  modelScript.getId();
    }

    @Override
    @Transactional
    public int deleteByIds(List<Integer> ids) {

        List<TestCase> testCaseList = new ArrayList<>();
        TestCase testCase = new TestCase();
        for(Integer id : ids) {
            testCase.setScriptId(id);
            testCaseList.addAll(testCaseMapper.getListByTestCase(testCase));
        }
        if(testCaseList.size()>0){
            throw new TestSystemException("脚本被其他用例所关联不能删除");
        }
        int a = modelScriptMapper.deleteByIds(ids);
        for(Integer id : ids) {
            scriptStepMapper.deleteByModelScript(id);
        }
        return a;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return modelScriptMapper.deleteByPrimaryKey(id);
    }

    @Override
    public  List<ScriptStep> selectByScriptId(Integer modelScriptId){
        return scriptStepMapper.selectByScriptId(modelScriptId,null);
    }

    @Override
    public int updateByPrimaryKey(ModelScript record) {
        return modelScriptMapper.updateByPrimaryKey(record);
    }

    @Override
    public ModelScript selectByPrimaryKey(Integer id){
       return modelScriptMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ScriptStep> getByScriptIdAndParamType(Integer scriptId, List<Integer> paramType) {
        return scriptStepMapper.getByScriptIdAndParamType(scriptId,paramType);
    }
}
