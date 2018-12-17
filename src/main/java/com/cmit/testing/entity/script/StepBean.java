package com.cmit.testing.entity.script;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 5:33.
 */
public class StepBean {

    /**
     * 动作类型：数据字典
     */
    private String actionCode;

    private String param1;

    private String param2;

    private String param3;

    private String desc;

    /**
     * 步骤序号：第几步执行
     */
    private String stepNo;

    private boolean isExistStart;

    private boolean isExistEnd;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public boolean isExistStart() {
        return isExistStart;
    }

    public void setExistStart(boolean existStart) {
        isExistStart = existStart;
    }

    public boolean isExistEnd() {
        return isExistEnd;
    }

    public void setExistEnd(boolean existEnd) {
        isExistEnd = existEnd;
    }
}
