package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysUserRoleMapper;
import com.cmit.testing.entity.SysUserRole;
import com.cmit.testing.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色ServiceImpl
 *
 * @author YangWanLi
 * @date 2018/7/9 17:32
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int delete(Integer id) {
        return sysUserRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysUserRole record) {
        return sysUserRoleMapper.insert(record);
    }

    @Override
    public SysUserRole get(Integer id) {
        return sysUserRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByUserIds(List<Integer> userIds) {
        return sysUserRoleMapper.deleteByUserIds(userIds);
    }

    @Override
    public int deleteByRoleId(Integer roleId) {
        return sysUserRoleMapper.deleteByRoleId(roleId);
    }

    @Override
    public List<SysUserRole> getByUserId(Integer userId) {
        return sysUserRoleMapper.getByUserId(userId);
    }

    @Override
    public List<SysUserRole> getByRoleIds(List<Integer> roleIds) {
        return sysUserRoleMapper.getByRoleIds(roleIds);
    }

    @Override
    public int insertSysUserRoleList(List<SysUserRole> sysUserRoleList) {
        return sysUserRoleMapper.insertSysUserRoleList(sysUserRoleList);
    }


}
