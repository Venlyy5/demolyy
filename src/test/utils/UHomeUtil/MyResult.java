package com.uhome.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author FYX day-activity tools 2F
 */
@Data
@ApiModel
public class MyResult<T> {

    @ApiModelProperty(value = "响应编码", position = 1)
    private Integer code; // 响应编码

    @ApiModelProperty(value = "响应消息", position = 2)
    private String message; // 响应消息

    @ApiModelProperty(value = "响应信息", position = 3)
    private T data; // 响应信息

    @ApiModelProperty(value = "响应时间", position = 4)
    private Long respTime; // 响应时间

    @ApiModelProperty(value = "签名：不常用", position = 5)
    private String sign; // 签名

    public MyResult setHttpStatus(HttpStatus httpStatus){
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        return this;
    };
}
