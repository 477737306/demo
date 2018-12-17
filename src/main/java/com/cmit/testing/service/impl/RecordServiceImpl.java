package com.cmit.testing.service.impl;

import com.cmit.testing.dao.RecordStepMapper;
import com.cmit.testing.dao.RecordTestCaseMapper;
import com.cmit.testing.entity.RecordStep;
import com.cmit.testing.entity.vo.CaseExecutionProcessVO;
import com.cmit.testing.entity.vo.RecordStepVO;
import com.cmit.testing.page.PageBean;
import com.cmit.testing.service.RecordService;
import com.cmit.testing.service.app.AppCaseResultService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suviky on 2018/7/31 17:07
 */
@Service
@Transactional
public class RecordServiceImpl extends BaseServiceImpl<RecordStep> implements RecordService {
    @Autowired
    private RecordStepMapper recordStepMapper;
    @Autowired
    private AppCaseResultService appCaseResultService;
    @Override
    public boolean insertRecord(List<RecordStep> list) {
        try {
            if(list.size()>0) {
                recordStepMapper.batchInsert(list);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("生成执行记录出错");
            return false;
        }
    }

    @Override
    public List<String> getInfoFromDependCase(Integer id) {
        return recordStepMapper.getInfoFromDependCase(id);
    }

    @Override
    public int deleteByTestcaseReportId(Integer testcaseReportId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(testcaseReportId);
        return recordStepMapper.deleteByTestcaseReportIds(ids);
    }

    /**
     * 步骤详情
     * @param recordStepVO
     * @return
     */
    @Override
    public List<RecordStepVO> findAll(RecordStepVO recordStepVO) {
        List<RecordStepVO> recordStepVOS = new ArrayList<>();
        if(null==recordStepVO.getType()){
            recordStepVOS.addAll(recordStepMapper.findPage(recordStepVO));//web步骤详情
            recordStepVOS.addAll(appCaseResultService.findstep(recordStepVO));//app步骤详情
        }else if(recordStepVO.getType().equals("web")) {
            recordStepVOS.addAll(recordStepMapper.findPage(recordStepVO));//web步骤详情
        }else if(recordStepVO.getType().equals("app")) {
            recordStepVOS.addAll(appCaseResultService.findstep(recordStepVO));//app步骤详情
        }
        return recordStepVOS;
    }

	@Override
	public  List<CaseExecutionProcessVO> getCaseExecutionProcess(Integer testcastId, String phoneNum) {
		return recordStepMapper.getCaseExecutionProcess(testcastId, phoneNum);
	}

	
}
