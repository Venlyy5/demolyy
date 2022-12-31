package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_3;

import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 抽象实现配置加载
 */
@Slf4j
public abstract class AbstractTcp {

    static TcpParameters tcpParameters = new TcpParameters();

    static{
        // 不用yml的原因： 静态代码块是在jvm加载类时执行，此时yml还没有加载所以无法引用
        ModbusConfig modbusConfig = new ModbusConfig();
        // tcp 参数已默认设置，如示例
        try {
            tcpParameters.setHost(InetAddress.getByName(modbusConfig.host));
        } catch (UnknownHostException e) {
            log.error(" UnknownHostException :{}", e.getMessage());
        }
        tcpParameters.setKeepAlive(modbusConfig.keepAlive);
        tcpParameters.setPort(modbusConfig.port);
    }

}