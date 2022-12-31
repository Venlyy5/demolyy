package com.dfds.demolyy.modbus_modbus4j.modbus4j_1;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.Modbus;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

/**
 * Modbus4j Slave
 */
public class Modbus4j_Slave {
    public static void main( String[] args ) throws Exception {
        final TcpSlave slave = new TcpSlave(502, false);
        slave.addProcessImage( new BasicProcessImage(1) );

        new Thread(new Runnable() {
            public void run() {
                try {
                	slave.start();
                }
                catch (ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Slave作为客户端主动连接
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入InetAddress:");
        String inetAddress = scanner.nextLine();
        System.out.println("请输入Port:");
        int port = scanner.nextInt();
        Socket socket = new Socket(inetAddress, port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("Slave Connect");
        System.out.println("[Slave作为客户端主动连接]"+ socket.getInetAddress() +":"+ socket.getPort());

        System.out.println("------------- [Slave启动] 127.0.0.1:502 -------------");
        while (true) {
            // Illegal data Address(读写地址非法): 读写的地址可能超过了目标从机能读的最大地址，或者读的地址不存在，或者一次读取超过了从机的最大读写字节数
            final BasicProcessImage po = (BasicProcessImage) slave.getProcessImage(1);
            // 设置10个字HoldingRegister
            po.setNumeric(RegisterRange.HOLDING_REGISTER, 0, DataType.FOUR_BYTE_FLOAT, new Random().nextInt(9999));//AB CD
            po.setNumeric(RegisterRange.HOLDING_REGISTER, 2, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Random().nextInt(9999));//CD AB
            po.setNumeric(RegisterRange.HOLDING_REGISTER, 4, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED, new Random().nextInt(9999));//DC BA
            po.setNumeric(RegisterRange.HOLDING_REGISTER, 6, DataType.EIGHT_BYTE_FLOAT_SWAPPED, new Random().nextInt(9999)); //GH EF CD AB
            // 设置10个Coil
            int random = new Random().nextInt(9);
            for (int i = 0; i < 10; i++) {
                if ((i+random)%2==0){
                    po.setCoil(i,true);
                }else {
                    po.setCoil(i,false);
                }
            }
            // 间隔5秒变更一次值
            synchronized (slave) {
                slave.wait(5000);
            }
        }
    }
}