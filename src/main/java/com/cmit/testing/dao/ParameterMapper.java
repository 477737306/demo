package com.cmit.testing.dao;

import com.cmit.testing.entity.Parameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ParameterMapper extends BaseMapper<Parameter>{
    int deleteByPrimaryKey(Integer id);

    int insert(Parameter record);

    int insertSelective(Parameter record);

    Parameter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Parameter record);

    int updateByPrimaryKey(Parameter record);

    List<Parameter> selectByTestCase(Integer id);

    int deleteByTestCaseId(@Param("testcaseId") Integer testCaseId);

    int deleteByTestCaseIds(@Param("testcaseIds") List<Integer> testCaseIds);

    List<Parameter> getByTaseCaseId(@Param("testcaseId") Integer testCaseId);

    List<Parameter> findParameter(Parameter parameter);

}