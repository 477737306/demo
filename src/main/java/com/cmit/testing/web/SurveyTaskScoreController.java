package com.cmit.testing.web;

import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.entity.vo.SurveyTaskScoreItemVO;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;
import com.cmit.testing.service.SysSurveyTaskScoreService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/19
 */
@RestController
//@Permission
@RequestMapping(value = "/api/v1/surveyTaskScore")
public class SurveyTaskScoreController extends BaseController {
    @Autowired
    private SysSurveyTaskScoreService sysSurveyTaskScoreService;

    @RequestMapping("/list")
    public JsonResultUtil list(Integer surveyTaskId, Integer testcaseId) {
        try {
            List<SurveyTaskScoreVO> list = sysSurveyTaskScoreService.list(surveyTaskId, testcaseId);
            return new JsonResultUtil(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResultUtil(300, "查询失败");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResultUtil save(@RequestBody String json) {
        JSONObject map = JsonUtil.parseJsonObject(json);
        Integer testcaseId = map.getInteger("testcaseId");
        Integer surveyTaskId = map.getInteger("surveyTaskId");
        Gson gson = new Gson();
        List<SurveyTaskScoreVO> list = gson.fromJson((JsonUtil.toJson(map.get("list"))).trim(), new TypeToken<List<SurveyTaskScoreVO>>() {
        }.getType());
        boolean flag = true;
        for (SurveyTaskScoreVO taskScoreVO :
                list) {
            BigDecimal max = new BigDecimal(0);
            for (SurveyTaskScoreItemVO scoreItem :
                    taskScoreVO.getItems()) {
                BigDecimal start = new BigDecimal(0);
                String startStr = scoreItem.getStart();
                BigDecimal end = null;
                String endStr = scoreItem.getEnd();
                if (StringUtils.isNotEmpty(startStr) && startStr.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?")) {
                    start = new BigDecimal(startStr);
                }
                if (StringUtils.isNotEmpty(endStr) && endStr.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?")) {
                    end = new BigDecimal(endStr);
                }

                if (max.compareTo(start) == 0) {
                    if (null == end) {

                    } else if (max.compareTo(end) == -1 && max.compareTo(start) == 0) {
                        max = end;
                    } else if (end != null) {
                        return new JsonResultUtil(300, "验证不通过");
                    }
                } else {
                    return new JsonResultUtil(300, "验证不通过");
                }
            }
        }
        try {
            flag = sysSurveyTaskScoreService.save(list, surveyTaskId, testcaseId);
            if (flag) {
                return new JsonResultUtil(200, "插入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResultUtil(300, "插入失败");
    }


}
