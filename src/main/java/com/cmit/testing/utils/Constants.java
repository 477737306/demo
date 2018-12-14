package com.cmit.testing.utils;

/**
 * 系统常量类
 * @author XXM
 * 2018-08-07
 */
public class Constants {
	
	public final static String EMPTY_STRING = "";
	public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String YYYYMM = "yyyy-MM";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String ENCODEING = "UTF-8";
    public static final String ZIP_NAME=".zip";
    
    public static final Integer SUCCESS_CODE  = 200;
    public static final Integer FAILD_CODE    = 300;
    public static final String SUCCESS_INSERT_MSG  = "添加成功";
    public static final String SUCCESS_UPDATE_MSG  = "修改成功";
    public static final String SUCCESS_DELETE_MSG  = "删除成功";
    public static final String SUCCESS_OPERATION_MSG  = "操作成功";
    public static final String FAILD_OPERATION_MSG  = "操作失败";
    
    public static final String PIC_PATH = "E:\\images";
    public static final String VIDEO_PATH = "E:\\video";

    /** 模板 */
    public static final String TEMPLATE_PATH= "jxls_template/"; //导出模板路径

    /**执行类型：0-立即执行，1-指定时间执行，2-指定周期执行，3-不执行**/
    public static final Integer EXECUTE_TYPE_IMMEDIATE = 0;
    public static final Integer EXECUTE_TYPE_TIMING = 1;
    public static final Integer EXECUTE_TYPE_CYCLE = 2;
    public static final Integer EXECUTE_TYPE_DOTNOT = 3;
    /**
     * 执行状态
     *
     * 0-未执行，1-执行中，2-执行完成
     **/
    public static final Integer EXECUTE_STATUS_NODO = 0;
    public static final Integer EXECUTE_STATUS_DOING = 1;
    public static final Integer EXECUTE_STATUS_DONE = 2;
    /**
     * 通过状态
     *
     * 0-未通过；1-通过；2-未执行
     */
    public static final Integer PASS_STATUS_FAILED = 0;
    public static final Integer PASS_STATUS_SUCCESS = 1;
    public static final Integer PASS_STATUS_NODO = 2;
    /**
     * 用例是否下发消息
     *
     * 0-未下发；1-已下发；2-已收到消息
     */
    public static final Integer IS_PUSH_NODO = 0;
    public static final Integer IS_PUSH_SEND = 1;
    public static final Integer IS_PUSH_RECEIVE = 2;
    /**
     * 设备使用状态
     *
     * 0-空闲；1-占用；2-预占用
     */
    public static final Integer USE_STATUS_FREE = 0;
    public static final Integer USE_STATUS_OCCUPY = 1;
    public static final Integer USE_STATUS_PREOCCUPANCY = 2;
    /**
     * 设备在线状态
     *
     * 0-离线；1-在线
     */
    public static final Integer ONLINE_STATUS = 0;
    public static final Integer OFFLINE_STATUS = 1;
    
    /**
     * 用例执行结果
     * 0-成功      1-失败
     */
    public static final Integer EXCUTE_RESULT_SUCCESS = 0;
    public static final Integer EXCUTE_RESULT_FAILED = 1;
    
    /**
     * 用例通过状态
     * 0-通过      1-不通过
     */
    public static final Integer PASS_STATE_SUCCESS = 0;
    public static final Integer PASS_STATE_FAILED = 1;
    
    /**
     * 对外token鉴权
     */
    public static final String FOREIGN_PERMISSION_KEY ="4AAQSkZJRgABAgAAAQABAAD"; //鉴权key值
    public static final Long FAILURE_TIME = 24*60*60*1000L; //鉴权失效时间,单位毫秒

    /**
     * 定时任务常量
     */
    public static final String WEB_JOB_NAME ="WEB_JOB_NAME"; //web任务名前缀
    public static final String WEB_JOB_GROUP_NAME ="WEB_JOB_GROUP_NAME"; //web任务组名
    public static final String WEB_TRIGGER_NAME = "WEB_TRIGGER_NAME"; //web触发器名
    public static final String WEB_TRIGGER_GROUP_NAME = "WEB_TRIGGER_GROUP_NAME"; //web触发器组名

    public static final String APP_JOB_NAME ="APP_JOB_NAME"; //app任务名前缀
    public static final String APP_JOB_GROUP_NAME ="APP_JOB_GROUP_NAME"; //app任务组名
    public static final String APP_TRIGGER_NAME = "APP_TRIGGER_NAME"; //app触发器名
    public static final String APP_TRIGGER_GROUP_NAME = "APP_TRIGGER_GROUP_NAME"; //app触发器组名


    public final static String ORIGIN_KEY = "origin"; //跨域白名单Key值

}
