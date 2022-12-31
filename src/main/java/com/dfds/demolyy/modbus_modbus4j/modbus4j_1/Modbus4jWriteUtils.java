package com.dfds.demolyy.modbus_modbus4j.modbus4j_1;

import com.serotonin.modbus4j.code.DataType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;

/**
 * Modbus4j-WriteUtils
 */
public class Modbus4jWriteUtils {
    static Log log = LogFactory.getLog(Modbus4jWriteUtils.class);
    static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * 获取Master
     */
    public static ModbusMaster getMaster() throws ModbusInitException {
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(502);

        // modbusFactory.createAsciiMaster(wrapper);//ASCII 协议
        // modbusFactory.createRtuMaster(wrapper);  //RTU 协议
        // modbusFactory.createUdpMaster(params);   //UDP 协议
        ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, false);
        tcpMaster.init();
        return tcpMaster;
    }

    /**---------------------------------------------------
     * 单个写Coil [01 Coil Status(0x)](function ID = 05)
     * @param slaveId 从站ID
     * @param writeOffset 位置
     * @param writeValue  写入单个bool值
     * @return 是否写入成功
     */
    public static boolean writeCoilSingle(int slaveId, int writeOffset, boolean writeValue) throws ModbusTransportException, ModbusInitException {
        ModbusMaster tcpMaster = getMaster();
        WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
        WriteCoilResponse response = (WriteCoilResponse) tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 多个写Coils [01 Coil Status(0x)] (function ID = 15)
     * @param slaveId
     * @param startOffset 开始位置
     * @param bdata       批量写入的bool数组
     * @return 是否写入成功
     */
    public static boolean writeCoilsMultiple(int slaveId, int startOffset, boolean[] bdata) throws ModbusTransportException, ModbusInitException {
        ModbusMaster tcpMaster = getMaster();
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
        WriteCoilsResponse response = (WriteCoilsResponse) tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }

    /**-------------------------------------------------------------------
     * 单个写Register [03 Holding Register(4x)] (function ID = 06)
     * @param slaveId
     * @param writeOffset
     * @param writeValue 写入值(int16)
     */
    public static boolean writeRegisterSingle(int slaveId, int writeOffset, short writeValue) throws ModbusTransportException, ModbusInitException {
        ModbusMaster tcpMaster = getMaster();
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
        WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 多个写Registers [03 Holding Register(4x)](function ID=16)
     * @param slaveId
     * @param startOffset
     * @param data  批量写入的值数组(int16)
     * @return 是否写入成功
     */
    public static boolean writeRegistersMultiple(int slaveId, int startOffset, short[] data) throws ModbusTransportException, ModbusInitException {
        ModbusMaster tcpMaster = getMaster();
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, data);
        ModbusResponse response = tcpMaster.send(request);
        if (response.isException()) {
            log.error(response.getExceptionMessage());
            return false;
        } else {
            return true;
        }
    }

    /**---------------------------------------------------
     * 单个写方式2: Register
     * @param slaveId
     * @param offset
     * @param value 写入值(任意数值)
     * @param dataType 数据类型,如DataType.FOUR_BYTE_FLOAT
     */
    public static void writeHoldingRegisterSingle(int slaveId, int offset, Number value, int dataType) throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        ModbusMaster tcpMaster = getMaster();
        BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
        tcpMaster.setValue(locator, value);
    }

    /**
     * 测试写入
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        // 写一个Coil [01 Coil Status(0x)]
        // Request : 00 00 00 00 00 06 01 05 00 00 FF 00
        // Response: 00 00 00 00 00 06 01 05 00 00 FF 00
        writeCoilSingle(1, 0, true);

        // 写多个Coil [01 Coil Status(0x)]
        // Request : 00 00 00 00 00 08 01 0F 00 04 00 03 01 05
        // Response: 00 00 00 00 00 06 01 0F 00 04 00 03
        writeCoilsMultiple(1, 4, new boolean[]{true, false, true});

        // 写一个寄存器int16 [03 Holding Register(4x)]
        // Request : 00 00 00 00 00 06 01 06 00 00 FE A7
        // Response: 00 00 00 00 00 06 01 06 00 00 FE A7
        writeRegisterSingle(1, 0, (short)-345);

        // 写多个寄存器int16 [03 Holding Register(4x)]
        // 请求说明:从机地址 0x10 寄存器起始地址 寄存器数量 字节数 写入值
        // Request : 00 00 00 00 00 0D 01 10 00 01 00 03 06 FF FD 00 03 00 09
        // Response: 00 00 00 00 00 06 01 10 00 01 00 03
        writeRegistersMultiple(1, 1, new short[]{-3, 3, 9});

        // 写一个寄存器4字节float [03 Holding Register(4x)]
        // Request : 00 00 00 00 00 0B 01 10 00 04 00 02 04 41 88 CC CD
        // Response: 00 00 00 00 00 06 01 10 00 04 00 02
        writeHoldingRegisterSingle(1, 4, 17.1f, DataType.FOUR_BYTE_FLOAT);
    }
}