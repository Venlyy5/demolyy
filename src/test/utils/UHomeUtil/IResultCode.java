package com.uhome.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author psy
 * @Date 2018/1/15 21:02
 **/
public enum IResultCode {
    Success(0, "操作成功"),
    Error(1, "失败"),
    ServerError(500, "服务端异常"),
    ParameterInvalid(201004, "参数无效"),
    ParameterFormat(201005, "参数格式错误"),
    CodeInvalid(201006, "验证码无效"),
    TokenInvalid(203001, "token无效"),
    LoginInvalid(203002, "登录无效"),
    DecryptFail(201003, "解密失败"),
    RequiredParamter(203003, "必填参数不能为空"),   //缺少必须的参数
    GoodsKindError(204003, "单品和品类冲突"),   //单品和品类冲突

    //具体业务代码
    RequestOvertime(301001, "请求已超时"),   //分布服务超时
    RequestInvalid(301002,  "请求已无效"),   //数据不一致，或已发生过改变
    LoginBindMobile(301003, "需绑定手机号"),   //用户下单必须绑定手机
    DateError(301004,"日期参数不准确"),  //日期不允许小于当前日期
    FileEmpty(301005,"文件不存在"),   //上传文件不存在
    MutexGoods(301007,"商品存在互斥"), //互斥的商品
    MutexStore(301008,"门店存在互斥"), //互斥的商品
    NameRepeat(301006,"活动名称重复");   //活动名称重复

    private final Integer key;
    private final String value;

    IResultCode(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static Map<Integer, IResultCode> map = new HashMap<Integer, IResultCode>();

    static {
        for (IResultCode iResultCode : IResultCode.values()) {
            map.put(iResultCode.key, iResultCode);
        }
    }

    public static String getValue(Integer key) {
        IResultCode iResultCode = map.get(key);
        if( null != iResultCode ) {
            return iResultCode.value;
        }
        return null;
    }
}
