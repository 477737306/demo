package com.cmit.testing.entity;

public class DicConst {
    private Integer id;  // 主键

    private String dvalue; //值

    private String dname; // 字典名称

    private String dtype; //类型

    private String dparentCode; // 上级字典编码

    private Integer status; // 状态

    private String remark; // 备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname == null ? null : dname.trim();
    }

    public String getDparentCode() {
        return dparentCode;
    }

    public void setDparentCode(String dparentCode) {
        this.dparentCode = dparentCode == null ? null : dparentCode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
}