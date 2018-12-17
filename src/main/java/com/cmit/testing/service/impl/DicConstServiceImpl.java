package com.cmit.testing.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmit.testing.dao.DicConstMapper;
import com.cmit.testing.entity.DicConst;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.DicConstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

@Service
public class DicConstServiceImpl extends BaseServiceImpl<DicConst> implements DicConstService {

	@Autowired
	private DicConstMapper constMapper;
	
	/**
	 * 根据主键删除
	 * 
	 */
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return constMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 字典新增
	 */
	@Override
	public int insert(Map<String,String> map) {
		DicConst dc = new DicConst();
		dc.setDname(map.get("dname"));
		dc.setDvalue(map.get("dvalue"));
		dc.setDparentCode(map.get("dparentCode"));
		dc.setStatus(Integer.parseInt(map.get("status")));
		dc.setRemark(map.get("remark"));
		dc.setDtype(map.get("dtype"));
		return constMapper.insert(dc);
	}
	
	/**
	 * 根据条件查询
	 */
	@Override
	public int insertSelective(DicConst record) {
		return constMapper.insertSelective(record);
	}

	/**
	 * 根据主键查询
	 */
	@Override
	public DicConst selectByPrimaryKey(Integer id) {
		return constMapper.selectByPrimaryKey(id);
	}

	/**
	 * 根据主键动态修改
	 */
	@Override
	public int updateByPrimaryKeySelective(DicConst record) {
		return constMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 修改字典
	 */
	@Override
	public int updateByPrimaryKey(DicConst record) {
		return constMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 字典根据主键批量删除
	 */
	@Override
	public int deleteByIds(List<Integer> ids) {
		return constMapper.deleteByIds(ids);
	}
	
	/**
	 * 根据条件查询字典
	 */
	@Override
	public List<DicConst> findDicConstByParam(DicConst dicConst) {
		return constMapper.findDicConstByParam(dicConst);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public PageBean<DicConst> findByPage(int currentPage, int pageSize,String dvalue, String dname, String dtype, String dparentCode, Integer status,
			String remark) {
		Page<Object> page = PageHelper.startPage(currentPage, pageSize);
		List<DicConst> dicConst = constMapper.findByPage(dvalue, dname, dtype, dparentCode, status, remark);
		PageBean<DicConst> page_ = new PageBean<>(currentPage,pageSize,(int)page.getTotal());
		page_.setItems(dicConst);
		return page_;
	}

}
