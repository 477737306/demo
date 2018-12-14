package com.cmit.testing.service;

/**
 * 对外鉴权
 *
 * @author YangWanLi
 * @date 2018/9/12 20:03
 */
public interface ForeignPermissionService {

    /**
     * 获取对外鉴权token
     * @return
     */
    String getForeignToken(String key, String time, String token);

    /**
     * 权限校验
     * @param foreignToken
     * @return
     */
    Boolean checkForeignPermission(String foreignToken);
}
