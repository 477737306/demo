package com.cmit.testing.service.app.impl;

import com.cmit.testing.dao.app.AppScriptFileMapper;
import com.cmit.testing.entity.app.AppScriptFile;
import com.cmit.testing.service.app.AppScriptFileService;
import com.cmit.testing.service.app.AppScriptStepService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 2:08.
 */
@Service("appScriptFileService")
public class AppScriptFileServiceImpl extends BaseServiceImpl<AppScriptFile> implements AppScriptFileService {

    @Autowired
    private AppScriptFileMapper appScriptFileMapper;

    @Autowired
    private AppScriptStepService appScriptStepService;

    @Override
    public int insertNotFull(AppScriptFile scriptFile) {
        return appScriptFileMapper.insertSelective(scriptFile);
    }

    @Override
    public int insertList(List<AppScriptFile> scriptFiles) {
        int count = 0;
        for (AppScriptFile scriptFile : scriptFiles) {
            int i = appScriptFileMapper.insertSelective(scriptFile);
            count += i;
        }
        return count;
    }

    @Override
    public List<AppScriptFile> getScriptFileByScriptId(Integer scriptId) {
        return appScriptFileMapper.getScriptFileByScriptId(scriptId);
    }




    @Override
    public int deleteScriptFileByScriptId(Integer scriptId) {
        List<AppScriptFile> list = getScriptFileByScriptId(scriptId);
        List<Integer> fileIds = new ArrayList<>();
        for (AppScriptFile file : list )
        {
            fileIds.add(file.getScriptFileId());
        }
        // 删除脚本文件关联的步骤
        appScriptStepService.deleteByScriptFileId(fileIds);

        return appScriptFileMapper.deleteScriptFileByScriptId(scriptId);
    }

    @Override
    public int updateAppScriptFile(AppScriptFile scriptFile) {
        return appScriptFileMapper.updateByPrimaryKeySelective(scriptFile);
    }
}
