package com.cmit.testing.service;

import com.cmit.testing.entity.DicConst;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;


public interface DicConstService extends BaseService<DicConst>{
	
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 新增字典
	 * @param map
	 * @return
	 */
	int insert(Map<String, String> map);

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
     * 根据主键全部修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(DicConst record);
    
    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);
    
    /**
     * 根据条件查询
     * @param dicConst
     * @return
     */
    List<DicConst> findDicConstByParam(DicConst dicConst);
    
    /**
     * 分页
     * @param currentPage
     * @param pageSize
     * @param dvalue
     * @param dname
     * @param dtype
     * @param dparentCode
     * @param status
     * @param remark
     * @return
     */
    PageBean<DicConst> findByPage(int currentPage, int pageSize, String dvalue, String dname, String dtype, String dparentCode, Integer status, String remark);
	
}
