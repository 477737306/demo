package com.cmit.testing.entity;

/**
 * 套餐表
 * @author weiBin
 * @date 2018/9/20
 */
public class Product {
    /**
     * id  主键
     */
    private String id ;
    /**
     * 套餐编号 一级能开
     */
    private String code ;
    /**
     * 套餐名称
     */
    private String name ;
    /**
     * 省份名称
     */
    private String province ;

    /**
     * 创建时间
     */
    private String createTime ;
    /**
     * 更新时间
     */
    private String updateTime ;

    public Product() {
    }

    public Product(String code, String name, String province) {
        this.code = code;
        this.name = name;
        this.province = province;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
