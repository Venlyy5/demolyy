package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_3;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.ModbusRequestBuilder;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class SimpleMasterTCP  extends AbstractTcp {

    public static void main(String[] args) {
        try {
            // 如果想单独设置连接参数,应该使用 createModbusMasterTCP(String host, int port, boolean keepAlive);
            ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);

            int slaveId = 1;
            int offset = 0;
            int quantity = 10;

            try {
                if (!m.isConnected()) {
                    m.connect();
                }

                // 同样从 1.2.8.4 开始，您可以创建自己的请求并与主服务器一起处理它
                ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadHoldingRegisters(slaveId, offset, quantity);
                // 功能码03 读保持寄存器, 只有强转类型的才能获取解析好的数据
                ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) m.processRequest(request);

                System.out.println(" ServerAddress: "+ response.getServerAddress());
                System.out.println(" ProtocolId: "+ response.getProtocolId());
                System.out.println(" TransactionId: "+ response.getTransactionId());
                System.out.println(" Function: "+ response.getFunction());
                System.out.println(" ExceptionCode: "+ response.getModbusExceptionCode());

                // 获取原始 byte[]
                byte[] bytes = response.getBytes();
                for (int i = 0; i < bytes.length; i++) {
                    if (i%2==0){
                        System.out.print("["+bytes[i] +" "+ bytes[i+1]+"]");
                    }
                }

                // 获取寄存器值的 int[]
                for (int value : response.getHoldingRegisters()) {
                    System.out.println("Address:"+ offset++ +"   Value: "+ value);
                }

            } catch (ModbusProtocolException e) {
                log.error(" ModbusProtocolException :{}",e.getMessage());
            } catch (ModbusNumberException e) {
                log.error(" ModbusNumberException :{}",e.getMessage());
            } catch (ModbusIOException e) {
                log.error(" ModbusIOException :{}",e.getMessage());
            } finally {
                try {
                    m.disconnect();
                } catch (ModbusIOException e) {
                    log.error(" ModbusIOException :{}",e.getMessage());
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error(" Exception :{}",e.getMessage());
        }
    }
}