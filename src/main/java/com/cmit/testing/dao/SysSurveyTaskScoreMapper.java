package com.cmit.testing.dao;

import com.cmit.testing.entity.SysSurveyTaskScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/19
 */
@Mapper
public interface SysSurveyTaskScoreMapper extends BaseMapper<SysSurveyTaskScore> {
    /**
     * 根据surveyTaskId、testcaseId查询其全部的打分标准
     *
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    List<SysSurveyTaskScore> list(@Param("surveyTaskId") Integer surveyTaskId, @Param("testcaseId") Integer testcaseId);

    /**
     * 根据surveyTaskId、testcaseId批量删除打分标准
     *
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    int deleteBySurveyTask(@Param("surveyTaskId") Integer surveyTaskId, @Param("testcaseId") Integer testcaseId);
}
