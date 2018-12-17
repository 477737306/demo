package com.cmit.testing.entity.script;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO XML中对应的Step元素对象
 * @date 2018/9/6 0006 下午 3:32.
 */
public class Step {

    private List<Attribute> attributeList;

    public Step() {
    }

    @XmlElement(name = "attribute")
    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
}
