package com.cmit.testing.dao;

import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.vo.PbtVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BusinessMapper extends BaseMapper<Business>{

    /**
     * 分页
     * @param business
     * @return
     */
    List<PbtVO> findByPage(Business business);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据projectid查询该项目下有哪些业务
     */
    List<Business> getBusinessListByProjectId(@Param("projectId") int projectId);

    /**
     * 业务数据统计
     * @return   业务数据统计（casenum：业务总数，noexecute：未执行，executing：执行中，executed：已执行
     */
    Map<String,Long> businessCount();

    /**
     * 统计项目下业务的成功数与失败数
     * @param projectId
     * @return
     */
    Map<String,Long> businessCountNumber(@Param("projectId") Integer projectId);

    Map<String,String> getProjectNameByBusinessName(@Param("businessName") String name);

    /**
     * 获取项目下正在执行的用例数
     * @param projectId
     * @return
     */
    Integer getExecutingCountByProjectId(@Param("projectId") Integer projectId);
}