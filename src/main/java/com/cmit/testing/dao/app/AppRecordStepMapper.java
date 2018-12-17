package com.cmit.testing.dao.app;

import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppRecordStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppRecordStepMapper {
    int deleteByPrimaryKey(Integer id);

    int insertList(@Param("stepList") List<AppRecordStep> stepList);

    int deleteRecordStep(AppRecordStep step);
    int deleteStepByExecuteId(@Param("ids") List<Integer> ids);

    int insert(AppRecordStep record);

    int insertSelective(AppRecordStep record);

    AppRecordStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppRecordStep record);

    int updateByPrimaryKey(AppRecordStep record);

    List<AppRecordStep> selectByExecuteId(@Param("ids") List<Integer> ids);

    int deleteByCaseResultIds(@Param("caseResultIds") List<Integer> caseResultIds);

    /**
     * 查询所有短信内容校验超时的步骤相关联的信息
     * @return
     */
    List<Map<String, Object>> getAllSmsVerifyStep();

    List<AppRecordStep> getAllStepByResultIds(@Param("resultIdList") List<Integer> resultIdList);
    List<AppRecordStep> getAppCaseStepByFewDays(@Param("day") String day, @Param("date") String date);
}