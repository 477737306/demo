package com.cmit.testing.web;

import javax.ws.rs.QueryParam;

import com.cmit.testing.quartz.QuartzTestTaskUtil;
import com.cmit.testing.service.FastDFSClearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.TimedTask;
import com.cmit.testing.service.TimedTaskService;
import com.cmit.testing.utils.JsonResultUtil;

import java.util.List;
import java.util.Map;

/**
 * 定时任务管理相关接口
 * @author dingpeng
 * 2018年10月30日下午7:21:03
 *
 */
@RestController
@RequestMapping(value = "/api/v1/timedTask")
@Permission
@SystemLog("定时任务管理")
public class TimedTaskController extends BaseController {
	 	@Autowired
	    private TimedTaskService timedTaskService;
        @Autowired
        FastDFSClearService fastDFSClearService;
        @Value("${time}")
        String time;
	 	
	 	/**
	 	 * 根据条件分页查询定时任务信息
	 	 * @param currentPage
	 	 * @param pageSize
	 	 * @param timedTask
	 	 * @return
	 	 */
	 	 @RequestMapping(value = "/findPage", method = RequestMethod.GET)
	     public JsonResultUtil findPage(@QueryParam("currentPage") Integer currentPage,
	                                    @QueryParam("pageSize") Integer pageSize,
	                                    TimedTask timedTask) {
	         return new JsonResultUtil(200, "操作成功", timedTaskService.findPage(currentPage, pageSize, timedTask));
	     }
	 	 
		/**
		 * 更新定时任务
		 * @param timedTask
		 * @return
		 */
        @RequestMapping(value="update",method = RequestMethod.PUT)
		public JsonResultUtil update(@RequestBody TimedTask timedTask) {
            TimedTask timedTask1 = timedTaskService.selectByPrimaryKey(timedTask.getId());
            try {
                if(timedTask.getStatus() == 1){
                    timedTask1.setStatus(1);
                    QuartzTestTaskUtil.removeTimedTask(timedTask1);
             }
                if(timedTask.getStatus() == 0){
                    timedTask1.setStatus(0);
                    QuartzTestTaskUtil.addTimedTask(timedTask1);
              }
                timedTaskService.updateByTestCaseIdAndType(timedTask1);
                return new JsonResultUtil(200, "更新成功");
           } catch (Exception e) {
                    e.printStackTrace();
                return new JsonResultUtil(300, "更新失败,请重试或联系管理员");
           }
		}


    /**
     * 删除定时任务
     * @return
     */
    @RequestMapping(value = "/delete",  method = RequestMethod.DELETE)
      public JsonResultUtil delete(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
            for (Integer id : ids) {
                TimedTask timedTask = new TimedTask();
                timedTask.setId(id);
                timedTaskService.deleteTimedTask(timedTask);
            }
        return new JsonResultUtil(200, "删除成功");
    }
    /**
     * 清除fastdDFS服务器文件
     */
    @RequestMapping(value = "/clearFDS",method = RequestMethod.DELETE )
    public JsonResultUtil reportCurrentByCron(){
        try {
            logger.info("================清除fastdDFS服务器文件================");
           fastDFSClearService.ClearRubbish(time);
           fastDFSClearService.ClearAppRubbish(time);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResultUtil(300,"清理失败");
        }
        return new JsonResultUtil("已清理");
    }

}
