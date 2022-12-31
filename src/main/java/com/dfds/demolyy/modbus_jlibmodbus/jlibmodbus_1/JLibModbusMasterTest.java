package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_1;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import java.net.InetAddress;

/**
 * Master读取寄存器和线圈测试
 */
public class JLibModbusMasterTest {
    public static void main(String[] args) {
        masterTest();
    }

    public static void masterTest(){
        try {
            //设置自增事务id
            Modbus.setAutoIncrementTransactionId(true);
            //设置主机TCP参数
            TcpParameters tcpParameters = new TcpParameters();
            tcpParameters.setHost(InetAddress.getByName("127.0.0.1")); //InetAddress.getLocalHost()
            tcpParameters.setPort(Modbus.TCP_PORT);
            //TCP设置长连接
            tcpParameters.setKeepAlive(true);


            //创建一个主机
            ModbusMaster modbusMaster = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            //从机地址
            int slaveId = 1;
            //寄存器读取开始地址
            int offset = 0;
            //读取寄存器数量
            int quantity = 10;

            if (!modbusMaster.isConnected()){
                System.out.println("Modbus主机没有连接,现在建立连接");
                modbusMaster.connect();
            }

            //功能码04 读输入寄存器
            int[] registers = modbusMaster.readInputRegisters(slaveId, offset, quantity);
            for (int value : registers) {
                System.out.println("Address: "+ offset++ +", Value: "+ value);
            }
            // INFO: Frame recv: 0001 0000 0006 01 04 0000 000A
            // INFO: Frame sent: 0001 0000 0017 01 04 14 3039 0000 5AD3 0001 0000 0000 2D18 5444 21FB 4009
            // 字0:3039 -> 12345; 字2~3:0001 5AD3 -> 88787;  字4:0; 字5:0; 字6~10:4009 21FB 5444 2D18 -> Math.PI


            //功能码01 读Coils, 读16个位
            offset = 0;
            boolean[] booleans = modbusMaster.readCoils(slaveId, offset, 16);
            for (boolean aBoolean : booleans) {
                System.out.println("Address: "+ offset++ +", BooleanValue: "+ aBoolean);
            }
            // INFO: Frame recv: 0002 0000 0006 01 01 0000 0010
            // INFO: Frame sent: 0002 0000 0005 01 01 02 2040
            // 20 40 二进制为 0010 0000 0100 0000, 位计算:7~0 15~8, 字节层面从左往右看(大端模式高字左 低字右), 位层面从右往左看(高位左 低位右), 第5位是1, 第14位是1

            // 关闭连接
            modbusMaster.disconnect();
        } catch ( Exception e){
            System.out.println("JLibModbus运行异常");
            e.printStackTrace();
        }
    }
}