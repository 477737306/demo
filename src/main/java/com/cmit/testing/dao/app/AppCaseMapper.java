package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseBO;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/28 0028 下午 12:40.
 */
@Mapper
public interface AppCaseMapper extends BaseMapper<AppCase> {

    List<Map<String, Object>> getAppCaseMapByCaseIds(@Param("caseIdList") List<Integer> caseIdList);

    /**
     * 判断当前项目下用例编号是否重复（除当前用例本身外）
     * @param projectId 项目ID
     * @param caseNum   用例编号
     * @param caseId    用例ID
     * @return
     */
    List<Integer> checkCaseNumUnique(@Param("projectId") Integer projectId, @Param("caseNum") String caseNum, @Param("caseId") Integer caseId);

    int updateByPrimaryKeySelective(AppCase appCase);
    int updateByAppCaseId(AppCase appCase);
    int insert(AppCase appCase);
    int deleteByPrimaryKey(Integer caseId);

    int insertSelective(AppCase appCase);

    Integer getAppIdByCaseId(@Param("caseId") Integer caseId);

    List<Integer> getIdsByFollowIds(@Param("followIdList") List<Integer> followIdList);

    List<Integer> getIdsByOldCaseIds(@Param("oldCaseIdList") List<Integer> oldCaseIdList);

    /**
     * 根据原用例ID查询被众测关联或批量任务关联的副本ID
     * @param oldCaseIdList
     * @param hasTaskId 值: null, 或 非空
     * @return
     */
    List<AppCase> getCaseByOldCaseIds(@Param("oldCaseIdList") List<Integer> oldCaseIdList, @Param("hasTaskId") String hasTaskId);

    List<AppCase> getCaseListByIds(@Param("caseIdList") List<Integer> caseIdList);

    /**
     * 根据省份获取最新的轮次号
     * @param oldCaseId 原用例ID
     * @return
     */
    Map<String, Integer> getMaxBatchNumber(@Param("caseId") Integer oldCaseId, @Param("province") String province);

    /**
     * 获取最新批次的轮次信息
     * @param caseId
     * @return
     */
    Integer getMaxExecuteCount(Integer caseId);

    Integer getNewCaseIdByOldCaseId(@Param("oldCaseId") Integer oldCaseId, @Param("executeCount") Integer executeCout);

    Integer getMaxNewCaseIdByOldCaseId(@Param("oldCaseId") Integer oldCaseId);

    /**
     * 批量删除App用例
     * @param ids
     * @return
     */
    Integer deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 添加App用例
     * @param ac
     * @return
     */
    Integer saveAppCase(AppCase ac);

    /**
     * 通过ID查询App用例
     * @param id
     * @return
     */
    AppCase getAppCaseById(@Param("caseId") Integer id);

    AppCase getParentCaseByCaseId(Integer caseId);

    /**
     * 根据条件查询app用例列表
     * @param ac
     * @return
     */
    List<AppCase> getList(AppCase ac);

    /**
     * 根据业务ID查询所有的APp用例
     * @param appCase
     * @return
     */
    List<AppCase> getAllByAppCase(AppCase appCase);

    Map<String, Long> appCaseCountNumber(@Param("businessId") Integer businessId);

    /**
     * 分页
     * @param name 用例名称
     * @return
     */
    List<PbtVO> findByPage(@Param("name") String name, @Param("businessId") Integer businessId);

    /**
     * 统计App侧，所有的用例数，未执行数，执行完成数，执行中数
     * @return
     */
    Map<String, Long> queryAppCaseCount();

    /**
     * 根据业务ID获取用例执行状态的分布情况
     * @param businessId
     * @return
     */
    Map<String, Long> getExecuteStatusCountByBusiness(@Param("businessId") Integer businessId);


    List<AppCase> selectByAppCase(AppCase appCase);


    /**
     * 获取满足调度条件的定时执行额用例列表
     * @return
     */
    List<AppCase> listAllTimingCase();
    /**
     * 根据条件查询
     */
    List<TestCase> getListByApgetIsFinishByBusinessIdpCase(TestCase testCase);
    List<TestCase> getListByAppCase(TestCase testCase);
    /**
     * 根据众测任务ID和用例ID查询众测任务关联的用例
     * @param surveyTaskId
     * @param caseId
     * @return
     */
    List<AppCase> getAppCaseBySurveyTaskIdAndCaseId(@Param("surveyTaskId") Integer surveyTaskId, @Param("caseId") Integer caseId);

    /**
     * 根据批量执行任务ID和用例ID查询批量指向任务关联的用例
     * @param sysTaskId
     * @param caseId
     * @return
     */
    List<AppCase> getAppCaseTaskIdAndCaseId(@Param("sysTaskId") Integer sysTaskId, @Param("caseId") Integer caseId);

    /**
     * 根据用例id集合、众测任务Id和省名称汇总用例报告数据
     * @param province        省份
     * @param surveyTaskId    众测任务ID
     * @param appCaseIdList   用例ID集合
     * @return
     */
    List<SurveyTaskReportVO> getCaseReportList(@Param("province") String province, @Param("surveyTaskId") Integer surveyTaskId,
                                               @Param("appCaseIdList") List<Integer> appCaseIdList);

    /**
     * 根据用例id集合、众测任务Id汇总用例报告数据
     *
     * @param appCaseIdList
     * @param surveyTaskId
     * @return
     */
    List<SurveyTaskReportVO> getCaseProvinceReportList(@Param("appCaseIdList") List<Integer> appCaseIdList, @Param("surveyTaskId") Integer surveyTaskId);

    /**
     * 根据用例id查最大轮次
     * @param id
     * @return
     */

    List<Long> getExcuteNumById(@Param("caseId") Integer id);

    int batchUpdateAppCase(@Param("caseList") List<AppCase> caseList);

    /**
     * 更新用例执行状态
     * @param map
     * @return
     */
    int updateExecuteStatusByMap(Map<String, Object> map);
    AppCase getAppCaseByoldId(@Param("caseId") Integer id);


    /**
     * 根据ids查询
     */
    List<AppCase> getTestCaseByIds(@Param("ids") List<Integer> ids);

    AppCaseBO getAppCaseBoByCaseId(@Param("caseId") Integer caseId);
}
