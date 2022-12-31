package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_2;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ModBusConnection {
    public final static TcpParameters tcpParameters = new TcpParameters();
    public static ModbusMaster master;
    static {
        try {
            InetAddress adress = InetAddress.getByName("127.0.0.1");
            // TCP参数设置ip地址
            tcpParameters.setHost(adress);
            // TCP设置长连接
            tcpParameters.setKeepAlive(true);
            // TCP设置端口，这里设置是默认端口502
            tcpParameters.setPort(502);
            // 创建一个主机
            master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ModbusMaster
     * @return
     */
    public static ModbusMaster getModbusMaster(){
        return  master;
    }

    /**
     * 关闭ModbusMaster
     */
    public static void destoryModbusMaster(){
        if(master.isConnected()){
            try {
                master.disconnect();
            } catch (ModbusIOException e) {
                e.printStackTrace();
            }
        }
    }
}