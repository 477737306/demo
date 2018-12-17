package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysUserMapper;
import com.cmit.testing.entity.SysUser;
import com.cmit.testing.entity.SysUserRole;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.security.shiro.MySessionManager;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysUserRoleService;
import com.cmit.testing.service.SysUserService;
import com.cmit.testing.utils.DESUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 用户ServiceImpl
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
@Service(value = "sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    private static final String  SKEY    = "abcdefgh";//加密key
    private static final Charset CHARSET = Charset.forName("gb2312");//加密编码

    @Override
    public SysUser getSysUserByAccount(String account) {
       List<SysUser> sysUserList = sysUserMapper.getSysUserByAccountOrNameAndId(account,null,null);

       return CollectionUtils.isNotEmpty(sysUserList) ? sysUserList.get(0) : null;
    }

    @Override
    @Transactional public SysUser insert(Map<String,Object> map) {
        ShiroUser shiroUser = getShiroUser();
        String cretateBy = shiroUser.getName();

        String account = map.get("account").toString();
        String name = map.get("name").toString();
        String password = "Acd!2018";
        //秘钥
        String secretKey = DESUtil.encrypt(account+"@"+password, CHARSET, SKEY);
        String phone = map.get("phone").toString();
        String email = map.get("email").toString();
        String[] roleIds = map.get("roleIds").toString().split(",");
        if(StringUtils.isEmpty(account)||StringUtils.isEmpty(name)||StringUtils.isEmpty(password)||roleIds.length<1){
            throw new TestSystemException(300+"@参数为空");
        }
        if(!checkPassword(password)){
            throw new TestSystemException(300+"@密码格式有误,数字+大小写字母+特殊符号+不允许使用连续3位以上的字符(或键盘上的连续字符),8-20位");
        }
        List<SysUser> sysUserList = sysUserMapper.getSysUserByAccountOrNameAndId(account,name,null);
        if(null != sysUserList && sysUserList.size()>0){
            throw new TestSystemException(300+"@账号已存在");
        }
        SysUser sysUser = new SysUser();
        sysUser.setPhone(phone);
        sysUser.setEmail(email);
        sysUser.setName(name);
        sysUser.setStatus(0);
        sysUser.setAccount(account);
        sysUser.setSalt(ShiroKit.getRandomSalt(5));
        sysUser.setPassword(ShiroKit.md5(password, sysUser.getSalt()));
         sysUser.setSecretKey(secretKey);
        sysUser.setCreateBy(cretateBy);
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateBy(cretateBy);
        sysUser.setUpdateTime(new Date());
        sysUserMapper.insert(sysUser);
        //添加角色关联
        addRoles(sysUser.getId(),roleIds);
        return sysUser;
    }

    @Override
    public SysUser updatePwd(Integer id,Map<String,String> map){
        ShiroUser shiroUser = ShiroKit.getUser();
        String cretateBy = shiroUser.getName();
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        try {
            oldPassword = DESUtil.decryption(oldPassword);
            newPassword = DESUtil.decryption(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(StringUtils.isEmpty(oldPassword)||StringUtils.isEmpty(newPassword)){
            throw new TestSystemException(300+"@旧密码或新密码不能为空");
        }
        if(!checkPassword(newPassword)){
            throw new TestSystemException(300+"@新密码格式有误,数字+大小写字母+特殊符号+不允许使用连续3位以上的字符(或键盘上的连续字符),8-20位");
        }
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);

        oldPassword = ShiroKit.md5(oldPassword, sysUser.getSalt());
        if(oldPassword.equals(sysUser.getPassword())){
            sysUser.setId(sysUser.getId());
            sysUser.setPassword(ShiroKit.md5(newPassword, sysUser.getSalt()));
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateBy(cretateBy);
            sysUserMapper.updateByPrimaryKey(sysUser);
            return sysUser;
        }else {
            throw new TestSystemException(300+"@旧密码错误");

        }
    }

    @Override
    public void unlock(List<Integer> userIds){
        sysUserMapper.unlock(userIds);
    }

    @Override
    public SysUser resetPassword(Integer id){
        ShiroUser shiroUser = ShiroKit.getUser();
        String cretateBy = shiroUser.getName();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        sysUser.setPassword(ShiroKit.md5("Acd!2018", sysUser.getSalt()));
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateBy(cretateBy);
        sysUserMapper.updateByPrimaryKey(sysUser);
        return sysUser;

    }


    @Override
    public SysUser updateSysUser(Integer id,Map<String,Object> map){
        ShiroUser shiroUser = ShiroKit.getUser();
        String cretateBy = shiroUser.getName();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        String name = map.get("name").toString();

        String [] roleIds = map.get("roleIds").toString().split(",");
        List<SysUser> sysUserList = sysUserMapper.getSysUserByAccountOrNameAndId(null,name,id);
        if(null != sysUserList && sysUserList.size()>0){
            throw new TestSystemException(300+"@用户名已存在");
        }
        sysUser.setName(name);
        sysUser.setPhone(map.get("phone").toString());
        sysUser.setEmail(map.get("email").toString());
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateBy(cretateBy);
        sysUserMapper.updateByPrimaryKey(sysUser);
        //添加角色关联
        addRoles(sysUser.getId(),roleIds);
        return sysUser;
    }

    @Override
    public PageBean<SysUser> findPage(int currentPage, int pageSize, String userName, String roleName) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
        List<SysUser> sysUsers = sysUserMapper.findByPage(userName,roleName);
        PageBean<SysUser> page_   = new PageBean<>(currentPage,pageSize,(int)page.getTotal());
        page_.setItems(sysUsers);
        return page_;
    }

    /**
     * 密码正则表达式验证
     *
     * 必须包含数字、小写字母、大写字母、特殊字符、不允许使用连续3位以上的字符(或键盘上的连续字符)
     * @param password
     * @return
     */
    private boolean checkPassword(String password){
    	if(!com.cmit.testing.utils.StringUtils.isOrderKeyCode(password) && !com.cmit.testing.utils.StringUtils.isOrderChar(password.toLowerCase())) {
    		return Pattern.matches("^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9])).{8,}$", password);
    	}
    	return false;
    }

    @Override
    public SysUser get(Integer id){
       return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Integer id){
        //删除Session
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.deletSessionByAccount(sysUser.getAccount());
       return sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<Integer> ids){
        //删除Session
        for(Integer id : ids){
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
            MySessionManager mySessionManager = new MySessionManager();
            mySessionManager.deletSessionByAccount(sysUser.getAccount());
        }
        int a = sysUserMapper.deleteByIds(ids);
        sysUserRoleService.deleteByUserIds(ids);
        return a;
    }

    @Override
    public void addRoles(Integer userId, String [] roleIds) {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);
        //删除之前的关联关系
        sysUserRoleService.deleteByUserIds(userIds);

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        String createBy = ShiroKit.getUser().getName();
        for(String roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(Integer.parseInt(roleId));
            sysUserRole.setUserId(userId);
            sysUserRole.setCreateBy(createBy);
            sysUserRole.setCreateTime(new Date());
            sysUserRole.setUpdateBy(createBy);
            sysUserRole.setUpdateTime(new Date());
            sysUserRoleList.add(sysUserRole);
        }
        sysUserRoleService.insertSysUserRoleList(sysUserRoleList);
        //刷新权限
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        ShiroKit.reloadAuthorizing(user.getAccount());
    }

}
