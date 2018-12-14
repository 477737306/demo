package com.cmit.testing.service;

import java.util.Map;

/**
 * 统计分析
 * @author YangWanLi
 * @date 2018/8/9 18:50
 */
public interface ProjectCountService {

    /**
     * 统计
     * @return
     */
   Map<String,Object> projectCount();
}
