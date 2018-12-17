package com.cmit.testing;

import com.cmit.testing.entity.vo.TestCaseReportVO;
import com.cmit.testing.security.shiro.MyShiroRealm;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.service.*;
import com.cmit.testing.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestingApplicationTests {

    @Autowired
    private ScriptStepService scriptStepService;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private TestCaseReportService testCaseReportService;
    @Autowired
    SysPermissionService  sysPermissionService;
    @Test
    public void contextLoads() {
        sysPermissionService.getPermissionsBydataIdAndType(10066,"case");
    }

}
