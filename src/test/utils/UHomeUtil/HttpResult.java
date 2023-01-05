package com.uhome.common.utils;

import org.apache.http.HttpStatus;

/**
 * 返回数据
 */
public class HttpResult {
    private int code;
    private String msg;
    private Object data;

    public HttpResult() {
    }

    public HttpResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static HttpResult error() {
        return error("未知异常，请联系管理员");
    }

    public static HttpResult error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static HttpResult error(int code, String msg) {
        HttpResult result = new HttpResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static HttpResult ok() {
        return ok(HttpStatus.SC_OK, "success");
    }

    public static HttpResult ok(String msg) {
        return ok(HttpStatus.SC_OK, msg);
    }

    public static HttpResult ok(int code, String msg) {
        HttpResult result = new HttpResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public HttpResult setData(Object data) {
        this.data = data;
        return this;
    }
}
