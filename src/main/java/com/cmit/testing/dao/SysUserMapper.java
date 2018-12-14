package com.cmit.testing.dao;

import com.cmit.testing.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 *
 * @author YangWanLi
 * @date 2018/7/5 13:27
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser>{
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 通过账号或用户名称和ID获取用户
     * @param account
     * @return
     */
    List<SysUser> getSysUserByAccountOrNameAndId(@Param("account") String account, @Param("name") String name, @Param("id") Integer id);

    /**
     * 分页
     * @param userName 用户名
     * @param roleName 角色名
     * @return
     */
    List<SysUser> findByPage(@Param("userName") String userName, @Param("roleName") String roleName);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);


    /**
     * 解锁
     * @param userIds
     */
    void unlock(@Param("userIds") List<Integer> userIds);

    /**
     * 根据角色id查询用户
     * @param roleIds
     * @return
     */
    List<SysUser> getSysUserByRoleIds(@Param("roleIds") List<Integer> roleIds);
}