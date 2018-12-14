package com.cmit.testing.web.app;

import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.entity.vo.SurveyTaskScoreItemVO;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;
import com.cmit.testing.service.SysSurveyTaskScoreService;
import com.cmit.testing.service.app.AppSurveyTaskScoreService;
import com.cmit.testing.utils.JsonResultUtil;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.VideoServiceNotifier;
import com.cmit.testing.web.BaseController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangxiaofang
 * @date 2018/10/25
 */
@RestController
//@Permission
@RequestMapping(value = "/api/v1/appSurveyTaskScore")
public class AppSurveyTaskScoreController extends BaseController {
    @Autowired
    private AppSurveyTaskScoreService appSurveyTaskScoreService;

    private Logger logger = LoggerFactory.getLogger(AppSurveyTaskScoreController.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResultUtil list(Integer surveyTaskId, Integer testcaseId)
    {
        try
        {
            List<SurveyTaskScoreVO> list = appSurveyTaskScoreService.list(surveyTaskId, testcaseId);
            return new JsonResultUtil(list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new JsonResultUtil(300, "查询失败");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResultUtil save(@RequestBody String json)
    {
        JSONObject map = JsonUtil.parseJsonObject(json);
        Integer testcaseId = map.getInteger("testcaseId");
        Integer surveyTaskId = map.getInteger("surveyTaskId");
        Gson gson = new Gson();
        List<SurveyTaskScoreVO> list = gson.fromJson((JsonUtil.toJson(map.get("list"))).trim(), new TypeToken<List<SurveyTaskScoreVO>>() {
        }.getType());
        boolean flag = true;
        for (SurveyTaskScoreVO taskScoreVO : list)
        {
            BigDecimal max = new BigDecimal(0);
            for (SurveyTaskScoreItemVO scoreItem : taskScoreVO.getItems())
            {
                BigDecimal start = new BigDecimal(0);
                String startStr = scoreItem.getStart();
                BigDecimal end = null;
                String endStr = scoreItem.getEnd();
                if (StringUtils.isNotEmpty(startStr) && startStr.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?"))
                {
                    start = new BigDecimal(startStr);
                }
                if (StringUtils.isNotEmpty(endStr) && endStr.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?"))
                {
                    end = new BigDecimal(endStr);
                }

                if (max.compareTo(start) == 0)
                {
                    if (null == end)
                    {

                    }
                    else if (max.compareTo(end) == -1 && max.compareTo(start) == 0)
                    {
                        max = end;
                    }
                    else if (end != null)
                    {
                        return new JsonResultUtil(300, "验证不通过");
                    }
                }
                else
                {
                    return new JsonResultUtil(300, "验证不通过");
                }
            }
        }
        try
        {
            flag = appSurveyTaskScoreService.save(list, surveyTaskId, testcaseId);
            if (flag)
            {
                return new JsonResultUtil(200, "插入成功");
            }
        } catch (Exception e) {
            logger.error("添加评分失败!",e);
        }
        return new JsonResultUtil(300, "插入失败");
    }


}
