package com.dfds.demolyy.modbus_modbus4j.modbus4j_1;

import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;

import java.util.Arrays;

/**
 * Modbus4j-ReadUtils
 * @dependencies modbus4j-3.0.3.jar
 * @website https://github.com/infiniteautomation/modbus4j
 */
public class Modbus4jReadUtils {
    static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * 获取master
     */
    public static ModbusMaster getMaster() throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(502);

        // modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
        // modbusFactory.createRtuMaster(wrapper);  //RTU 协议
        // modbusFactory.createUdpMaster(params);   //UDP 协议
        ModbusMaster master = modbusFactory.createTcpMaster(params, false);// TCP 协议
        master.init();
        return master;
    }
 
    /**---------------------------------------------------------
     * 单个读取coil [01 Coil Status 0x]
     */
    public static Boolean readCoilStatusSingle(int slaveId, int offset) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }
    /**
     * 多个读coils [01 Coil Status 0x]
     * @param master
     * @param slaveId 从站地址
     * @param start 起始地址的偏移量
     * @param len 待读的个数
     */
    private static void readCoilsMultiple(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, start, len);
        ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);
        if (response.isException())
            System.out.println("Exception response: message=" + response.getExceptionMessage());
        else
            System.out.println(Arrays.toString(response.getBooleanData())); //读到的boolean[]
    }
 
    /**-------------------------------------------------------------
     * 单个读InputStatus [02 Input Status 1x]
     */
    public static Boolean readInputStatusSingle(int slaveId, int offset) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
        Boolean value = getMaster().getValue(loc);
        return value;
    }
    /**
     * 多个读DiscreteInputs [02 Input Status 1x]
     * @param master
     * @param slaveId 从站地址
     * @param start 起始地址的偏移量
     * @param len 待读的个数
     */
    private static void readDiscreteInputsMultiple(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
        if (response.isException())
            System.out.println("Exception response: message=" + response.getExceptionMessage());
        else
            System.out.println(Arrays.toString(response.getBooleanData())); //读到的boolean[]
    }

    /**-------------------------------------------------------------------
     * 单个读HoldingRegister [03 Holding Register类型 2x]
     * @param slaveId
     * @param offset 读取寄存器的地址
     * @param dataType 读取的数据类型,如DataType.FOUR_BYTE_FLOAT
     */
    public static Number readHoldingRegisterSingle(int slaveId, int offset, int dataType) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
        Number value = getMaster().getValue(loc);
        return value;
    }
    /**
     * 多个读HoldingRegisters [03 Holding Register类型 2x]
     * @param master
     * @param slaveId 从站地址
     * @param start 起始地址的偏移量
     * @param len 待读寄存器的个数
     */
    public static void readHoldingRegistersMultiple(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        System.out.println(master.getIoLog());
        if (response.isException()){
            System.out.println("Exception response: message=" + response.getExceptionMessage());
        }
        else{
            System.out.println("获取原始byte[]: "+ Arrays.toString(response.getData()));
            System.out.println("16进制表示: "+ HexUtils.bytes2HexStr(response.getData()));
        }
    }


    /**-----------------------------------------------------------------
     * 单个读InputRegister [04 Input Registers 3x]
     */
    public static Number readInputRegisterSingle(int slaveId, int offset, int dataType) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
        Number value = getMaster().getValue(loc);
        return value;
    }
    /**
     * 多个读InputRegisters [04 Input Registers 3x]
     * @param master
     * @param slaveId 从站地址
     * @param start 起始地址的偏移量
     * @param len 待读寄存器的个数
     */
    private static void readInputRegistersMultiple(ModbusMaster master, int slaveId, int start, int len) throws ModbusTransportException {
        ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
        if (response.isException())
            System.out.println("Exception response: message=" + response.getExceptionMessage());
        else
            System.out.println(Arrays.toString(response.getData())); // 获取原始byte[]
    }


    /**---------------------------------------------------
     * 批量读
     * 可以批量读不同的寄存器类型: 不同类型的寄存器批量读会分多个请求发送; 相同类型的寄存器是一次请求
     * 可以批量读不同的数据类型
     * 可以指定偏移量跳着读取,BatchResults只会获取设置读的数据, 但响应报文还是连续的
     */
    public static void batchRead() throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        // 创建BatchRead, 泛型<Integer>是指Key(id)的类型,可以为Integer或者String
        BatchRead<Integer> batch = new BatchRead<>();
        // 读取1个inputStatus [02 Input Status 1x]
        batch.addLocator(0, BaseLocator.inputStatus(1, 0));
        // 读取2个holdingRegister [03 Holding Register]
        batch.addLocator(1, BaseLocator.holdingRegister(1, 0, DataType.FOUR_BYTE_FLOAT));
        batch.addLocator(2, BaseLocator.holdingRegister(1, 4, DataType.FOUR_BYTE_INT_UNSIGNED));
        batch.setContiguousRequests(false);

        ModbusMaster master = getMaster();
        BatchResults<Integer> results = master.send(batch);

        // key: 批量添加时设置的id
        System.out.println("BatchResults: "+ results);
        System.out.println("Key0: "+ results.getValue(0));
        System.out.println("Key1: "+ results.getValue(1));
        System.out.println("Key2: "+ results.getValue(2));
    }
 
    /**------------
     * 测试
     */
    public static void main(String[] args) throws Exception{
        // 1.单个读取测试
        // System.out.println("01功能测试: " + readCoilStatusSingle(1, 0));
        // System.out.println("02功能测试: " + readInputStatusSingle(1, 0));
        // System.out.println("03功能测试: " + readHoldingRegisterSingle(1, 0, DataType.FOUR_BYTE_FLOAT));
        // System.out.println("04功能测试: " + readInputRegisterSingle(1, 0, DataType.FOUR_BYTE_FLOAT));

        // 2.批量读测试
        //batchRead();

        // 3.多个读取测试
        ModbusMaster master = getMaster();
        // readCoilsMultiple(master,1,0,10);
        // readDiscreteInputsMultiple(master,1,0,10);
        readHoldingRegistersMultiple(master,1,0,10);
        // readInputRegistersMultiple(master,1,0,10);
    }
}