package com.cmit.testing.service;

/**
 * 清理FastDFS
 */

public interface FastDFSClearService {

    /**
     * fastDFS的截图视频清理
     * @return
     */
    void ClearRubbish(String time);
    /**
     * fastDFS的截图视频清理
     * @return
     */
    void ClearAppRubbish(String time);
}
