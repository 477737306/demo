package com.cmit.testing.entity;

public class ReportBusCase {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_bus_case.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_bus_case.report_id
     *
     * @mbggenerated
     */
    private Integer reportId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_bus_case.business_id
     *
     * @mbggenerated
     */
    private Integer businessId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_bus_case.testcase_id
     *
     * @mbggenerated
     */
    private String testcaseId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_bus_case.id
     *
     * @return the value of report_bus_case.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_bus_case.id
     *
     * @param id the value for report_bus_case.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_bus_case.report_id
     *
     * @return the value of report_bus_case.report_id
     *
     * @mbggenerated
     */
    public Integer getReportId() {
        return reportId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_bus_case.report_id
     *
     * @param reportId the value for report_bus_case.report_id
     *
     * @mbggenerated
     */
    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_bus_case.business_id
     *
     * @return the value of report_bus_case.business_id
     *
     * @mbggenerated
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_bus_case.business_id
     *
     * @param businessId the value for report_bus_case.business_id
     *
     * @mbggenerated
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_bus_case.testcase_id
     *
     * @return the value of report_bus_case.testcase_id
     *
     * @mbggenerated
     */
    public String getTestcaseId() {
        return testcaseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_bus_case.testcase_id
     *
     * @param testcaseId the value for report_bus_case.testcase_id
     *
     * @mbggenerated
     */
    public void setTestcaseId(String testcaseId) {
        this.testcaseId = testcaseId == null ? null : testcaseId.trim();
    }
}