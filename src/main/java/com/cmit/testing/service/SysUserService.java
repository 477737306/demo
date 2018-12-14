package com.cmit.testing.service;

import com.cmit.testing.entity.SysUser;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * 用户Service
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
public interface SysUserService extends BaseService<SysUser>{

    int delete(Integer id);

    SysUser getSysUserByAccount(String account);

    SysUser get(Integer id);

    SysUser insert(Map<String, Object> map);

    SysUser updatePwd(Integer id, Map<String, String> map);

    SysUser updateSysUser(Integer id, Map<String, Object> map);

    /**
     * 分页
     * @param currentPage
     * @param pageSize
     * @param userName
     * @param roleName
     * @return
     */
    PageBean<SysUser> findPage(int currentPage, int pageSize, String userName, String roleName);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    void addRoles(Integer userId, String[] roleIds);

    /**
     * 解锁
     * @param userIds
     * @return
     */
    void unlock(List<Integer> userIds);

    /**
     * 重置密码
     * @param id
     * @return
     */
    SysUser resetPassword(Integer id);
}
