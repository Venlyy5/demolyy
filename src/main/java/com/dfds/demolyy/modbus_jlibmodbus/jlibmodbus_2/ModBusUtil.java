package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_2;
import cn.hutool.core.util.HexUtil;

public class ModBusUtil {
    public static String getModbusValue(String ip, int port, int slaveId, int offset, int quantity) {
        String requestString = "";
        try {
            //是否连接成功
            if (!ModBusConnection.getModbusMaster().isConnected()) {
                ModBusConnection.getModbusMaster().connect();// 开启连接
            }
            // 功能码04 读取输入寄存器
            int[] registerValues = ModBusConnection.getModbusMaster().readInputRegisters(slaveId, offset, quantity);

            for (int value : registerValues) {
                // 控制台输出每个寄存器的int值
                System.out.println("Address: " + offset++ + ", Value: " + value);
                // int转16进制,并拼接
                requestString += HexUtil.toUnicodeHex(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回所有寄存器16进制
        return requestString.replaceAll("\\\\u"," ").toUpperCase();
    }

    public static void main(String[] args) {
        //jlibmodbus测试: 功能码04 读输入寄存器.
        String modbusValue = ModBusUtil.getModbusValue("127.0.0.1", 502, 1, 0, 10);
        System.out.println("Add1~Add9 HEX:"+ modbusValue);
    }
}