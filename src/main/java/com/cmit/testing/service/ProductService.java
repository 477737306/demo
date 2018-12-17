package com.cmit.testing.service;

import com.cmit.testing.entity.Product;

import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/20
 */
public interface ProductService {
    /**
     * 根据套餐信息查询
     *
     * @param product
     * @return
     */
    List<Product> queryByProduct(Product product);

    /**
     * 保存数据
     * @param product
     * @return
     */
    String save(Product product);

    /**
     * 查询套餐名称
     *
     * @return
     */
    List<String> getProductNames(String name);
}
