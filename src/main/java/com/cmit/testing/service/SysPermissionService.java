package com.cmit.testing.service;

import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.utils.JsonResultUtil;

import java.util.List;

public interface SysPermissionService extends BaseService<SysPermission>{

    List<SysPermission> getPermissions(int pageNum, int pageSize);

    JsonResultUtil updatePermissionOfRole(Integer role_id, String pids);

    List<SysPermission> getPermissionByRole(Integer role_id);

    SysPermission getPermissionById(Integer id);


    /**
     * 获取树形菜单（是根据当前登录用户）
     * @param parentId 父级id
     * @param noType 不查询指定类型菜单
     * @return
     */
    List<SysPermission> getTreeMenu(Integer parentId, List<String> noType);

    /**
     * 根据用户获取权限
     * @return
     */
    List<SysPermission> getPermissionByUserId();

    /**
     * 获取登录用户菜单权限
     * @return
     */
    List<SysPermission> loginPermission();


    /**
     * 根据菜单的Id删除与角色的关联关系
     * @param permissionIds
     * @return
     */
    int deletePermissionsOfRoleByPermissionIds(List<Integer> permissionIds);

    /**
     * 保存项目模块的菜单以及测试角色的权限
     * @param sysPermission
     */
    int saveProjectPermission(SysPermission sysPermission);

    /**
     *  根据ParentId获取对象集合
     * @param parentIds
     * @return
     */
    List<SysPermission> getSysPermissionsByParentIds(List<Integer> parentIds);

    /**
     *  根据ids获取对象集合
     * @param ids
     * @return
     */
    List<SysPermission> getPermissionsByIds(List<Integer> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 根据type查询数据
     * @param type
     * @return
     */
    List<SysPermission> getByType(String type);

    /**
     * 获取属性菜单
     * @param parentId
     * @param noType
     * @return
     */
    List<SysPermission> getAllTree(Integer parentId, List<String> noType);

    /**
     * 查重
     * @param parentId 父级菜单
     * @param noDataId 去除数据id之外
     * @param type 类型
     * @param name 菜单名称
     * @return
     */
    List<SysPermission> duplicateCheck(Integer parentId, Integer noDataId, String type, String name);

    /**
     * 根据父级id递归获取app和web用例的id
     * @param parentId
     * @param sysPermissions
     * @param webIds
     * @param appIds
     */
    void getCaseIds(Integer parentId, List<SysPermission> sysPermissions, List<Integer> webIds, List<Integer> appIds);

    /**
     * 根据数据id查询菜单
     * @param dataId
     * @return
     */
    SysPermission getPermissionsBydataIdAndType(Integer dataId, String type);
}
