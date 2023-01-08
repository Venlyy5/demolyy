package com.dfds.demolyy.modbus_jlibmodbus.jlibmodbus_1;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.hutool.socket.SocketUtil;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.ModbusCoils;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

/**------------------
 * JLibModBus 从站测试
 */
public class JLibModBusSlaveTest {
	public static void main(String[] args) {
		try {
			// Slave作为客户端主动连接
			// Scanner scanner = new Scanner(System.in);
			// System.out.println("请输入InetAddress:");
			// String inetAddress = scanner.nextLine();
			// System.out.println("请输入Port:");
			// int port = scanner.nextInt();
			// Socket socket = new Socket(inetAddress, port);
			// DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			// out.writeUTF("Slave Connect");
			// System.out.println("[Slave作为客户端主动连接]"+ socket.getInetAddress() +":"+ socket.getPort());



			// 设置控制台输出主机和从机命令交互日志
			Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
			// 设置自增事务ID
			Modbus.setAutoIncrementTransactionId(true);

			// 设置从机TCP参数
			TcpParameters tcpParameters = new TcpParameters();
			tcpParameters.setHost(InetAddress.getByName("127.0.0.1")); //InetAddress.getLocalHost()
			tcpParameters.setPort(Modbus.TCP_PORT);
			// 设置从机TCP的是否长连接，通俗点讲就是一直保持连接，一次连接完下次就不要在连接了
			tcpParameters.setKeepAlive(true);


			// 创建一个从机
			ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
			// 为从机设置从机服务地址slaveId
			slave.setServerAddress(1);
			// 设置从机的读超时时间，建议主机读的超时时间小于该值
			slave.setReadTimeout(1500);
			// 创建从机的寄存器
			MyOwnDataHolder dh = new MyOwnDataHolder();
			// 为从机寄存器添加监听事件，这里的监听事件主要是主机如果发送写命令修改从机则控制台输出
			dh.addEventListener(new ModbusEventListener() {
				@Override
				public void onWriteToSingleCoil(int address, boolean value) {
					System.out.print("onWriteToSingleCoil: address " + address + ", value " + value);
				}
				@Override
				public void onWriteToMultipleCoils(int address, int quantity, boolean[] values) {
					System.out.print("onWriteToMultipleCoils: address " + address + ", quantity " + quantity);
				}
				@Override
				public void onWriteToSingleHoldingRegister(int address, int value) {
					System.out.print("onWriteToSingleHoldingRegister: address " + address + ", value " + value);
				}
				@Override
				public void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values) {
					System.out.print("onWriteToMultipleHoldingRegisters: address " + address + ", quantity " + quantity);
				}
			});
			// 为从机设置寄存器
			slave.setDataHolder(dh);


			// 设置从机01-Coil, 02-DiscreteInputs, 第一个参数代表寄存器地址，第二个代表修改的数值
			ModbusCoils mc = new ModbusCoils(16);
			mc.set(5,true);
			mc.set(9,true);

			// 设置从机03HoldingRegisters, 04-InputRegisters
			ModbusHoldingRegisters hr = new ModbusHoldingRegisters(10);
			hr.set(0, 12345); 						// 2字节int
			hr.setInt16At(1,789); 		// 2字节int
			hr.setInt32At(2, 88787); 	// 4字节int, CD AB
			hr.setFloat32At(4,998.673F); // 4字节Float, CD AB
			hr.setFloat64At(6, Math.PI); 	    // 8字节Float, GH EF CD AB

			// 为从机设置 01-Coil
			slave.getDataHolder().setCoils(mc);
			// 为从机设置 02-DiscreteInputs
			slave.getDataHolder().setDiscreteInputs(mc);
			// 为从机设置 03-HoldingRegisters
			slave.getDataHolder().setHoldingRegisters(hr);
			// 为从机设置 04-InputRegisters
			slave.getDataHolder().setInputRegisters(hr);


			// 开启从机监听事件
			System.out.println("[启动Slave] 127.0.0.1:" + Modbus.TCP_PORT);
			slave.listen();
 
			//这部分代码主要是设置Java虚拟机关闭的时候需要做的事情，即本程序关闭的时候需要做的事情，直接使用即可
			if (slave.isListening()) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						System.out.println("[关闭Slave]");
						synchronized (slave) {
							slave.notifyAll();
						}
					}
				});
 
				synchronized (slave) {
					slave.wait();
				}
 
				/*
				 * using master-branch it should be #slave.close();
				 */
				slave.shutdown();
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	// 监听接口
	public interface ModbusEventListener {
		void onWriteToSingleCoil(int address, boolean value);
 
		void onWriteToMultipleCoils(int address, int quantity, boolean[] values);
 
		void onWriteToSingleHoldingRegister(int address, int value);
 
		void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values);
	}
 
	// 寄存器类定义
	public static class MyOwnDataHolder extends DataHolder {
		final List<ModbusEventListener> modbusEventListenerList = new ArrayList<ModbusEventListener>();
		public MyOwnDataHolder() {
			// you can place the initialization code here
			/*
			 * something like that: setHoldingRegisters(new
			 * SimpleHoldingRegisters(10)); setCoils(new Coils(128)); ... etc.
			 */
		}
 
		public void addEventListener(ModbusEventListener listener) {
			modbusEventListenerList.add(listener);
		}
 
		public boolean removeEventListener(ModbusEventListener listener) {
			return modbusEventListenerList.remove(listener);
		}
 
		@Override
		public void writeHoldingRegister(int offset, int value) throws IllegalDataAddressException,
				IllegalDataValueException {
			for (ModbusEventListener l : modbusEventListenerList) {
				l.onWriteToSingleHoldingRegister(offset, value);
			}
			super.writeHoldingRegister(offset, value);
		}
 
		@Override
		public void writeHoldingRegisterRange(int offset, int[] range)
				throws IllegalDataAddressException, IllegalDataValueException {
			for (ModbusEventListener l : modbusEventListenerList) {
				l.onWriteToMultipleHoldingRegisters(offset, range.length, range);
			}
			super.writeHoldingRegisterRange(offset, range);
		}
 
		@Override
		public void writeCoil(int offset, boolean value) throws IllegalDataAddressException,
				IllegalDataValueException {
			for (ModbusEventListener l : modbusEventListenerList) {
				l.onWriteToSingleCoil(offset, value);
			}
			super.writeCoil(offset, value);
		}
 
		@Override
		public void writeCoilRange(int offset, boolean[] range) throws IllegalDataAddressException,
				IllegalDataValueException {
			for (ModbusEventListener l : modbusEventListenerList) {
				l.onWriteToMultipleCoils(offset, range.length, range);
			}
			super.writeCoilRange(offset, range);
		}
	}
}