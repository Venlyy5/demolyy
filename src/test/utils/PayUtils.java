package com.xuecheng.order.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PayUtils {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private WXPayConfigImpl payConfig;

    //构造二维码
    public String createOrder(String orderNo, Long totalPay, String desc){
        Map<String, String> data = new HashMap<>();
        // 商品描述
        data.put("body", desc);
        // 订单号
        data.put("out_trade_no", orderNo);
        // 金额，单位是分
        data.put("total_fee", totalPay.toString());
        // 调用微信支付的终端IP
        data.put("spbill_create_ip", "127.0.0.1");
        // 回调地址
        data.put("notify_url", payConfig.getNotifyUrl());
        // 交易类型为扫码支付
        data.put("trade_type", payConfig.getPayType());

        // 利用wxPay工具,完成下单
        Map<String, String> result = null;
        try {
            result = wxPay.unifiedOrder(data);
        } catch (Exception e) {
            log.error("【微信下单】创建预交易订单异常失败", e);
            throw new RuntimeException("微信下单失败", e);
        }
        // 校验业务状态, 失败抛异常
        checkResultCode(result);

        // 下单成功，获取支付链接
        String url = result.get("code_url");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("微信下单失败，支付链接为空");
        }
        return url;
    }

    public void checkResultCode(Map<String, String> result) {
        // 检查业务状态,如果result_code=FAIL,抛异常
        String resultCode = result.get("result_code");
        if ("FAIL".equals(resultCode)) {
            log.error("【微信支付】微信支付业务失败，错误码：{}，原因：{}", result.get("err_code"), result.get("err_code_des"));
            throw new RuntimeException("【微信支付】微信支付业务失败");
        }
    }


    // 根据订单编号 校验支付状态, 返回true表示交易成功
    public Boolean checkPayment(String orderNo) {
        Map map = new HashMap();
        map.put("out_trade_no", orderNo);
        try {
            Map result = wxPay.orderQuery(map);
            //比对trade_state的值,SUCCESS表示交易成功
            return "SUCCESS".equals(result.get("trade_state"));
        } catch (Exception e) {
            log.error("【微信查询】查询微信支付状态失败", e);
            return false;
        }
    }


    // 关闭订单
    public Boolean closeOrder(String orderNo) {
        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", orderNo);
        try {
            Map<String, String> resultMap = wxPay.closeOrder(data);
            // 比对result_code, SUCCESS表示关闭结果成功
            return "SUCCESS".equals(resultMap.get("result_code"));
        } catch (Exception e) {
            log.error("【微信查询】查询微信支付状态失败", e);
            return false;
        }
    }
}










