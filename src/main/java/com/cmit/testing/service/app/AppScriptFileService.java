package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppScriptFile;
import com.cmit.testing.service.BaseService;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 2:05.
 */
public interface AppScriptFileService extends BaseService<AppScriptFile> {

    /**
     * 添加脚本文件信息
     * @param scriptFile
     * @return
     */
    int insertNotFull(AppScriptFile scriptFile);

    /**
     * 批量添加脚本文件信息
     * @param scriptFiles 脚本文件列表
     * @return 添加的条数
     */
    int insertList(List<AppScriptFile> scriptFiles);

    /**
     * 根据脚本ID查询脚本对应的脚本文件
     * @param scriptId
     * @return
     */
    List<AppScriptFile> getScriptFileByScriptId(Integer scriptId);

    int deleteScriptFileByScriptId(Integer scriptId);

    int updateAppScriptFile(AppScriptFile scriptFile);

}
