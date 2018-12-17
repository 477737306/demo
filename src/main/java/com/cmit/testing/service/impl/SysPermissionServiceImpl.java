package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysPermissionMapper;
import com.cmit.testing.dao.SysRoleMapper;
import com.cmit.testing.dao.SysUserMapper;
import com.cmit.testing.entity.PermissionOfRole;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.SysRoleService;
import com.cmit.testing.utils.JsonResultUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {

        return permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysPermission record) {
        return permissionMapper.insert(record);
    }

    @Override
    public int insertSelective(SysPermission record) {
        return permissionMapper.insertSelective(record);
    }

    @Override
    public SysPermission selectByPrimaryKey(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysPermission record) {
        return permissionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysPermission record) {
        return permissionMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysPermission> getPermissions(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysPermission> list = permissionMapper.getAllPermission(null);
        return list;
    }

    @Override
    public JsonResultUtil updatePermissionOfRole(Integer role_id, String pids) {
        String [] permissions=pids.split(",");
        List<PermissionOfRole> list = new ArrayList<>();
        for (String pid:permissions) {
            PermissionOfRole p = new PermissionOfRole(role_id,Integer.parseInt(pid));
            list.add(p);
        }
        try {
            permissionMapper.deletePermissionsOfRole(role_id);
            permissionMapper.insertPermissionsOfRole(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResultUtil(300,"绑定权限失败");
        }

        //刷新权限
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(role_id);
        List<SysUser> userList = sysUserMapper.getSysUserByRoleIds(roleIds);
        for (SysUser sysUser : userList) {
            ShiroKit.reloadAuthorizing(sysUser.getAccount());
        }
        return new JsonResultUtil(200,"绑定成功");

    }

    @Override
    public List<SysPermission> getPermissionByRole(Integer role_id) {
        return permissionMapper.getPermissionByRole(role_id);
    }

    @Override
    public SysPermission getPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysPermission> getTreeMenu(Integer parentId,List<String> noType) {
        ShiroUser shiroUser = getShiroUser();
        List<SysPermission> sysPermissions = new ArrayList<>();
        if("admin".equals(shiroUser.getAccount())) {
            sysPermissions = permissionMapper.getAllPermission(noType);
        }else {
            sysPermissions = permissionMapper.getPermissionByUserId(shiroUser.getId(),noType);
        }
        List<SysPermission> sysPermissionList = new ArrayList<>();
        getTree(sysPermissionList,parentId,sysPermissions);
        return sysPermissionList;
    }

    @Override
    public List<SysPermission> getAllTree(Integer parentId,List<String> noType) {
        List<SysPermission> sysPermissions = permissionMapper.getAllPermission(noType);
        List<SysPermission> sysPermissionList = new ArrayList<>();
        getTree(sysPermissionList,parentId,sysPermissions);
        return sysPermissionList;
    }

    @Override
    public List<SysPermission> duplicateCheck(Integer parentId, Integer noDataId, String type, String name) {
        return permissionMapper.duplicateCheck(parentId,noDataId,type,name);
    }


    @Override
    public List<SysPermission> getPermissionByUserId() {
        ShiroUser shiroUser = ShiroKit.getUser();
        List<SysPermission> sysPermissions = new ArrayList<>();
        if("admin".equals(shiroUser.getAccount())){//admin获取所有菜单
            sysPermissions =  permissionMapper.getAllPermission(null);
        }else {
            sysPermissions = permissionMapper.getPermissionByUserId(shiroUser.getId(),null);
        }
        return sysPermissions;
    }


    @Override
    public List<SysPermission> loginPermission() {
        ShiroUser shiroUser = ShiroKit.getUser();
        List<SysPermission> sysPermissions = new ArrayList<>();
        if("admin".equals(shiroUser.getAccount())){//admin获取所有菜单
            sysPermissions =  permissionMapper.getAllPermission(null);
        }else {
            sysPermissions = permissionMapper.getPermissionByUserId(shiroUser.getId(),null);
        }
        List<SysPermission> ownerPermissions = new ArrayList<>();

        getTree(ownerPermissions,0, sysPermissions);
        return ownerPermissions;
    }

    @Override
    public int deletePermissionsOfRoleByPermissionIds(List<Integer> permissionIds) {
        return permissionMapper.deletePermissionsOfRoleByPermissionIds(permissionIds);
    }

    /**
     * 获取树形菜单
     * @param
     * @param sysPermissions
     */
    public void getTree(List<SysPermission> menus,int parentId,List<SysPermission> sysPermissions){
        for (SysPermission sysPermission1 : sysPermissions) {
            if (parentId== sysPermission1.getParentId()){
                getTree(sysPermission1.getPermissions(),sysPermission1.getId(),sysPermissions);
                menus.add(sysPermission1);
            }
        }
    }

    /**
     * 保存项目模块的菜单以及测试角色的权限
     * @param sysPermission
     */
    @Override
    public int saveProjectPermission(SysPermission sysPermission){
        Map<String,String> map = new HashMap<>();
        map.put("project","/security/api/v1/project");
        map.put("business","/security/api/v1/business");
        map.put("case","/security/api/v1/testcase,/security/api/v1/scriptstep");
        map.put("scripts","/security/api/v1/modelScript");
        map.put("appCase","/security/api/v1/app/case");
        map.put("appScript","/security/api/v1/app/script");
        sysPermission.setServerUrl(map.get(sysPermission.getType()));
        sysPermission.setStatus(1);
        sysPermission.setNameCopyNum(0);
        insertSelective(sysPermission);
        List<PermissionOfRole> permissionOfRoles = new ArrayList<>();
        List<Integer> roleIds = sysRoleMapper.getRoleIdByPermissionId(sysPermission.getParentId());
        if(roleIds!=null && roleIds.size()>0)
        for (Integer roleId : roleIds) {
            PermissionOfRole permissionOfRole = new PermissionOfRole(roleId,sysPermission.getId());
            permissionOfRoles.add(permissionOfRole);
        }
        permissionMapper.insertPermissionsOfRole(permissionOfRoles);
        return sysPermission.getId();
    }

    @Override
    public List<SysPermission> getSysPermissionsByParentIds(List<Integer> parentIds) {
        List<SysPermission> sysPermissionList = new ArrayList<>();
        if(parentIds.size()>0){
            sysPermissionList =  permissionMapper.getSysPermissionsByParentIds(parentIds);
        }
        return sysPermissionList;
    }

    @Override
    public List<SysPermission> getPermissionsByIds(List<Integer> ids) {
        return permissionMapper.getPermissionsByIds(ids);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return permissionMapper.deleteByIds(ids);
    }

    /**
     * 根据type查询数据
     * @param type
     * @return
     */
    @Override
    public List<SysPermission> getByType(String type) {
        return permissionMapper.getByType(type);
    }

    /**
     * 根据父级id递归获取app和web用例的id
     * @param parentId
     * @param sysPermissions
     * @param webIds
     * @param appIds
     */
    public void getCaseIds(Integer parentId,List<SysPermission> sysPermissions,List<Integer> webIds,List<Integer> appIds){
        for (SysPermission sysPermission : sysPermissions) {
            if(parentId.equals(sysPermission.getParentId())) {
                if ("case".equals(sysPermission.getType())) {
                    webIds.add(sysPermission.getDataId());
                }
                if ("appCase".equals(sysPermission.getType())) {
                    appIds.add(sysPermission.getDataId());
                }
                getCaseIds(sysPermission.getId(),sysPermissions,webIds,appIds);
            }
        }

    }

    @Override
    public SysPermission getPermissionsBydataIdAndType(Integer dataId,String type){
        return permissionMapper.getPermissionsBydataIdAndType(dataId,type);
    }
}
