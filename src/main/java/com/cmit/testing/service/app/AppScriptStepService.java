package com.cmit.testing.service.app;

import com.cmit.testing.entity.app.AppScriptStep;
import com.cmit.testing.entity.proxy.VerifySmsMsg;
import com.cmit.testing.service.BaseService;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 12:53.
 */
public interface AppScriptStepService extends BaseService<AppScriptStep> {

    /**
     * 批量添加脚本步骤信息
     * @param steps 脚本步骤列表
     * @return 批量添加的数据条数
     */
    int insertList(List<AppScriptStep> steps);

    /**
     * 批量删除脚本步骤
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 根据脚本XML文件ID删除脚本步骤
     */
    int deleteByScriptFileId(List<Integer> xmlFileId);

    /**
     * 根据脚本ID删除脚本步骤
     */
    int deleteByScriptId(Integer scriptId);

    List<AppScriptStep> getStepByScriptFileId(Integer scriptFileId);

    /**
     * 校验短信内容
     * @param smsMsg
     */
    void verifySmsContent(VerifySmsMsg smsMsg);

}
