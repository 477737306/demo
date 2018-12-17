package com.cmit.testing.entity.app;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppApk  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String apkName;

    private String apkVersion;

    private String pkgName;

    private Integer apkType;

    private String iconPath;

    private String apkUrl;

    private String apkSize;

    private String apkCategory;

    private String md5Code;

    private Date createTime;

    private Integer createPerson;

    private Date updateTime;

    private Integer updatePerson;

    private String createPersonName;

    private String updatePersonName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName == null ? null : apkName.trim();
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion == null ? null : apkVersion.trim();
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName == null ? null : pkgName.trim();
    }

    public Integer getApkType() {
        return apkType;
    }

    public void setApkType(Integer apkType) {
        this.apkType = apkType;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath == null ? null : iconPath.trim();
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl == null ? null : apkUrl.trim();
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize == null ? null : apkSize.trim();
    }

    public String getApkCategory() {
        return apkCategory;
    }

    public void setApkCategory(String apkCategory) {
        this.apkCategory = apkCategory == null ? null : apkCategory.trim();
    }

    public String getCreatePersonName() {return createPersonName;}

    public void setCreatePersonName(String createPersonName) {this.createPersonName = createPersonName;}

    public String getUpdatePersonName() {return updatePersonName;}

    public void setUpdatePersonName(String updatePersonName) {this.updatePersonName = updatePersonName;}

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code == null ? null : md5Code.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(Integer updatePerson) {
        this.updatePerson = updatePerson;
    }
}