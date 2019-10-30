package com.tan.eth.utils;


import java.io.Serializable;



/**
 * 统一结果返回
 * @author lqx
 *
 */
public class ResultEntity<T> implements Serializable{
		
	// 状态码  分   false [异常状态码] 和 true[业务状态码]
	private int code = 200;
	
	//返回的主题信息
	private String message;
	
	//具体 返回的 主体
	private T t;
	
	//是否抛出异常  默认是正常运行的  抛出异常为 false
	private boolean success = true;

	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public ResultEntity() {}
	
	protected ResultEntity(int code, boolean success, String message, T t) {
		this.code = code;
		this.success = success;
		this.message = message;
		this.t = t;
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 */
	public static <T> ResultEntity<T> success(T data) {
		return new ResultEntity<T>(ResultCode.SUCCESS.getCode(),true, ResultCode.SUCCESS.getMessage(), data);
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 * @param  message 提示信息
	 */
	public static <T> ResultEntity<T> success(T data, String message) {
		return new ResultEntity<T>(ResultCode.SUCCESS.getCode(),true, message, data);
	}


	/**
	 * 失败返回结果
	 * @param message 提示信息
	 */
	public static <T> ResultEntity<T> failed(String message) {
		return new ResultEntity<T>(ResultCode.FAILED.getCode(),false,  message, null);
	}






}
