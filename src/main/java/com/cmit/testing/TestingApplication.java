package com.cmit.testing;

import com.cmit.testing.quartz.QuartzTestTaskUtil;
import com.cmit.testing.utils.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@SpringBootApplication
public class TestingApplication {
    @Bean("poolExecutor")
    public ThreadPoolExecutor getExecutor() {
        return new ThreadPoolExecutor(5, 40,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static void main(String[] args) {
        SpringApplication.run(TestingApplication.class, args);
        RedisUtil.setObject("updateAllLocalData", "false");
        RedisUtil.setObject("simCardFlag", "false");
        QuartzTestTaskUtil.startTimedTask();//启动用例定时任务
/*

        FileStorageOperate fileStorageOperate = SpringContextHolder.getBean("fileStorageOperateImpl");
        String url = fileStorageOperate.fileStorageUploadFile("D://11.txt");
        System.out.println(url);
*/

    }

}
