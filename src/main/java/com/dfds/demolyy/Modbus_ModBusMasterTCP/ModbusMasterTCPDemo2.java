package com.dfds.demolyy.Modbus_ModBusMasterTCP;

import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import com.digitalpetri.modbus.FunctionCode;
import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.*;
import com.digitalpetri.modbus.responses.*;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @version: V1.0
 * @description: modbus TCP协议 数据读取写入
 * @date: 2021-02-04
 */
@Slf4j
public class ModbusMasterTCPDemo2 {
    /**
     * tcp连接对象
     */
    private static ModbusTcpMaster modbusTcpMaster;
    /**
     * modbus ip地址
     */
    private static final String IP = "192.168.19.130";
    /**
     * 端口
     */
    private static final Integer PORT = 502;
    /**
     * modubs从站ID
     */
    private static final Integer UNIT_ID = 1;
    /**
     * 成功代码
     */
    private static final String SUCCESS_CODE = "0x000000";
    /**
     * 与modubs连接异常
     */
    private static final String COON_FAIL_CODE = "0x000001";
    /**
     * 向modubs发送命令执行异常
     */
    private static final String EXEC_FAIL_CODE = "0x000002";

    /**
     * 数据写入失败
     */
    private static final String WRITE_FAIL_CODE = "0x000004";
    private static String logName = "ModbusMasterTCPFromZhengMei ";
    /**
     * @description: 初始化连接
     * @param:
     * @return: 结果值
     */
    private static String init() {
        try {
            if (modbusTcpMaster == null) {
                ModbusTcpMasterConfig config = new ModbusTcpMasterConfig.Builder(IP).setPort(PORT).build();
                modbusTcpMaster = new ModbusTcpMaster(config);
            }
            return SUCCESS_CODE;
        } catch (Exception e) {
            log.error("ModbusMasterTCP::init - " + e.getMessage() + "(0x000001)" + "\r\n" + Arrays.toString(e.getStackTrace()));
            return COON_FAIL_CODE;
        }
    }

    /**
     * @description: 释放连接
     * @param:
     * @return: 结果值
     */
    private static String release() {
        try {
            if (modbusTcpMaster != null) {
                modbusTcpMaster.disconnect();
            }
            Modbus.releaseSharedResources();
            return SUCCESS_CODE;
        } catch (Exception e) {
            return COON_FAIL_CODE;
        }
    }

