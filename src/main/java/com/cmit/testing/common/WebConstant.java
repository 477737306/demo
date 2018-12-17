package com.cmit.testing.common;

/**
 * @author ChenXinJin
 * @date 2018/12/17 03:08
 */
public class WebConstant {
	/**
	 * web用例执行状态：0-执行完成，1-执行中，2-未执行，3-执行被终止，4-暂停
	 */
	public class TestCaseExecuteStatus {
		public final static int COMPLETED = 0;
		public final static int EXECUTING = 1;
		public final static int UNEXECUTED = 2;
		public final static int TERMINATED = 3;
		public final static int SUSPEND = 4;
	}

	/**
	 * web用例任务执行类型：0-单个用例执行，1-批量执行，2-众测任务执行，3-业务执行
	 */
	public class TestCaseTaskType {
		public final static int SINGLE_CASE = 0;
		public final static int SYS_TASK = 1;
		public final static int SYS_SURVEY_TASK = 2;
		public final static int BUSINESS = 3;
	}
}
