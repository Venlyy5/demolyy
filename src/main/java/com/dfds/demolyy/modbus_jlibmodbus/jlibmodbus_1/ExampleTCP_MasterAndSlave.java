package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_1;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveTCP;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.intelligt.modbus.jlibmodbus.utils.*;
import java.net.InetAddress;
import java.util.Observer;

import static com.dfds.demolyy.utils.ProtocolUtils.HexUtils.*;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 * [https://github.com/kochedykov/jlibmodbus]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

/**
 * 创建Slave,然后创建Master去读取测试
 */
public class ExampleTCP_MasterAndSlave {
    static public void main(String[] argv) {
        try {
            // 设置控制台输出主机和从机命令交互日志
            Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
            // 设置TCP连接参数
            TcpParameters tcpParameters = new TcpParameters();
            tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setPort(Modbus.TCP_PORT);
            tcpParameters.setKeepAlive(true);

            // 创建master, slave
            ModbusSlaveTCP slave = (ModbusSlaveTCP) ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
            ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);

            // 配置master,slave
            master.setResponseTimeout(1000);
            slave.setServerAddress(Modbus.TCP_DEFAULT_ID);// slaveId
            slave.setBroadcastEnabled(true);
            slave.setReadTimeout(1000);

            // master,slave添加事件监听器
            FrameEventListener listener = new FrameEventListener() {
                @Override
                public void frameSentEvent(FrameEvent event) {
                    System.out.println("frame sent " + DataUtils.toAscii(event.getBytes()));
                }

                @Override
                public void frameReceivedEvent(FrameEvent event) {
                    System.out.println("frame recv " + DataUtils.toAscii(event.getBytes()));
                }
            };
            master.addListener(listener);
            slave.addListener(listener);

            // slave添加状态检测
            Observer o = new ModbusSlaveTcpObserver() {
                @Override
                public void clientAccepted(TcpClientInfo info) {
                    System.out.println("Client connected " + info.getTcpParameters().getHost());
                }
                @Override
                public void clientDisconnected(TcpClientInfo info) {
                    System.out.println("Client disconnected " + info.getTcpParameters().getHost());
                }
            };
            slave.addObserver(o);

            // 创建保持寄存器
            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);
            // 设置int值
            for (int i = 0; i < holdingRegisters.getQuantity(); i++) {
                holdingRegisters.set(i, i + 1);
            }
            // 设置float64值(4个字, GH EF CD AB, High word first & High byte first)
            holdingRegisters.setFloat64At(0, Math.PI);
            // 设置slave的寄存器数据
            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

            // 设置自增事务ID
            Modbus.setAutoIncrementTransactionId(true);

            slave.listen();
            master.connect();

            // 配置请求
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
            request.setServerAddress(Modbus.TCP_DEFAULT_ID);
            request.setStartAddress(0);
            request.setQuantity(10);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

            // master处理请求
            master.processRequest(request);


            // 1.获取响应的数据byte[]
            byte[] bytes = response.getBytes();
            System.out.println("响应byte[]: "+ byteArray2HexString(bytes));

            // 2.获取响应的数据值
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            // 获取int
            for (int r : registers) {
                System.out.println(r);
            }
            //获取float
            System.out.println("PI is approximately equal to " + registers.getFloat64At(0));
            System.out.println("Double -> HEX: "+ double2Hex(Math.PI));
            System.out.println("HEX -> Double: "+ hex2Double("400921FB54442D18"));

            master.disconnect();
            slave.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}