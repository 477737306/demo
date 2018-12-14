package com.cmit.testing.service;

import com.cmit.testing.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Service
 *
 * @author YangWanLi
 * @date 2018/7/9 17:31
 */
public interface SysUserRoleService extends BaseService<SysUserRole>{

    int delete(Integer id);

    int insert(SysUserRole record);

    SysUserRole get(Integer id);

    /**
     * 根据用户userIds删除
     * @param userIds
     * @return
     */
    int deleteByUserIds(List<Integer> userIds);

    /**
     * 根据用户roleId删除
     * @param roleId
     * @return
     */
    int deleteByRoleId(Integer roleId);

    /**
     * 根据用户userId获取List
     * @param userId
     * @return
     */
    List<SysUserRole> getByUserId(Integer userId);

    /**
     * 根据用户roleIds获取List
     * @param roleIds
     * @return
     */
    List<SysUserRole>  getByRoleIds(List<Integer> roleIds);

    /**
     * 批量插入数据
     * @param sysUserRoleList
     * @return
     */
    int insertSysUserRoleList(List<SysUserRole> sysUserRoleList);
}
