package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.SysPermission;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.service.ProjectMenuService;
import com.cmit.testing.service.ScriptStepService;
import com.cmit.testing.service.SysPermissionService;
import com.cmit.testing.service.TestCaseService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/scriptstep")
@ResponseBody
@SystemLog("脚本步骤模块")
@Permission
public class ScriptStepController extends BaseController {

	@Autowired
	private ScriptStepService scriptStepService;
	
	@Autowired
	private TestCaseService caseService;

	@Autowired
	private ProjectMenuService projectMenuService;

	@Autowired
	private SysPermissionService sysPermissionService;

	@RequestMapping(value = "/executeScript", method = RequestMethod.POST)
	/*  */
	public JsonResultUtil execute(@RequestBody TestCase testCase) {
		SysPermission sysPermission = sysPermissionService.getPermissionsBydataIdAndType(testCase.getId(),"case");
		if(!projectMenuService.checkStatus(sysPermission.getId())){
			throw new TestSystemException("项目已被归档，不可操作！");
		}
		TestCase tc = caseService.selectByPrimaryKey(testCase.getId());
		// 生成用例副本
		TestCase t1 = scriptStepService.executeTestCaseBefore(tc, false);
		// 执行用例副本
		scriptStepService.executeTestCase(t1);
		// 判断用例是否执行结束
		while (2 != ThreadUtils.getTestcaseExecuteStatus(t1.getId())) {
			Thread.yield();
		}
		// 更改用例与用例副本的状态
		scriptStepService.executeTestCaseAfter(t1);
		return new JsonResultUtil(200, "");
	}
}
