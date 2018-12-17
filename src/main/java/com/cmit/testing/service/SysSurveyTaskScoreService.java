package com.cmit.testing.service;

import com.cmit.testing.entity.SysSurveyTaskScore;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;

import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/19
 */
public interface SysSurveyTaskScoreService extends BaseService<SysSurveyTaskScore> {
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
