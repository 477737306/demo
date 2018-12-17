package com.cmit.testing.service;

import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.page.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author YangWanLi
 * @date 2018/7/30 10:43
 */
public interface ProjectMenuService {

    /**
     * 通过菜单Id删除菜单目录和数据
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 分页
     *
     * @param currentPage
     * @param pageSize
     * @param name
     * @return
     */
    PageBean<PbtVO> findPage(int currentPage, int pageSize, String name, String tableFlag, Integer parentId);


    /**
     * 保存
     * @param map
     */
    int save(Map<String, Object> map);

    /**
     * 重命名
     * @param id
     * @return
     */
    int updateNameById(Integer id, String name);

    /**
     * 用例执行完成后更改数据
     * @param businessId 业务id
     */
    void exeAfterUpdateData(Integer businessId);

    /**
     * 用例执行前更改数据
     * @param businessId 业务id
     */
    void exeBeforeUpdateData(Integer businessId);

    /**
     * 检测用例于批量任务或众测任务是否关联
     * @param id
     * @return
     */
    boolean checkCorrelation(Integer id);

    /**
     * 修改归档状态
     * @param id
     * @param type
     */
    void updateStatus(Integer id, String type);

    /**
     * 检测是否归档
     * @param id 数据id
     * @return true 未归档 false 归档
     */
    boolean checkStatus(Integer id);
}
