package com.cmit.testing.service.app;

import com.cmit.testing.entity.ModelScript;
import com.cmit.testing.entity.ScriptStep;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.app.AppScriptFile;
import com.cmit.testing.entity.app.AppScriptStep;
import com.cmit.testing.entity.vo.AppScriptVO;
import com.cmit.testing.entity.vo.ScriptVO;
import com.cmit.testing.entity.vo.SubAppScript;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

/**
 * creat by chenxiaozhang
 *
 */
public interface AppScriptService  extends BaseService<AppScript> {
    int  deleteByPrimaryKey(Integer id);

    int insert(AppScript record);

    int insertSelective(AppScript record);

    Map<String, Object> getAppScriptById(Integer id);



    int updateByPrimaryKey(AppScript appScript);
    public int updateByscriptId(AppScript appScript);

    /**
     * 校验脚本名称是否重复
     * @param scriptId 脚本ID
     * @param scriptName 脚本名称
     * @return
     */
    Map<String, Object> checkScriptName(String scriptId, String scriptName);

    /**
     * APP脚本上传
     */
    Map<String, Object> uploadScript(MultipartFile multipartFile, ScriptVO vo) throws Exception;

    /**
     * APP脚本保存
     */
    int saveAppScript(Map<String, Object> map, Integer id);

    /**
     * 根据脚本ID查询脚本
     */
    AppScriptVO getAppScriptByScriptId(Integer scriptId);

    int delScriptByIds(List<Integer> scriptIds);

    AppScript getScriptByScriptId(Integer scriptId);

    /**
     * 根据业务ID获取所有的APP脚本列表
     * @param businessId
     * @return
     */
    List<AppScript> getAllByBusinessId(Integer businessId);

    /**
     * 根据业务ID获取所有的APP脚本列表、脚本步骤
     * @param businessId
     * @return
     */
    List<SubAppScript> getAllByBusinessId1(Integer businessId);


    /**
     * 查看脚本关联的APP用例
     * @param pageBean
     * @param appCase
     * @return
     */
    PageBean<AppCase> getPageByScriptId(PageBean pageBean, AppCase appCase);


    boolean copy(List<SysPermission> list, int menuParentId);

    int copyDetail(SysPermission sysPermission, SysPermission parentSysPermission);

    int copyData(AppScript appScript, Integer oldId, Integer newBusinessId);

    //int addAppScript(AppScript appScript, List<AppScriptStep> appScriptSteps,List<AppScriptFile> appfiles);

    boolean shear(List<SysPermission> list, int menuParentId);
    AppScript getScriptById(Integer scriptId);
    int updateByPrimaryKeySelective(AppScript appScript);
}
