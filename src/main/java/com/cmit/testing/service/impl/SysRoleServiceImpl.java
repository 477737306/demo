package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysPermissionMapper;
import com.cmit.testing.dao.SysRoleMapper;
import com.cmit.testing.entity.SysRole;
import com.cmit.testing.entity.SysUserRole;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.SysRoleService;
import com.cmit.testing.service.SysUserRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色ServiceImpl
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public int delete(Integer id) {
        return sysRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SysRole get(Integer id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleMapper.findAll();
    }

    @Override
    public SysRole getByRoleName(String name) {
        return sysRoleMapper.getByRoleName(name);
    }

    @Override
    public SysRole insert(Map<String, String> map) {
        String createBy = ShiroKit.getUser().getName();
        String name = map.get("name");
        List<SysRole> sysRoleList = sysRoleMapper.getByNameExcId(name,null);
        if(null != sysRoleList && sysRoleList.size()>0){
            throw new TestSystemException(300+"@角色名已存在");
        }
        SysRole sysRole = new SysRole();
        sysRole.setName(name);
        sysRole.setCreateBy(createBy);
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateBy(createBy);
        sysRoleMapper.insert(sysRole);
        return sysRole;
    }


    @Override
    public SysRole update(Integer id, Map<String, String> map) {
        String createBy = ShiroKit.getUser().getName();
        String name = map.get("name");
        List<SysRole> sysRoleList = sysRoleMapper.getByNameExcId(name,id);
        if(null != sysRoleList && sysRoleList.size()>0){
            throw new TestSystemException(300+"@角色名已存在");
        }
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        sysRole.setName(name);
        sysRole.setUpdateTime(new Date());
        sysRole.setUpdateBy(createBy);
        sysRoleMapper.updateByPrimaryKey(sysRole);
        return sysRole;
    }

    @Override
    public PageBean<SysRole> findPage(int currentPage, int pageSize,String roleName) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<SysRole> sysRoles = sysRoleMapper.findByPage(roleName);
        PageBean<SysRole> page_   = new PageBean<>(currentPage,pageSize,(int)page.getTotal());
        page_.setItems(sysRoles);
        return page_;    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        List<SysUserRole> sysUserRoleList = sysUserRoleService.getByRoleIds(ids);
        if(null!= sysUserRoleList&& sysUserRoleList.size()>0){
            throw new TestSystemException(300+"@角色已被其他用户关联，不能删除");
        }
        //删除角色和菜单关联
        for (Integer roloeId : ids) {
            sysPermissionMapper.deletePermissionsOfRole(roloeId);
        }
        return sysRoleMapper.deleteByIds(ids);
    }

    @Override
    public List<SysRole> getSysRolesByUserId(Integer userId) {
        return sysRoleMapper.getSysRolesByUserId(userId);
    }
}
