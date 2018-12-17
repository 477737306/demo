package com.cmit.testing.dao;

import com.cmit.testing.entity.Product;
import com.cmit.testing.entity.Project;
import com.cmit.testing.entity.vo.PbtVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author weiBin
 */
@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(String id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 根据套餐信息查询
     *
     * @param product
     * @return
     */
    List<Product> queryByProduct(Product product);

    /**
     * 查询套餐名称
     *
     * @return
     */
    List<String> getProductNames(@Param("name") String name);

}