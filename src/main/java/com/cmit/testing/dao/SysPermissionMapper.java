package com.cmit.testing.dao;

import com.cmit.testing.entity.PermissionOfRole;
import com.cmit.testing.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission>{
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    /**
     * 根据type查询数据
     * @param type
     * @return
     */
    List<SysPermission> getByType(@Param("type") String type);


    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> getAllPermission(@Param("noType") List<String> noType);

    int deletePermissionsOfRole(Integer roleId);

    /**
     * 添加角色与菜单的关联关系
     * @param list
     * @return
     */
    int insertPermissionsOfRole(List<PermissionOfRole> list);

    List<SysPermission> getPermissionByRole(Integer role_id);

    /**
     *  根据用户id获取菜单权限
     * @param userId
     * @return
     */
    List<SysPermission> getPermissionByUserId(@Param("userId") Integer userId, @Param("noType") List<String> noType);

    /**
     * 根据菜单的Id删除与角色的关联关系
     * @param permissionIds
     * @return
     */
    int deletePermissionsOfRoleByPermissionIds(@Param("permissionIds") List<Integer> permissionIds);

    /**
     *  根据ParentIds获取对象集合
     * @param parentIds
     * @return
     */
    List<SysPermission> getSysPermissionsByParentIds(@Param("parentIds") List<Integer> parentIds);

    /**
     *  根据ids获取对象集合
     * @param ids
     * @return
     */
    List<SysPermission> getPermissionsByIds(@Param("ids") List<Integer> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);


    SysPermission getPermissionsBydataIdAndType(@Param("dataId") Integer dataId, @Param("type") String type);

    /**
     * 查重
     * @param parentId 父级菜单
     * @param noDataId 去除数据id之外
     * @param type 类型
     * @param name 菜单名称
     * @return
     */
    List<SysPermission> duplicateCheck(@Param("parentId") Integer parentId, @Param("noDataId") Integer noDataId, @Param("type") String type, @Param("name") String name);
}