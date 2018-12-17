package com.cmit.testing.dao;

import com.cmit.testing.entity.Phones;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhonesMapper extends BaseMapper<Phones>{
    int deleteByPrimaryKey(Integer id);

    int insert(Phones record);

    int insertSelective(Phones record);

    Phones selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Phones record);

    int updateByPrimaryKey(Phones record);

    List<Phones> selectAll();

    /**
     * 根据手机Id批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 分页
     * @return
     */
    List<Phones> findByPage();
}