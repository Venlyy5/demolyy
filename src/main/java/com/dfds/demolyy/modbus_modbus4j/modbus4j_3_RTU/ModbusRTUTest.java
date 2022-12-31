package com.dfds.demolyy.modbus_modbus4j.modbus4j_3_RTU;

import java.util.Arrays;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import jssc.SerialPort;

/**
 * 通过串口解析MODBUS协议
 * 上位机接口使用java的modbus4j实现
 * modbus4j的SerialPortWrapper接口没有实现类，推荐使用Freedomotic中的三个类实现：
 * SerialPortWrapperImpl.java
 * SerialInputStream.java
 * SerialOutputStream.java
 */
public class ModbusRTUTest {

    public static void main(String[] args) {
        SerialPortWrapper serialParameters = new SerialPortWrapperImpl(
                "COM1",
                SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE,
                SerialPort.FLOWCONTROL_NONE,
                SerialPort.FLOWCONTROL_NONE);

        //创建ModbusFactory工厂实例
        ModbusFactory modbusFactory = new ModbusFactory();
        //创建ModbusMaster实例
        ModbusMaster master = modbusFactory.createRtuMaster(serialParameters);

        try {
            // 初始化
            master.init();
            // 03功能 整形写测试
            writeRegisters(master, 1,0, new short[]{3,42,5,32,1,44,24,33,99,888});
            // 03功能 整形读测试
            //readHoldingRegistersTest(master, 1, 0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            master.destroy();
        }
    }

    /**
     * 读保持寄存器上的内容
     * @param master 主站
     * @param slaveId 从站ID
     * @param start 起始地址的偏移量
     * @param len 待读寄存器的个数
     */
    private static void readHoldingRegistersTest(ModbusMaster master, int slaveId, int start, int len) {
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)master.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            } else {
                System.out.println(Arrays.toString(response.getShortData()));
                short[] list = response.getShortData();
                for (int i = 0; i < list.length; i++) {
                    System.out.print(list[i] + " ");
                }
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入保持寄存器
     * @param master 主站
     * @param slaveId 从站ID
     * @param startOffset 起始地址的偏移量
     * @param data 写入的数据
     */
    public static boolean writeRegisters(ModbusMaster master, int slaveId, int startOffset, short[] data) throws ModbusTransportException {
        // 创建请求对象
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, data);
        // 发送请求并获取响应对象
        ModbusResponse response = master.send(request);
        if (response.isException()) {
            System.out.println(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }
}