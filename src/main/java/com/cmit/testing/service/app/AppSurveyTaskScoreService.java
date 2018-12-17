package com.cmit.testing.service.app;

import com.cmit.testing.entity.SysSurveyTaskScore;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;
import com.cmit.testing.service.BaseService;

import java.util.List;

/**
 * @author zhangxiaofang
 * @date 2018/9/19
 */
public interface AppSurveyTaskScoreService extends BaseService<SysSurveyTaskScore> {
    /**
     * 根据surveyTaskId、testcaseId查询其全部的打分标准
     *
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    List<SurveyTaskScoreVO> list(Integer surveyTaskId, Integer testcaseId);

    /**
     * 添加评分标准
     *
     * @param scoreVOList
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    boolean save(List<SurveyTaskScoreVO> scoreVOList, Integer surveyTaskId, Integer testcaseId) throws Exception;
}
