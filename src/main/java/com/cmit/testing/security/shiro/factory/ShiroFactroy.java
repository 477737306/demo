package com.cmit.testing.security.shiro.factory;

import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.SysRole;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.SysRoleService;
import com.cmit.testing.service.SysUserService;
import com.cmit.testing.utils.SpringContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义shirorealm所需数据
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {

    //如果项目中用到了事物，@Autowired注解会使事物失效，可以自己用get方法获取值
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    public static IShiro me() {
        return SpringContextHolder.getBean(IShiro.class);
    }

    @Override
    public SysUser user(String account) {

        SysUser user = sysUserService.getSysUserByAccount(account);

        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
//        // 账号被冻结
//        if (user.getStatus() != ManagerStatus.OK.getCode()) {
//            throw new LockedAccountException();
//        }
        return user;
    }


    @Override
    public ShiroUser shiroUser(SysUser user) {
        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(user.getId());
        shiroUser.setAccount(user.getAccount());
        shiroUser.setName(user.getName());
        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Integer roleId) {
        List<String> permissions = new ArrayList<>();
        List<SysPermission> sysPermissions = sysPermissionService.getPermissionByRole(roleId);
        for(SysPermission sysPermission : sysPermissions){
            permissions.add(sysPermission.getUrl());
        }
        return permissions;
    }

    @Override
    public List<SysPermission> getPermissionByUserId() {
        List<SysPermission> sysPermissions = sysPermissionService.getPermissionByUserId();
        return sysPermissions;
    }

    @Override
    public List<SysRole> getSysRolesByUserId(Integer userId){
        return sysRoleService.getSysRolesByUserId(userId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUser user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
