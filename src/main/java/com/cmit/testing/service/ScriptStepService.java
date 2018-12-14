package com.cmit.testing.service;

import java.util.List;

import com.cmit.testing.entity.TestCase;
import org.apache.ibatis.annotations.Param;

import com.cmit.testing.entity.ScriptStep;

public interface ScriptStepService extends BaseService<ScriptStep>{

    List<ScriptStep> selectByScriptId(@Param("scriptId") Integer scriptId);

    int deleteByModelScript(Integer id);

    /**
     * 执行用例
     * @param testCase 用例副本
     * @return
     */
    TestCase executeTestCase(TestCase testCase);

    /**
     * 执行用例前的操作：设置原用例状态为1-执行中,更改相关业务数据,生成用例副本
     * @param testCase 原用例
     * @param isExecuteBusiness 是否业务执行(业务执行生成的用例副本的业务id保留为原业务id,其他用例副本业务id设置为空)
     * @return 用例副本
     */
    public TestCase executeTestCaseBefore(TestCase testCase, boolean isExecuteBusiness);
    /**
     * 执行用例后的操作：更新当前用例(即原用例副本)的执行状态(0-执行完成),更新原用例的执行状态(0-执行完成),更改相关业务数据
     * @param testCase 用例副本
     */
    public void executeTestCaseAfter(TestCase testCase);
}
