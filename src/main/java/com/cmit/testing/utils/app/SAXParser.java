package com.cmit.testing.utils.app;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/7 0007 下午 6:24.
 */
public class SAXParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(SAXParser.class);

    public static List<String> getAuthorityIdFromXml(String configPath) {
        if(configPath.isEmpty()){
            return null;
        }
        List<String> idList = new ArrayList<String>();
        // 创建SAXReader对象
        SAXReader reader = new SAXReader();
        // 读取文件 转换成Document
        Document document;
        try {
            document = reader.read(new File(configPath));
            // 获取根节点元素对象
            Element root = document.getRootElement();
            getListNodesId(root, idList);
            return idList;
        } catch (DocumentException e) {
            LOGGER.error("getAuthorityIdFromXml error: ", e);
            return null;
        }
    }

    /**
     * 遍历当前节点下的所有节点
     * @param node
     * @param idList
     */
    private static void getListNodesId(Element node, List<String> idList) {
        // 首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attribute : list) {
            LOGGER.debug("属性" + attribute.getName() + ":" + attribute.getValue());
        }
        // 如果当前节点内容不为空，则输出
        if (!(node.getTextTrim().equals("")) && node.getName().equals("id")) {
            LOGGER.debug(node.getName() + "：" + node.getText());
            idList.add(node.getText());
        }
        // 同时迭代当前节点下面的所有子节点
        // 使用递归
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            getListNodesId(e, idList);
        }
    }
}
