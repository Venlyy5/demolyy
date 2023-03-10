package com.dfds.demolyy.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常码枚举
 *
 * @author xingshuang
 */
public enum EMbExceptionCode {

    /**
     * 非法功能码
     */
    ILLEGAL_FUNCTION((byte) 0x01, "非法功能码"),

    /**
     * 非法数据地址
     */
    ILLEGAL_DATA_ADDRESS((byte) 0x02, "非法数据地址"),

    /**
     * 非法数据值
     */
    ILLEGAL_DATA_VALUE((byte) 0x03, "非法数据值"),

    /**
     * 从站设备失败
     */
    SLAVE_DEVICE_FAILURE((byte) 0x04, "从站设备失败"),

    ;

    private static final Map<Byte, EMbExceptionCode> map = new HashMap<>();

    static {
        for (EMbExceptionCode item : EMbExceptionCode.values()) {
            map.put(item.code, item);
        }
    }

    public static EMbExceptionCode from(byte data) {
        return map.get(data);
    }

    private byte code;

    private String description;

    EMbExceptionCode(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EMbExceptionCode getByCode(byte code){
        return Arrays.stream(EMbExceptionCode.values()).filter(e -> e.code==code).findFirst().orElse(null);
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static void main(String[] args) {
        EMbExceptionCode byCode = getByCode((byte) 0x03);
        System.out.println(byCode);
    }

}