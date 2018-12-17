package com.cmit.testing.entity.script;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author XieZuLiang
 * @description TODO XML文件中元素属性对象
 * @date 2018/9/6 0006 下午 3:34.
 */
public class Attribute {

    private String name;

    private String value;

    public Attribute() {
    }

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
