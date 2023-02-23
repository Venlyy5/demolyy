package com.dfd.platform.utils.dfdbus;

import lombok.Data;

/**------------------
 * DFDBus报文实体类
 * @author LiYangYang
 * @date 2022/11/16
 */
@Data
public class DfdBusEntity {
    public final String HEAD = "AA55";
    private String dataLength = "";
    private String interactiveCommand = "";
    private String functionCode = "";

    private String deviceId = "";
    private String deviceToken = "";
    private String status = ""; //验证状态

    private String messageId = "";
    private String functionStartId = "";
    private String functionData = ""; // 功能点数据
    private String readDataLength = ""; //读取的数据长度

    private String crc = "";
    public final String END = "0D0A";

    /**----------
     * 拼接整体报文
     */
    protected String toHexString(){
        return HEAD
                + dataLength
                + interactiveCommand
                + functionCode
                + deviceId
                + deviceToken
                + status
                + messageId
                + functionStartId
                + functionData
                + readDataLength
                + crc
                + END;
    }

    /**------------------
     * 获取报文中需要计算CRC的部分
     */
    protected String getCRCContent(){
        return  interactiveCommand
                + functionCode
                + deviceId
                + deviceToken
                + status
                + messageId
                + functionStartId
                + functionData
                + readDataLength;
    }

    /**------------------
     * 获取报文中需要计算长度的部分
     */
    protected String getDataContent(){
        return  interactiveCommand
                + functionCode
                + deviceId
                + deviceToken
                + status
                + messageId
                + functionStartId
                + functionData
                + readDataLength
                + crc;
    }
}
