package com.cmit.testing.service.impl;

import com.cmit.testing.dao.ProductMapper;
import com.cmit.testing.entity.Product;
import com.cmit.testing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/20
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> queryByProduct(Product product) {
        return productMapper.queryByProduct(product);
    }

    @Override
    public String save(Product record) {
        //如果存在不插入直接返回id
        List<Product> productList = productMapper.queryByProduct(record);
        if (productList.size() > 0) {
            return productList.get(0).getId();
        }
        record.setId(new JdkIdGenerator().generateId().toString().replace("-", ""));
        int i = productMapper.insert(record);
        if (i == 0) {
            return null;
        }
        return record.getId();
    }

    /**
     * 查询套餐名称
     *
     * @return
     */
    @Override
    public List<String> getProductNames(String name){
        return productMapper.getProductNames(name);
    }
}
