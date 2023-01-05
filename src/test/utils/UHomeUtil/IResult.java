package com.uhome.common.utils;

/**
 * @Author lgc
 * @Date 2018/1/12 9:15
 **/
public class IResult {

    private Integer code;

    private String message;

    private Object data;

    private Long respTime;

    private String sign;

    public IResult() {
        this.code = IResultCode.Error.getKey();
        this.message = IResultCode.Error.getValue();
    }

    public IResult(IResultCode resultCode) {
        this.code = resultCode.getKey();
        this.message = resultCode.getValue();
    }

    public IResult(Integer code) {
        this.code = code;
        this.message = IResultCode.getValue(code);
    }

    public IResult(Integer code, String message) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public IResult setCode(IResultCode resultCode) {
        this.code = resultCode.getKey();
        this.message = resultCode.getValue();
        return this;
    }

    public IResult setCode(Integer code) {
        this.code = code;
        this.message = IResultCode.getValue(code);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public IResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData()
    {
        if(data == null) {
            data = "";
        }
        return data;
    }

    public IResult setData(Object data) {
        this.data = data;
        return this;
    }

    public Long getRespTime() {
        return respTime;
    }

    public void setRespTime(Long respTime) {
        this.respTime = respTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
