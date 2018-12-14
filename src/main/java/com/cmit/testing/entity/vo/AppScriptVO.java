package com.cmit.testing.entity.vo;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/11 0011.
 */
public class AppScriptVO {

    private Integer scriptId;

    private Integer scriptTag;

    private Integer scriptType;

    private String phoneHeight;

    private String phoneWidth;

    private Integer isClear;

    private Integer isUninstall;

    private Integer isInstall;

    private List<AppScriptFileVO> fileVoList;

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getScriptTag() {
        return scriptTag;
    }

    public void setScriptTag(Integer scriptTag) {
        this.scriptTag = scriptTag;
    }

    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public String getPhoneHeight() {
        return phoneHeight;
    }

    public void setPhoneHeight(String phoneHeight) {
        this.phoneHeight = phoneHeight;
    }

    public String getPhoneWidth() {
        return phoneWidth;
    }

    public void setPhoneWidth(String phoneWidth) {
        this.phoneWidth = phoneWidth;
    }

    public Integer getIsClear() {
        return isClear;
    }

    public void setIsClear(Integer isClear) {
        this.isClear = isClear;
    }

    public Integer getIsUninstall() {
        return isUninstall;
    }

    public void setIsUninstall(Integer isUninstall) {
        this.isUninstall = isUninstall;
    }

    public Integer getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(Integer isInstall) {
        this.isInstall = isInstall;
    }

    public List<AppScriptFileVO> getFileVoList() {
        return fileVoList;
    }

    public void setFileVoList(List<AppScriptFileVO> fileVoList) {
        this.fileVoList = fileVoList;
    }
}