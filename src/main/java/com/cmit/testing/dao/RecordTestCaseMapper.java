package com.cmit.testing.dao;

import com.cmit.testing.entity.RecordTestCase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordTestCaseMapper extends BaseMapper<RecordTestCase>{
    int deleteByPrimaryKey(Integer id);

    int insert(RecordTestCase record);

    int insertSelective(RecordTestCase record);

    RecordTestCase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecordTestCase record);

    int updateByPrimaryKey(RecordTestCase record);
}