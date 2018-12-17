package com.cmit.testing.dao;

import com.cmit.testing.entity.ReportBusCase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportBusCaseMapper extends BaseMapper<ReportBusCase>{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    int insert(ReportBusCase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    int insertSelective(ReportBusCase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    ReportBusCase selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ReportBusCase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table report_bus_case
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ReportBusCase record);
}