package com.cmit.testing.security.shiro.factory;

import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.SysRole;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.security.shiro.ShiroUser;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import java.util.List;

/**
 * 定义shirorealm所需数据的接口
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */
public interface IShiro {

    /**
     * 根据账号获取登录用户
     *
     * @param account 账号
     */
    SysUser user(String account);

    /**
     * 根据系统用户获取Shiro的用户
     *
     * @param user 系统用户
     */
    ShiroUser shiroUser(SysUser user);

        /**
    //     * 获取权限列表通过角色id
    //     *
    //     * @param roleId 角色id
    //     */
    List<String> findPermissionsByRoleId(Integer roleId);

    /**
     * 获取shiro的认证信息
     */
    SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUser user, String realmName);

    /**
     * 根据用户Id获取角色集合
     * @param userId
     * @return
     */
    List<SysRole> getSysRolesByUserId(Integer userId);

    /**
     * 根据用户Id获取菜单资源
     * @return
     */
    List<SysPermission> getPermissionByUserId();
}
