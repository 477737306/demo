package com.cmit.testing.dao.app;

import com.cmit.testing.dao.BaseMapper;
import com.cmit.testing.entity.app.AppSysSurveyTaskScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangxiaofang
 * @date 2018/10/26
 */
@Mapper
public interface AppSurveyTaskScoreMapper extends BaseMapper<AppSysSurveyTaskScore> {
    /**
     * 根据surveyTaskId、testcaseId查询其全部的打分标准
     *
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    List<AppSysSurveyTaskScore> list(@Param("surveyTaskId") Integer surveyTaskId, @Param("testcaseId") Integer testcaseId);

    /**
     * 根据surveyTaskId、testcaseId批量删除打分标准
     *
     * @param surveyTaskId
     * @param testcaseId
     * @return
     */
    int deleteBySurveyTask(@Param("surveyTaskId") Integer surveyTaskId, @Param("testcaseId") Integer testcaseId);
}
