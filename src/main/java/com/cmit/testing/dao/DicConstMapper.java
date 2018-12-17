package com.cmit.testing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cmit.testing.entity.DicConst;
import com.cmit.testing.entity.SysUser;

@Mapper
public interface DicConstMapper extends BaseMapper<DicConst>{
	
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);
    
    /**
     * 添加字典
     * @param record
     * @return
     */
    int insert(DicConst record);
    
    /**
     * 根据条件查询
     * @param record
     * @return
     */
    int insertSelective(DicConst record);
    
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    DicConst selectByPrimaryKey(Integer id);

    /**
     * 根据主键动态修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(DicConst record);
    
    /**
     * 修改字典
     * @param record
     * @return
     */
    int updateByPrimaryKey(DicConst record);
    
    /**
     * 字典根据主键批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);
    
    /**
     * 根据条件查询字典 
     * @param dicConst
     * @return List<DicConst>
     */
    List<DicConst> findDicConstByParam(DicConst dicConst);
    
    /**
     * 分页查询
     * @param dvalue
     * @param dname
     * @param dtype
     * @param dparentCode
     * @param status
     * @param remark
     * @return
     */
    List<DicConst> findByPage(@Param("dvalue") String dvalue, @Param("dname") String dname, @Param("dtype") String dtype, @Param("dparentCode") String dparentCode, @Param("status") Integer status
            , @Param("remark") String remark);
}