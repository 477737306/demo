package com.cmit.testing.entity.vo;

import com.cmit.testing.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/10 0010 下午 4:52.
 */
public class AppDeviceVO extends Object{

    /**
     * 新增字段 用于关联应用参数
     */
    private Integer appId;

    private Integer deviceId;

    /**
     * 机型：品牌+型号
     */
    private String devBv;

    /**
     * 省份
     */
    private String province;
    /**
     * SIM卡品牌（全球通、神州行、动感地带）
     */
    private List<String> brand;

    /**
     * 手机号码
     */
    private String telNum;

    /**
     * 终端机使用状态：0-空闲；1-占用；2-预占用
     */
    private Integer useStatus;
    /**
     * 终端机在线状态：0-离线；1-在线
     */
    private String onlineStatus;
    /**
     * SIM卡所属省份
     */
    private List<String> provinceList;

    /**
     * SIM卡状态：0-停机；1-正常
     */
    private String availableStatus;
    /**
     * SIM卡最大余额
     */
    private Double maxBalance;
    /**
     * SIM卡最少余额
     */
    private Double minBalance;

    /**
     * 是否办理套餐：
     * 0-未办理
     * 1-办理
     */
    private Integer isTransact;
    /**
     * 新增字段   关联手机的账号密码
     */
    private String accountPwd;
    private List<Map<String,Object>> orderStatusList = new ArrayList<>(); //筛选条件是否办理业务，0未办理，1已办理;套餐名称集合

    private Integer orderStatus;    //筛选条件是否办理业务，0未办理，1已办理

    private String product;    //套餐名称

    @JsonIgnore
    private List<Product> productList;

    public List<Map<String, Object>> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<Map<String, Object>> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevBv() {
        return devBv;
    }

    public void setDevBv(String devBv) {
        this.devBv = devBv;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public List<String> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<String> provinceList) {
        this.provinceList = provinceList;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public Double getMaxBalance() {
        return maxBalance;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setMaxBalance(Double maxBalance) {
        this.maxBalance = maxBalance;
    }

    public Double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(Double minBalance) {
        this.minBalance = minBalance;
    }

    public Integer getIsTransact() {
        return isTransact;
    }

    public void setIsTransact(Integer isTransact) {
        this.isTransact = isTransact;
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppDeviceVO that = (AppDeviceVO) o;
        return Objects.equals(appId, that.appId) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(devBv, that.devBv) &&
                Objects.equals(province, that.province) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(telNum, that.telNum) &&
                Objects.equals(useStatus, that.useStatus) &&
                Objects.equals(onlineStatus, that.onlineStatus) &&
                Objects.equals(provinceList, that.provinceList) &&
                Objects.equals(availableStatus, that.availableStatus) &&
                Objects.equals(maxBalance, that.maxBalance) &&
                Objects.equals(minBalance, that.minBalance) &&
                Objects.equals(isTransact, that.isTransact) &&
                Objects.equals(accountPwd, that.accountPwd) &&
                Objects.equals(orderStatusList, that.orderStatusList) &&
                Objects.equals(orderStatus, that.orderStatus) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, deviceId, devBv, province, brand, telNum, useStatus, onlineStatus, provinceList, availableStatus, maxBalance, minBalance, isTransact, accountPwd, orderStatusList, orderStatus, product);
    }
}
