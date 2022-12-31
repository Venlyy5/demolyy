package com.dfds.demolyy.Modbus_ModBusMasterTCP;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.*;
import com.digitalpetri.modbus.responses.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;


/***
 * modbus TCP协议Java通讯读取
 */
public class ModbusMasterTCPDemo {
    static ModbusTcpMaster master;
 
    /**
     * 获取TCP协议的Master
     */
    public static void initModbusTcpMaster() {
        if (master == null) {
            ModbusTcpMasterConfig config = new ModbusTcpMasterConfig.Builder("localhost").setPort(502).build();
            master = new ModbusTcpMaster(config);
        }
    }
 
    /***
     * 释放资源
     */
    public static void release() {
        if (master != null) {
            master.disconnect();
        }
        Modbus.releaseSharedResources();
    }
 
    /**
     * 读取 01-Coils
     * @param address 寄存器开始地址
     * @param quantity 数量
     */
    public static Boolean readCoils(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadCoilsResponse> future = master.sendRequest(new ReadCoilsRequest(address, quantity), unitId);
        ReadCoilsResponse readCoilsResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readCoilsResponse != null) {
            ByteBuf buf = readCoilsResponse.getCoilStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(readCoilsResponse);
        }
        return result;
    }
 
    /**
     * 读取 02-DiscreteInputs
     * @param address 寄存器开始地址
     * @param quantity 数量
     */
    public static Boolean readDiscreteInputs(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadDiscreteInputsResponse> future = master.sendRequest(new ReadDiscreteInputsRequest(address, quantity), unitId);
        ReadDiscreteInputsResponse discreteInputsResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (discreteInputsResponse != null) {
            ByteBuf buf = discreteInputsResponse.getInputStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(discreteInputsResponse);
        }
        return result;
    }
 
    /**
     * 读取 03-HoldingRegister
     * @param address 寄存器地址
     * @param quantity 寄存器数量
     */
    public static Number readHoldingRegisters(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Number result = null;
        CompletableFuture<ReadHoldingRegistersResponse> future = master.sendRequest(new ReadHoldingRegistersRequest(address, quantity), unitId);
        ReadHoldingRegistersResponse readHoldingRegistersResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readHoldingRegistersResponse != null) {
            ByteBuf buf = readHoldingRegistersResponse.getRegisters();
            result = buf.readFloat();
            ReferenceCountUtil.release(readHoldingRegistersResponse);
        }
        return result;
    }
 
    /**
     * 读取 04-InputRegisters
     * @param address 寄存器开始地址
     * @param quantity 数量
     */
    public static Number readInputRegisters(int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Number result = null;
        CompletableFuture<ReadInputRegistersResponse> future = master.sendRequest(new ReadInputRegistersRequest(address, quantity), unitId);
        ReadInputRegistersResponse readInputRegistersResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (readInputRegistersResponse != null) {
            ByteBuf buf = readInputRegistersResponse.getRegisters();
            result = buf.readDouble();
            ReferenceCountUtil.release(readInputRegistersResponse);
        }
        return result;
    }


    /**
     * 06-Write Single Register
     */
    public static boolean WriteSingleRegister(int address, int unitId, int value){
        // 发送单个寄存器数据，一般是uint16
        master.sendRequest(new WriteSingleRegisterRequest(address, value), unitId);
        return true;
    }

    /**
     * 10-Write Multiple Registers
     */
    public static boolean WriteMultipleRegisters(int address, int quantity, int unitId, float values){
        // float类型转字节数组
        byte[] bytes = HexUtils.float2bytes(values);
        // 转netty需要的字节类型
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        // 写多个寄存器数据，数据类型由quantity（modbus地址的数量）决定，1个modbus地址是16位即2字节，float类型占4个字节即2个数量的modbus地址，double类型占8个字节即4个数量的modbus地址
        master.sendRequest(new WriteMultipleRegistersRequest(address, quantity, byteBuf), unitId);
        //ReadWriteMultipleRegistersRequest readWriteMultipleRegistersRequest = new ReadWriteMultipleRegistersRequest();
        return true;
    }



    public static void main(String[] args) {
        try {
            // 初始化资源
            initModbusTcpMaster();

            // 读取1个开关量
            System.out.println("功能码=1,读取地址0,读1个,unitId=1: "+ readCoils(0, 1, 1));
            // 读取1个开关量
            System.out.println("功能码=2,读取地址0,读1个,unitId=1: "+ readDiscreteInputs(0, 1, 1));
            // 读取4个开关量,从1~4 有一个为true则为true
            System.out.println("功能码=2,读取地址1,读4个,unitId=1: "+ readDiscreteInputs(1, 4, 1));
 
            // 读取模拟量  - Float(AB CD EF GH)
            //System.out.println("功能码=3,读取地址0,读2个,unitId=1: "+ readHoldingRegisters(0, 2, 1));
            //System.out.println("功能码=3,读取地址6,读2个,unitId=1: "+ readHoldingRegisters(6, 2, 1));
            //System.out.println("功能码=3,读取地址8,读2个,unitId=1: "+ readHoldingRegisters(8, 2, 1));

            // 读取模拟量  - Double(AB CD EF GH)
            System.out.println("功能码=4,读取地址0,读4个,unitId=1: "+ readInputRegisters(0, 4, 1));
            System.out.println("功能码=4,读取地址4,读4个,unitId=1: "+ readInputRegisters(4, 4, 1));

            // 单独写int: 功能码=3,写入地址address,从站unitId,写入值value
            WriteSingleRegister(3,1,79);

            // 写多个寄存器, 功能码=3
            WriteMultipleRegisters(4,2,1,11.21f);
 
            // 释放资源
            release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}