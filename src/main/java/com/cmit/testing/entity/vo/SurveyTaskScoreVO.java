package com.cmit.testing.entity.vo;

import com.cmit.testing.utils.SurveyTaskScoreTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author weiBin
 * @date 2018/9/19
 */
public class SurveyTaskScoreVO {

    /**
     * 类型名称
     */
    @JsonProperty("name")
    private String typeName;
    /**
     * 类型
     */
    @JsonProperty("type")
    private String type;
    /**
     * 列表
     */
    @JsonProperty("items")
    private List<SurveyTaskScoreItemVO> items;

    public SurveyTaskScoreVO(SurveyTaskScoreTypeEnum typeEnum, List<SurveyTaskScoreItemVO> items) {
        this.typeName = typeEnum.getMessage();
        this.type = typeEnum.getCode();
        this.items = items;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SurveyTaskScoreItemVO> getItems() {
        return items;
    }

    public void setItems(List<SurveyTaskScoreItemVO> items) {
        this.items = items;
    }
}
