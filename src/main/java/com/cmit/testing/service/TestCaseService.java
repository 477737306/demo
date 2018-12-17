package com.cmit.testing.service;

import com.cmit.testing.entity.DownloadFileDto;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.vo.PbtVO;
import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.page.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用例Service
 *
 * @author YangWanLi
 * @date 2018/7/9 13:27
 */
public interface TestCaseService extends BaseService<TestCase>{

   //
   List<Map<String ,List<TestCase>>> testCaseExecuteReorder(Map<String, List<Integer>> caseMap);

    //批量更新用例依赖
    int batchUpdateTestCase(List<TestCase> list);

    public boolean isExistSerialNumber(Integer projectId, Integer id, String serialNumber);
    /**
     * 保存
     * @param map
     * @return
     */
    Integer saveTestCase(Map<String, Object> map, Integer id);

    List<TestCase> getTestCasesBySurveyTaskId(Integer surveyTaskId);

    Map<String,Integer> getTestCaseMaxExcuteNum(Integer id);

    int deleteByPrimaryKey(Integer id);

    TestCase selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(TestCase record);


    boolean copy(List<SysPermission> list, int menuParentId);

    /**
     * shearModelScript
     * @param list 剪切的菜单list
     * @param menuParentId 剪切到目标对象的id
     */
    boolean shear(List<SysPermission> list, int menuParentId);


    /**
     * 根据条件查询
     * @param testCase
     * @return
     */
    List<TestCase> getListByTestCase(TestCase testCase);

    /**
     * 脚本关联的用例分页
     * @param testCase
     * @return
     */
    PageBean<TestCase> getPageByScriptId(PageBean<TestCase> pageBean, TestCase testCase);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 分页
     * @param name 用户名
     * @return
     */
    List<PbtVO> findByPage(String name, Integer businessId);

    /**
     * 根据任务Id查询用例
     * @param taskId
     * @return
     */
    PageBean<TestCase> getTestCaseByTaskId(PageBean<TestCase> pageBean, Integer taskId);

    /**
     * 通过业务查询用例
     * @param testCase
     * @return
     */
    List<TestCase> selectTastCaseTive(TestCase testCase);

    /**
     * 获取批次号
     * @param id
     * @return
     */
    List<Long> getExcuteNumById(Integer id);

    /**
     * 根据所选用例及轮次生成报告
     * @param list
     * @return
     */
    List<Map<String,Object>> getTestCaseReportByTaskId(List<TestCase> list, Integer number);

    /**
     * 统计业务下用例的成功数与失败数
     * @param businessId
     * @return
     */
    Map<String,Long> testCaseCountNumber(Integer businessId);

    /**
     * 根据众测任务id及用例id查询用例
     * @param surveyTaskid
     * @param id
     * @return
     */
    List<TestCase> getTestCaseBySurTaskIdAndCaseId(Integer surveyTaskid, Integer id);

//    List<TestCase> getTestCaseByTaskIdAndCaseId(Integer taskid,Integer id);

    /**
     * 更改原用例的成功、失败、执行状态等数据
     * @param newId（新的用例id）
     * @return
     */
    void updateTestcaeData(Integer newId);

    /**
     * 获取所有执行中的用例
     *  @param pageBean 分页bean
     *  @param testCaseReportVO 参数Vo
     */
    PageBean<TestCase> getAllExecutingCase(PageBean<TestCase> pageBean, TestCaseReportVO testCaseReportVO);

    /**
     * 根据执行类型查询用例父模板
     * @param executionType 执行类型 0立即执行，1指定时间执行，2指定周期执行，3不执行
     * @return
     */
    List<TestCase> getTestCaseByExeType(Integer executionType);

    /**
     * 压缩用例截图
     * @param testCaseReport
     * @return
     */
    List<DownloadFileDto> downloadZipScreenshots(TestCaseReportVO testCaseReport);

	/**
	* 获取业务下正在执行的用例数
	* @param businessId
	* @return
	*/
	Integer getExecutingCountByBusiness(@Param("businessId") Integer businessId);

	/**
	 * 根据原用例id和任务id获取用例副本
	 * @param oldTestcaseId 原用例id
	 * @param sysTaskId 任务id
	 * @return
	 */
	List<TestCase> getTestCasesByOldTestcaseIdAndSysTaskId(Integer oldTestcaseId, Integer sysTaskId);

	/**
	 * 获取用例的手机号
     * @param testCase
     * @return
	 */
	List<String> getPhoneListByTestCase(TestCase testCase);
}
