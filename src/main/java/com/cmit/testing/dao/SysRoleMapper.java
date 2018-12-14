package com.cmit.testing.dao;

import com.cmit.testing.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole>{
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    List<SysRole> getByNameExcId(@Param("name") String name, @Param("id") Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 分页
     * @return
     */
    List<SysRole> findByPage(@Param("roleName") String roleName);

    /**
     * 批量删除
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
     * 获取所有集合
     * @return
     */
    List<SysRole>  findAll();

    /**
     * 根据角色名获取角色名
     * @param name
     * @return
     */
    SysRole getByRoleName(@Param("name") String name);

    /**
     * 根据菜单id获取角色id
     * @param permissionId
     * @return
     */
    List<Integer> getRoleIdByPermissionId(@Param("permissionId") Integer permissionId);
}