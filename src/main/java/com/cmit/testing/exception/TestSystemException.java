package com.cmit.testing.exception;

import com.cmit.testing.enums.ResultEnum;

/**
 * 系统提示异常
 * 
 * @author YangWanLi
 * @date 2018/7/10 14:32
 */
public class TestSystemException extends RuntimeException {

	public TestSystemException() {
		super();
	}

	public TestSystemException(String message) {
		super(message);
	}

	private Integer code;

	public TestSystemException(ResultEnum resultEnum) {
		super(resultEnum.getMessage());
		this.setCode(resultEnum.getCode());
	}

	public TestSystemException(Integer code, String message) {
		super(message);
		this.setCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
