package com.cmit.testing.service;

import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.page.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业务Service
 *
 * @author YangWanLi
 * @date 2018/7/25 10:15
 */
public interface BusinessService extends BaseService<Business>{

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    int saveBusiness(Map<String, Object> map, Integer id);

    boolean copy(List<SysPermission> list, int menuParentId);

    /**
     * shearBusiness
     * @param list  剪切的菜单list
     * @param menuParentId 剪切到目标对象的id
     */
    boolean shear(List<SysPermission> list, int menuParentId);

    /**
     * 根据projectid查询该项目下有哪些业务
     */
    List<Business> getBusinessListByProjectId(int projectId);

    /**
     * 分页
     * @param name 用户名
     * @param projectId
     * @return
     */
    List<PbtVO> findByPage(String name, Integer projectId);

    /**
     *  业务分页
     * @param pageBean
     * @param business
     * @return
     */
    PageBean<PbtVO> findPage(PageBean<Object> pageBean, Business business);

    /**
     * 统计项目下业务的成功数与失败数
     * @param projectId
     * @return
     */
    Map<String,Long> businessCountNumber(Integer projectId);

    /**
     * 根据业务名获得项目名
     */
    Map<String,String> getProjectNameByBusinessName(String name);

    /**
     * 获取项目下正在执行的用例数
     * @param projectId
     * @return
     */
    Integer getExecutingCountByProjectId(@Param("projectId") Integer projectId);
}
