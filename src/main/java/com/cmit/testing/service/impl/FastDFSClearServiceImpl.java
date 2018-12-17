package com.cmit.testing.service.impl;

import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.dao.app.AppCaseResultMapper;
import com.cmit.testing.dao.app.AppRecordStepMapper;
import com.cmit.testing.entity.TestCaseReport;
import com.cmit.testing.entity.app.AppCaseResult;
import com.cmit.testing.entity.app.AppRecordStep;
import com.cmit.testing.fastdfs.FileStorageOperate;
import com.cmit.testing.listener.timer.TaskScheduleServiceImpl;
import com.cmit.testing.service.FastDFSClearService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class FastDFSClearServiceImpl implements FastDFSClearService {

    @Autowired
    private FileStorageOperate fileStorageOperate;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;
    @Autowired
    private AppCaseResultMapper  appCaseResultMapper;
    @Autowired
    private AppRecordStepMapper appRecordStepMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScheduleServiceImpl.class);


    @Override
    public void ClearRubbish(String time) {

            Date date1 =new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(date1);
            List<TestCaseReport> testCaseReports = testCaseReportMapper.getTestCaseReportByFewDays(time,date);
            for (TestCaseReport testCaseReport : testCaseReports) {
                if(StringUtils.isNotBlank(testCaseReport.getSnapshotLocation())){
                    boolean b = fileStorageOperate.deleteFile(testCaseReport.getSnapshotLocation());
                    if(b)
                    testCaseReport.setSnapshotLocation(null);
                }
                if (StringUtils.isNotBlank(testCaseReport.getVideoLocation())){
                    boolean b = fileStorageOperate.deleteFile(testCaseReport.getVideoLocation());
                    if(b)
                    testCaseReport.setVideoLocation(null);
                }
               testCaseReportMapper.updateByPrimaryKey(testCaseReport);
            }

           LOGGER.info("清理FastDFS-----------------");
    }

    @Override
    public void ClearAppRubbish(String time) {

                Date date1 =new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(date1);
               //List<TestCaseReport> testCaseReports = testCaseReportMapper.getTestCaseReportByFewDays(time,date);
                List<AppCaseResult> appCaseResults=appCaseResultMapper.getTestAppCaseReportByFewDays(time,date);
                for (AppCaseResult appCaseResult : appCaseResults) {
                    if(StringUtils.isNotBlank(appCaseResult.getScreenshotUrl())){
                        boolean b = fileStorageOperate.deleteFile(appCaseResult.getScreenshotUrl());
                        if(b)
                            appCaseResult.setScreenshotUrl(null);

                    }
                    if (StringUtils.isNotBlank(appCaseResult.getRecordVideoUrl())){
                        boolean b = fileStorageOperate.deleteFile(appCaseResult.getRecordVideoUrl());
                        if(b)
                            appCaseResult.setRecordVideoUrl(null);
                    }
                    appCaseResultMapper.updateByPrimaryKey(appCaseResult);
                }
                List<AppRecordStep> appRecordSteps=appRecordStepMapper.getAppCaseStepByFewDays(time,date);
                for (AppRecordStep appRecordStep:appRecordSteps){
                    if(StringUtils.isNotBlank(appRecordStep.getScreenShotUrl())){
                        boolean b = fileStorageOperate.deleteFile(appRecordStep.getScreenShotUrl());
                        if(b)
                            appRecordStep.setScreenShotUrl(null);

                    }
                    appRecordStepMapper.updateByPrimaryKey(appRecordStep);
                }
        LOGGER.info("清理FastDFS-----------------");
             }

    }
