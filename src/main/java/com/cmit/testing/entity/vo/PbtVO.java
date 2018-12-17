package com.cmit.testing.entity.vo;

import java.util.Date;

/**
 * 项目，业务，用例
 * @author YangWanLi
 * @date 2018/7/27 12:06
 */
public class PbtVO {
    private Integer id;

    private Date updateTime; //修改时间

    private Integer successNum; //成功数

    private Integer failureNum; //失败数

    private Double successRate; //成功率

    private String name; //名称

    private String tableFlag;//0项目表，1业务表,2用例表,3脚本

    private Integer code; //排序

    private Integer menuId; //菜单Id

    private Integer isFinish; //完成状态.0:执行完成，1:执行中，2:未执行，3:执行被终止，4:暂停


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableFlag() {
        return tableFlag;
    }

    public void setTableFlag(String tableFlag) {
        this.tableFlag = tableFlag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
