package com.cmit.testing.utils;

import java.io.Serializable;

public class JsonResultUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status=200;
	private String message="操作成功";
	private Object data;
	
	public JsonResultUtil() {}
	public JsonResultUtil(int status, String message) {
		this.status=status;
		this.message=message;
	}

	public JsonResultUtil(Object data) {
		this.data=data;
	}

	public JsonResultUtil(int status, String message, Object data) {
		this.status=status;
		this.message=message;
		this.data=data;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toJson() {
		return JsonUtil.toJson(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("{").append(status).append(",").append(message).append("}");
		return sb.toString();
	}
}