    /**
     * @param address 寄存器地址
     * @param value   写入值
     * @param unitId  id
     * @description: 单个写 HoldingRegister数据
     * @return: 结果值
     */
    public static String writeHoldingRegisters(Integer address, Integer value, Integer unitId) {
        ModbusResponse modbusResponse;
        try {
            // 发送单个寄存器数据，一般是无符号16位值：比如10
            CompletableFuture<ModbusResponse> future = modbusTcpMaster.sendRequest(new WriteSingleRegisterRequest(address, value), unitId);

            //获取写入的响应流
            modbusResponse = future.get();
            if (modbusResponse == null) {
                System.out.println("FCSC-ExternalConnection WriteHoldingRegisters：modbusResponse is null ");
                return WRITE_FAIL_CODE;
            }
            //获取写入的响应FunctionCode
            FunctionCode functionCode = modbusResponse.getFunctionCode();
            System.out.println("FCSC-ExternalConnection functionCode=" + functionCode + " value=" + value);
            if (functionCode == FunctionCode.WriteSingleRegister) {
                return SUCCESS_CODE;
            } else {
                return WRITE_FAIL_CODE;
            }
        } catch (Exception e) {
            log.error("ModbusMasterTCP::writeHoldingRegisters - " + e.getMessage() + ",value=" + value + "(0x000002)" + "\r\n" + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return EXEC_FAIL_CODE;
        } finally {
            // String releaseRes = release();
            // //如果释放连接失败，返回执行失败
            // if (!SUCCESS_CODE.equals(releaseRes)) {
            //     return releaseRes;
            // }
        }
    }

    /**
     * @param address  寄存器地址
     * @param quantity 写位数
     * @param values   写入值
     * @description: 批量写 HoldingRegister数据
     * @return: 结果值
     */
    public static String WriteMultipleRegisters(Integer address, Integer quantity, byte[] values) {
        try {
            WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(address, quantity, values);
            // 发送单个寄存器数据，一般是无符号16位值：比如10
            CompletableFuture<ModbusResponse> future = modbusTcpMaster.sendRequest(request, UNIT_ID);
            ModbusResponse modbusResponse;
            //获取写入的响应流
            modbusResponse = future.get();
            if (modbusResponse == null) {
                System.out.println("FCSC-ExternalConnection WriteMultipleRegisters：modbusResponse is null ");
                return WRITE_FAIL_CODE;
            }
            //获取写入的响应FunctionCode
            FunctionCode functionCode = modbusResponse.getFunctionCode();
            System.out.println("FCSC-ExternalConnection functionCode.getCode()===" + functionCode.getCode() + "=" + functionCode);
            if (functionCode == FunctionCode.WriteMultipleRegisters) {
                return SUCCESS_CODE;
            } else {
                return WRITE_FAIL_CODE;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return EXEC_FAIL_CODE;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return EXEC_FAIL_CODE;
        } finally {
            // String releaseRes = release();
            // //如果释放连接失败，返回执行失败
            // if (!SUCCESS_CODE.equals(releaseRes)) {
            //     return releaseRes;
            // }
        }
    }

    /**
     * @description: 批量写 HoldingRegister
     * @param: address 寄存器地址
     * @param: value 写入值
     * @return: 结果值
     */
    public static String writeByteData(byte[] values) {
        String initRes = init();
        //如果初始化失败，则立即返回
        if (!SUCCESS_CODE.equals(initRes)) {
            return initRes;
        }
        String writeRes = WriteMultipleRegisters(0, 2, values);
        //如果写入失败，返回
        if (!SUCCESS_CODE.equals(writeRes)) {
            return writeRes;
        }
        return SUCCESS_CODE;
    }

    /**
     * @description: 单个写 HoldingRegister
     * @param: address 寄存器地址
     * @param: value 写入值
     * @return: 结果值
     */
    public static String writeData(Integer address, Integer value) {
        String initRes = init();
        //如果初始化失败，则立即返回
        if (!SUCCESS_CODE.equals(initRes)) {
            return initRes;
        }

        String writeRes = writeHoldingRegisters(address, value, UNIT_ID);
        //如果写入失败，返回
        if (!SUCCESS_CODE.equals(writeRes)) {
            return writeRes;
        }
        return SUCCESS_CODE;
    }


    /**
     * 测试 - 单个写 HoldingRegister数据
     */
    public static void writeDemo() {
        // 初始化资源
        init();
        Random random = new Random();
        int value = random.nextInt(100) + 1;
        System.out.println("write value=" + value);
        try {
            writeHoldingRegisters(222, value, UNIT_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 释放资源
        release();
    }

    /**
     * 测试 - 读取HoldingRegister
     */
    public static void readDemo() {
        try {
            // 初始化资源
            init();
            System.out.println("readDemo=" + readHoldingRegisters(222, 4, 1));
            release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读单个 Coils开关量
     * @param address  寄存器开始地址
     * @param quantity 数量
     * @param unitId   ID
     * @return 读取值
     */
    public static Boolean readCoils(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadCoilsResponse> future = modbusTcpMaster.sendRequest(new ReadCoilsRequest(address, quantity), unitId);
        ReadCoilsResponse readCoilsResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readCoilsResponse != null) {
            ByteBuf buf = readCoilsResponse.getCoilStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(readCoilsResponse);
        }
        return result;
    }

    /**
     * 读单个 readDiscreteInputs开关量
     * @param address  寄存器开始地址
     * @param quantity 数量
     * @param unitId   ID
     */
    public static Boolean readDiscreteInputs(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadDiscreteInputsResponse> future = modbusTcpMaster.sendRequest(new ReadDiscreteInputsRequest(address, quantity), unitId);
        ReadDiscreteInputsResponse discreteInputsResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (discreteInputsResponse != null) {
            ByteBuf buf = discreteInputsResponse.getInputStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(discreteInputsResponse);
        }
        return result;
    }

    /**
     * 读单个 HoldingRegister数据
     * @param address  寄存器地址
     * @param quantity 寄存器数量
     * @param unitId   id
     * @return 读取结果
     */
    public static Number readHoldingRegisters(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Number result = null;
        CompletableFuture<ReadHoldingRegistersResponse> future = modbusTcpMaster.sendRequest(new ReadHoldingRegistersRequest(address, quantity), unitId);
        ReadHoldingRegistersResponse readHoldingRegistersResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readHoldingRegistersResponse != null) {
            ByteBuf buf = readHoldingRegistersResponse.getRegisters();
            byte[] bytes = new byte[buf.capacity()];
            buf.readBytes(bytes, 0, buf.capacity());
            // System.out.println("bytes=" + Arrays.toString(bytes));
            result = HexUtils.bytes2ShortBE(bytes);
            ReferenceCountUtil.release(readHoldingRegistersResponse);
        }
        return result;
    }

    /**
     * 读取单个 int值
     * @param address
     * @param quantity
     */
    public static Integer readData(Integer address, Integer quantity) throws Exception {
        String initRes = init();
        //如果初始化失败，则立即返回
        if (!SUCCESS_CODE.equals(initRes)) {
            return -5;
        }
        Number number = readHoldingRegisters(address, quantity, UNIT_ID);
        return number.intValue();
    }

    /**
     * 读单个 InputRegisters模拟量数据
     * @param address  寄存器开始地址
     * @param quantity 数量
     * @param unitId   ID
     * @return 读取值
     */
    public static Number readInputRegisters(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Number result = null;
        CompletableFuture<ReadInputRegistersResponse> future = modbusTcpMaster.sendRequest(new ReadInputRegistersRequest(address, quantity), unitId);
        ReadInputRegistersResponse readInputRegistersResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readInputRegistersResponse != null) {
            ByteBuf buf = readInputRegistersResponse.getRegisters();
            result = buf.readDouble();
            ReferenceCountUtil.release(readInputRegistersResponse);
        }
        return result;
    }

    /**
     * 测试 - 单个写
     */
    public static void writeDemo2() {
        Random random = new Random();
        int value = random.nextInt(100) + 1;
        System.out.println("ready write value=" + value);
        String res = writeData(222, value);
        System.out.println("res=" + res);
    }

    /**
     * 测试 - 批量写 HoldingRegister
     */
    public static void writeDemo3() {
        byte[] bytes = new byte[]{0, 2, 0, 3};
        String res = writeByteData(bytes);
        System.out.println(res);
    }

    /**
     * 写单个 Coil
     * @param address
     * @param value
     * @return
     */
    public static String writeCoils(int address, boolean value) {
        try {
            init();
            WriteSingleCoilRequest writeSingleCoilRequest = new WriteSingleCoilRequest(address, value);
            CompletableFuture<ModbusResponse> request = modbusTcpMaster.sendRequest(writeSingleCoilRequest, UNIT_ID);
            ModbusResponse modbusResponse = request.get();
            if (modbusResponse == null) {
                System.out.println(logName + "writeCoils：modbusResponse is null ");
                return WRITE_FAIL_CODE;
            }
            FunctionCode functionCode = modbusResponse.getFunctionCode();
            System.out.println(logName + "writeCoils address=" + address + " value=" + value + " functionCode=" + functionCode);
            if (functionCode == FunctionCode.WriteSingleCoil) {
                return SUCCESS_CODE;
            } else {
                return WRITE_FAIL_CODE;
            }
        } catch (Exception e) {
            log.error(logName + "writeCoils - " + e.getMessage() + ",address" + address + ",value=" + value + "(0x000002)"
                    + "\r\n" + Arrays.toString(e.getStackTrace()));
            System.out.println(logName + "writeCoils - " + e.getMessage());
            return WRITE_FAIL_CODE;
        }
    }

    public static void main(String[] args) {
        // writeDemo();
        // readDemo();
        writeDemo3();
        release();
    }
}
