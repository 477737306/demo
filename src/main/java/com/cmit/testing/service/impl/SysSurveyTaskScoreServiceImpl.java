package com.cmit.testing.service.impl;

import com.cmit.testing.dao.SysSurveyTaskScoreMapper;
import com.cmit.testing.entity.SysSurveyTaskScore;
import com.cmit.testing.entity.vo.SurveyTaskScoreItemVO;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;
import com.cmit.testing.service.SysSurveyTaskScoreService;
import com.cmit.testing.utils.SurveyTaskScoreTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiBin
 * @date 2018/9/19
 */
@Service
public class SysSurveyTaskScoreServiceImpl extends BaseServiceImpl<SysSurveyTaskScore> implements SysSurveyTaskScoreService {
    @Autowired
    private SysSurveyTaskScoreMapper sysSurveyTaskScoreMapper;

    @Override
    public List<SurveyTaskScoreVO> list(Integer surveyTaskId, Integer testcaseId) {
        List<SurveyTaskScoreVO> list = new ArrayList<>();
        List<SysSurveyTaskScore> scores = sysSurveyTaskScoreMapper.list(surveyTaskId, testcaseId);
        List<SurveyTaskScoreItemVO> caseItemList = scores.stream().filter(a -> SurveyTaskScoreTypeEnum.CASE_TIME.getCode().equals(a.getType())).map(e -> new SurveyTaskScoreItemVO(e.getStart(), e.getEnd(), e.getScore())).collect(Collectors.toList());
        list.add(new SurveyTaskScoreVO(SurveyTaskScoreTypeEnum.CASE_TIME, caseItemList));
        List<SurveyTaskScoreItemVO> clickItemList = scores.stream().filter(a -> SurveyTaskScoreTypeEnum.CLICK_NUM.getCode().equals(a.getType())).map(e -> new SurveyTaskScoreItemVO(e.getStart(), e.getEnd(), e.getScore())).collect(Collectors.toList());
        list.add(new SurveyTaskScoreVO(SurveyTaskScoreTypeEnum.CLICK_NUM, clickItemList));
        List<SurveyTaskScoreItemVO> inputItemList = scores.stream().filter(a -> SurveyTaskScoreTypeEnum.INPUT_NUM.getCode().equals(a.getType())).map(e -> new SurveyTaskScoreItemVO(e.getStart(), e.getEnd(), e.getScore())).collect(Collectors.toList());
        list.add(new SurveyTaskScoreVO(SurveyTaskScoreTypeEnum.INPUT_NUM, inputItemList));
        List<SurveyTaskScoreItemVO> msgItemList = scores.stream().filter(a -> SurveyTaskScoreTypeEnum.MSG_TIME.getCode().equals(a.getType())).map(e -> new SurveyTaskScoreItemVO(e.getStart(), e.getEnd(), e.getScore())).collect(Collectors.toList());
        list.add(new SurveyTaskScoreVO(SurveyTaskScoreTypeEnum.MSG_TIME, msgItemList));
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(List<SurveyTaskScoreVO> scoreVOList, Integer surveyTaskId, Integer testcaseId) throws Exception {
        List<SurveyTaskScoreVO> collect = scoreVOList.stream().filter(a -> SurveyTaskScoreTypeEnum.getSurveyTaskScoreTypeEnumCode(a.getType()) == null).collect(Collectors.toList());
        if (collect.size() > 0) {
            return false;
        }

        try {
            //删除原评分
            sysSurveyTaskScoreMapper.deleteBySurveyTask(surveyTaskId, testcaseId);
            for (SurveyTaskScoreVO taskScoreVO : scoreVOList) {
                String type = taskScoreVO.getType();
                for (SurveyTaskScoreItemVO item : taskScoreVO.getItems()) {
                    SysSurveyTaskScore record = new SysSurveyTaskScore();
                    BeanUtils.copyProperties(item, record);
                    record.setSurveyTaskId(surveyTaskId);
                    record.setTestcaseId(testcaseId);
                    record.setType(type);
                    sysSurveyTaskScoreMapper.insert(record);
                }
            }
            return true;
        } catch (BeansException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
}
