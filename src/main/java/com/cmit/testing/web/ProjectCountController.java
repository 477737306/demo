package com.cmit.testing.web;

import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.service.ProjectCountService;
import com.cmit.testing.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计分析
 * @author YangWanLi
 * @date 2018/8/9 18:46
 */

@RestController
@RequestMapping("api/v1/projectCount")
@Permission
public class ProjectCountController extends BaseController {

    @Autowired
    private ProjectCountService projectCountService;

    /**
     * 统计
     * @return
     */
    @RequestMapping(value = "/projectCountData", method = RequestMethod.GET)
    public JsonResultUtil projectCountData(){
        return new JsonResultUtil(projectCountService.projectCount());
    }
}
