package com.cmit.testing.enums;

public enum ResultEnum {

	SUCCESS(200, "成功"),
	TESTCASE_PHONE_NOT_EXIST(01, "用例执行手机号不存在"),
	PHONE_DISABLE_STATUS_CLOSED(02, "手机号启用状态为已关闭"),
	;

	private Integer code;
	private String message;

	ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
