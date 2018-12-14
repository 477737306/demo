package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.Business;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.BusinessService;
import com.cmit.testing.service.ScriptStepService;
import com.cmit.testing.service.TestCaseReportService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务Controller
 * @author YangWanLi
 * @date 2018/7/25 10:19
 */
@RestController
@RequestMapping(value = "/api/v1/business")
@SystemLog("业务模块")
@Permission
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;
    
    @Autowired
	private TestCaseService caseService;
    
    @Autowired
    private ScriptStepService scriptStepService; 
    @Autowired
    private TestCaseReportService caseReportService;

//    /**
//     * 添加
//     * @param record
//     * @return
//     */
//
//    @RequestMapping
//    public JsonResultUtil add(Business record){
//        businessService.insert(record);
//        return new JsonResultUtil(200,"操作成功");
//    }

    /**
     * 修改
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonResultUtil update(@PathVariable("id") Integer id, Business record){
        record.setId(id);
        businessService.updateByPrimaryKey(record);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 批量删除
     * @return
     */
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE)
    public JsonResultUtil deleteByIds(@RequestBody Map<String,Object> map){
        List<Integer> ids = (List<Integer>)map.get("ids");
        businessService.deleteByIds(ids);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResultUtil delete(@PathVariable("id")Integer id){
        businessService.deleteByPrimaryKey(id);
        return new JsonResultUtil(200,"操作成功");
    }

    /**
     * 获取
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResultUtil get(@PathVariable("id")Integer id){
        return new JsonResultUtil(200,"操作成功", businessService.selectByPrimaryKey(id));
    }


    /**
     * 业务报告分页
     * @param pageBean
     * @param business
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public JsonResultUtil findPage(PageBean<Object> pageBean, Business business){
        return new JsonResultUtil(businessService.findPage(pageBean,business));
    }
    
    /**
     * 业务执行
     * 1.根据业务ID查找用例
     * 2.根据用例执行
     */
    @RequestMapping(value = "/executeBusiness/{id}", method = RequestMethod.GET)
	public JsonResultUtil  executeBusiness(@PathVariable("id") Integer id){
    	TestCase tt = new TestCase();
    	tt.setBusinessId(id); // set Id
//    		int success = 0, faild = 0;
    	
    	List<TestCase> list = caseService.selectTastCaseTive(tt);
    	for (int i = 0; i < list.size(); i++) {
    		// 生成用例副本
    		TestCase t1 = scriptStepService.executeTestCaseBefore(list.get(i), true);
    		// 执行用例副本
    		scriptStepService.executeTestCase(t1);
    		// 判断用例是否执行结束
    		while (2 != ThreadUtils.getTestcaseExecuteStatus(t1.getId())) {
    			Thread.yield();
    		}
    		// 更改用例与用例副本的状态
    		scriptStepService.executeTestCaseAfter(t1);
    	}
    	Business bs = businessService.selectByPrimaryKey(id);
    	/*bs.setSuccessNum(success);
			bs.setFailureNum(faild);*/
		return new JsonResultUtil(200,"操作成功");
    }
}
