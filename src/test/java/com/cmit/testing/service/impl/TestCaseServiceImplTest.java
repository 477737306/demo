package com.cmit.testing.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.entity.OrderRelationship;
import com.cmit.testing.entity.TestCase;
import com.cmit.testing.service.OrderRelationshipService;
import com.cmit.testing.service.TestCaseService;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCaseServiceImplTest {
	@Autowired
	private TestCaseServiceImpl testCaseService;

	@Test
	public void getTestCasesByOldTestcaseIdAndSysTaskId() {
		List<TestCase> a = testCaseService.getTestCasesByOldTestcaseIdAndSysTaskId(2370, 45);
		for (TestCase testCase : a) {
			System.out.println(testCase);
		}
	}
}