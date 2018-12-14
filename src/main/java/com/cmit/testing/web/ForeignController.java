package com.cmit.testing.web;

import com.cmit.testing.common.annotion.ForeignPermission;
import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.SysSurveyTask;
import com.cmit.testing.service.ForeignPermissionService;
import com.cmit.testing.service.PerceptionService;
import com.cmit.testing.service.SysSurveyTaskService;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.DateUtil;
import com.cmit.testing.utils.JsonResultUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangWanLi
 * 编写对外的接口
 * @date 2018/9/13 10:08
 */
@RestController
@RequestMapping("/api/v1/foreign")
public class ForeignController {

    @Autowired
    private ForeignPermissionService foreignPermissionService;
    
    @Autowired
    private SysSurveyTaskService sysSurveyTaskService;

	@Autowired
	private PerceptionService perceptionService;


	@RequestMapping(value = "/getToken",method = RequestMethod.GET)
    public JsonResultUtil getToken(String key,String time,String token){
        return new JsonResultUtil(foreignPermissionService.getForeignToken(key,time,token));
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ForeignPermission
    public JsonResultUtil test(String token){
        return new JsonResultUtil();
    }
    
    
    /**
     * 众测调用，向用户模拟平台发送任务及索要的指标（用户模拟平台接收众测平台发送过来的众测任务）type=0
     * @param map
     * @return
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/receiveSurveyTask/{type}", method = RequestMethod.POST)
	public Map<String,Object> receiveSurveyTask(@RequestBody Map<String, Object> map,@PathVariable("type")Integer type) {
		Map<String,Object> result = new HashMap<>();
		Map<String,Object> resultRoot = new HashMap<>();
		Map<String,Object> resultBody = new HashMap<>();
		try {
			Map<String, Object> root = (Map<String, Object>) map.get("ROOT");
			Map<String, Object> body = (Map<String, Object>) root.get("BODY");
			Map<String, Object> header = (Map<String, Object>) root.get("HEADER");
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS);
			Integer taskId = Integer.parseInt((String) body.get("TASK_ID"));
			String taskName = (String) body.get("TASK_NAME");
			String taskIss = (String) body.get("TASK_ISS");
			String tgAccuracy = (String) body.get("TG_ACCURACY");
			String tgClicksAvg = (String) body.get("TG_CLICKS_AVG");
			String tgInputAvg = (String) body.get("TG_INPUT_AVG");
			String tgNoteAvg = (String) body.get("TG_NOTE_AVG");
			String tgStepAvg = (String) body.get("TG_STEP_AVG");
			String ip = (String) header.get("IP");
			Date time = sdf.parse((String) header.get("TIME"));
			
			if (StringUtils.isNotEmpty(taskName) && StringUtils.isNotEmpty(taskIss)
					&& StringUtils.isNotEmpty(tgAccuracy) && StringUtils.isNotEmpty(tgClicksAvg)
					&& StringUtils.isNotEmpty(tgInputAvg) && StringUtils.isNotEmpty(tgNoteAvg)
					&& StringUtils.isNotEmpty(tgStepAvg) && StringUtils.isNotEmpty(ip)) {
				SysSurveyTask sTask = new SysSurveyTask();
				sTask.setTaskId(taskId);
				sTask.setTaskName(taskName);
				sTask.setTaskIss(taskIss);
				sTask.setTgAccuracy(tgAccuracy);
				sTask.setTgClicksAvg(tgClicksAvg);
				sTask.setTgInputAvg(tgInputAvg);
				sTask.setTgNoteAvg(tgNoteAvg);
				sTask.setTgStepAvg(tgStepAvg);
				sTask.setIp(ip);
				sTask.setTime(time);
				//如果众测发送重复任务id数据，更新重复任务id数据
				SysSurveyTask surveyTask =sysSurveyTaskService.selectByTaskId(taskId);
				if (surveyTask != null) {
					sysSurveyTaskService.updateByPrimaryKey(sTask);
				}else{
					sysSurveyTaskService.insert(sTask, type);
				}
				resultBody.put("CODE","0000");
				resultRoot.put("BODY",resultBody);
				result.put("ROOT",resultRoot);

			}else{
				resultBody.put("CODE","9999");
				resultBody.put("MSG","数据不能为空");
				resultRoot.put("BODY",resultBody);
				result.put("ROOT",resultRoot);
			}
			
		} catch (Exception e) {
			resultBody.put("CODE","9999");
			resultBody.put("MSG",e.getMessage());
			resultRoot.put("BODY",resultBody);
			result.put("ROOT",resultRoot);
		}
		return result;
	}


	/**
	 * 获取基本数据
	 * @return
	 */
	@RequestMapping(value = "/basic",method = RequestMethod.POST)
	public JsonResultUtil getBasiceData(){

		return new JsonResultUtil(perceptionService.getBasicData());
	}



}
