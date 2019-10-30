package com.tan.eth.utils;

/**
 * @author by Tan
 * @create 2019/8/26/026
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(-1, "操作失败"),
	UNAUTHORIZED(401, "暂未登录"),
	USER_TOKEN_INVALID(403,"用户凭证失效,请返回登录"),
	USER_TOKEN_NO(402,"还未登录,请先登录"),
	SERVICE_FAIL(400, "服务调用超时"),
	FUND_USER_NOT_INVALIDATE(3006, "用户基金账户校验失败"),
	PRODUCT_QUERY_INVALID(901,"商品查询失败");

    private int code;
    private String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
