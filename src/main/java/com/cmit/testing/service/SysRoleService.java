package com.cmit.testing.service;

import com.cmit.testing.entity.SysRole;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * 角色Service
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
public interface SysRoleService extends BaseService<SysRole>{
    int delete(Integer id);

    SysRole get(Integer id);

    SysRole insert(Map<String, String> map);

    SysRole update(Integer id, Map<String, String> map);

    /**
     * 分页
     * @param currentPage
     * @param pageSize
     * @param roleName 角色名称
     * @return
     */
    PageBean<SysRole> findPage(int currentPage, int pageSize, String roleName);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 根据用户Id获取角色集合
     * @param userId
     * @return
     */
    List<SysRole> getSysRolesByUserId(Integer userId);

    /**
     * 获取角色集合
     * @return
     */
     List<SysRole> findAll();

    /**
     * 根据角色名获取角色名
     * @param name
     * @return
     */
     SysRole getByRoleName(String name);
}
