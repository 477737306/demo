package com.cmit.testing.entity.vo;

import com.cmit.testing.entity.app.AppScript;
import com.cmit.testing.entity.app.AppScriptStep;

import java.util.List;

/**
 * 新建用例脚本初始化的对象，返回给前端
 * Created by zhangxiaofang on 2018/9/26.
 */

public class SubAppScript extends AppScript {
    /**
     * 脚本步骤
     */
    private List<AppScriptStep> step;

    public List<AppScriptStep> getStep() {
        return step;
    }

    public void setStep(List<AppScriptStep> step) {
        this.step = step;
    }
}
