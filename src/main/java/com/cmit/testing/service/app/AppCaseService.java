package com.cmit.testing.service.app;

import com.cmit.testing.entity.DownloadFileDto;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.proxy.CmdParamStartTask;
import com.cmit.testing.entity.proxy.TestResultInfo;
import com.cmit.testing.entity.vo.CaseExcResultVO;


import com.cmit.testing.entity.vo.CommonResultVO;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.entity.vo.SurveyTaskReportVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO app用例Service
 *
 * @date 2018/8/28 0028 下午 2:26.
 */
public interface AppCaseService extends BaseService<AppCase> {

    PageBean<AppCase> getAllRunningAppCase(PageBean<AppCase> pageBean, CaseExcResultVO resultVO);

    /**
     * 统计AppCase的已执行用例次数
     * @param appCaseId
     */
    void countAppCaseExecuteNumber(Integer appCaseId);

    /**
     * 根据众测任务ID和原用例ID 获取对应副本信息
     */
    List<AppCase> getAppCaseBySurveyTaskIdAndCaseId(Integer surveyTaskId, Integer caseId);

    /**
     * 根据批量任务ID和原用例ID获取对应副本用例的信息
     * @param sysTaskId
     * @param caseId
     * @return
     */
    List<AppCase> getAppCaseTaskIdAndCaseId(Integer sysTaskId, Integer caseId);

    /**
     * 批量删除App用例
     * @param ids
     * @return
     */
    Integer deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 添加App用例
     * @param map
     * @param id
     * @return
     */
    Integer saveAppCase(Map<String, Object> map, Integer id);

    /**
     * 通过ID查询App用例
     * @param id
     * @return
     */
    AppCase getAppCaseById(@Param("caseId") Integer id);

    /**
     * 根据条件查询app用例列表
     * @param ac
     * @return
     */
    List<AppCase> getList(AppCase ac);

    /**
     * 根据caseID将case相关的信息打包成zip包
     * @param caseId
     * @return
     */
    Map<String, String> buildZipByCaseId(Integer caseId);

    /**
     * 根据caseID将case相关的信息打包成zip包
     * @param appCase
     * @return
     */
    Map<String, String> buildZipByCase(AppCase appCase);

    /**
     * 获取所有的APP用例
     * @param appCase
     * @return
     */
    List<AppCase> getAllByAppCase(AppCase appCase);

    /**
     * 更新当前proxy对应终端机的测试状态
     *
     * @param cmdParamStartTask
     * @param proxyIpMac proxy的ip和mac
     */
    void updateTestCase(CmdParamStartTask cmdParamStartTask, String proxyIpMac) throws Exception;

    /**
     * 读取用例执行结果
     *
     * @param testResultInfo
     */
    void readTestResult(TestResultInfo testResultInfo) throws Exception;

    /**
     * 统计指定业务下的所有用例执行的成功数和失败数
     * @param businessId
     * @return
     */
    Map<String, Long> appCaseCountNumber(Integer businessId);

    /**
     * 用例的执行成功数和失败数
     * @param name
     * @param businessId
     * @return
     */
    List<PbtVO> findByPage(String name, Integer businessId);

    /**
     * 手动执行用例
     * @param appCaseId
     * @return
     */
    Map<String, String> handExecuteCase(Integer appCaseId);

    /**
     * 统计用例、脚本、设备执行失败用例个数统计
     */
    Map<String,Integer> caseScriptCount();

    /**
     * 获取满足调度条件的定时执行额用例列表
     * @return
     */
    List<AppCase> listAllTimingCase();
    /**
     * 根据条件查询
     * @param testCase
     * @return
     */
    List<TestCase> getListByAppCase(TestCase testCase);

    /**
     * 根据用例ID查询该用例是否执行完成
     * 若执行完成，则更改状态及相关信息
     * @param appCase  副本用例
     * @param devSnList
     */
    void checkCaseIsFinish(AppCase appCase, List<String> devSnList);

    /**
     * 众测任务-同步数据到众测平台
     * @param sysSurveyTask
     * @return
     */
    List<Map<String ,Object>> syncSysSurveyTaskData(SysSurveyTask sysSurveyTask, Map<String, List<Integer>> caseIds);

    /**
     * 获取众测任务的报告信息
     * @param surveyTaskId 众测任务ID
     * @param province     省份
     * @param type
     *      如填0，表示不区分省份，1=全网平均，2=省份
     * @return
     */
    List<SurveyTaskReportVO> getAppCaseReportList(Integer surveyTaskId, String province, Integer type);


    boolean copy(List<SysPermission> list, int menuParentId);

    boolean shear(List<SysPermission> list, int menuParentId);
    int  updateByPrimaryKeySelective(AppCase appCase);
    /***
     * 根据caseid查最大批次号
     */
    List<Long> getExcuteNumById(Integer caseId);

    /**
     * 获取关联用例的相关数据
     * @param caseIds
     * @return
     */
    List<Map<String, Object>> getAppCaseByCaseIds(List<Integer> caseIds);

    /**
     * 提供给web侧使用：获取指定的用例的完成状态
     * @param appCaseId 副本用例ID
     * @return
     *  true - 执行完成； false - 未完成
     */
    boolean getAppCaseFinishStatus(Integer appCaseId);

   List<DownloadFileDto> downloadCompressPicZip(CommonResultVO caseVO);

    int batchUpdateAppCase(List<AppCase> caseList);

    /**
     * 根据业务ID获取当前业务下所有正在执行中的原用例
     * @param businessId
     * @return
     */
    Integer getExecutingCountByBusiness(Integer businessId);

    /**
     * 根据ids查询
     */
    List<AppCase> getTestCaseByIds(List<Integer> ids);
}
