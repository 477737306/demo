package com.cmit.testing.service.app.impl;


import com.cmit.testing.dao.app.AppSurveyTaskScoreMapper;
import com.cmit.testing.entity.SysSurveyTaskScore;
import com.cmit.testing.entity.app.AppSysSurveyTaskScore;
import com.cmit.testing.entity.vo.SurveyTaskScoreItemVO;
import com.cmit.testing.entity.vo.SurveyTaskScoreVO;
import com.cmit.testing.service.app.AppSurveyTaskScoreService;
import com.cmit.testing.service.impl.BaseServiceImpl;
import com.cmit.testing.utils.SurveyTaskScoreTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxiaofang
 * @date 2018/10/25
 */
@Service("appSurveyTaskScoreService")
public class AppSurveyTaskScoreServiceImpl extends BaseServiceImpl<SysSurveyTaskScore> implements AppSurveyTaskScoreService {
    @Autowired
    private AppSurveyTaskScoreMapper appSurveyTaskScoreMapper;

    public static Logger logger = LoggerFactory.getLogger(AppSurveyTaskScoreServiceImpl.class);

    @Override
    public List<SurveyTaskScoreVO> list(Integer surveyTaskId, Integer testcaseId)
    {
        List<SurveyTaskScoreVO> list = new ArrayList<>();
        List<AppSysSurveyTaskScore> scores = appSurveyTaskScoreMapper.list(surveyTaskId, testcaseId);

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
    public boolean save(List<SurveyTaskScoreVO> scoreVOList, Integer surveyTaskId, Integer testcaseId) throws Exception
    {
        List<SurveyTaskScoreVO> collect = scoreVOList.stream().filter(a -> SurveyTaskScoreTypeEnum.getSurveyTaskScoreTypeEnumCode(a.getType()) == null).collect(Collectors.toList());
        if (collect.size() > 0)
        {
            return false;
        }

        try
        {
            //删除原评分
            appSurveyTaskScoreMapper.deleteBySurveyTask(surveyTaskId, testcaseId);
            for (SurveyTaskScoreVO taskScoreVO : scoreVOList)
            {
                String type = taskScoreVO.getType();
                for (SurveyTaskScoreItemVO item : taskScoreVO.getItems())
                {
                    AppSysSurveyTaskScore record = new AppSysSurveyTaskScore();
                    BeanUtils.copyProperties(item, record);
                    record.setSurveyTaskId(surveyTaskId);
                    record.setTestcaseId(testcaseId);
                    record.setType(type);
                    appSurveyTaskScoreMapper.insert(record);
                }
            }
            return true;
        }
        catch (BeansException e)
        {
            logger.error("保存评分失败！", e);
            throw new Exception();
        }
    }
}
