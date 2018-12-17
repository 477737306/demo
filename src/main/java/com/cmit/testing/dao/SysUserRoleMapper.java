package com.cmit.testing.dao;

import com.cmit.testing.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Mapper
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole>{
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 根据用户userId删除
     * @param userIds
     * @return
     */
    int deleteByUserIds(@Param("userIds") List<Integer> userIds);

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
    List<SysUserRole>  getByRoleIds(@Param("roleIds") List<Integer> roleIds);

    /**
     * 批量插入数据
     * @param sysUserRoleList
     * @return
     */
    int insertSysUserRoleList(List<SysUserRole> sysUserRoleList);
}