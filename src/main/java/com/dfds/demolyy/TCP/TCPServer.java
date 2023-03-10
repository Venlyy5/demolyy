package com.dfds.demolyy.TCP;
import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP-Server
 * 服务器的工作是建立一个通信终端，并被动地等待客户端的连接。典型的TCP服务端执行如下：
 * 1.创建ServerSocket对象，绑定并监听端口
 * 2.通过accept监听客户端的请求
 * 3.建立连接后，通过输出输入流进行读写操作
 * 4.关闭相关资源
 * 模拟qq聊天功能： 实现客户端与服务器（一对一）的聊天功能，客户端首先发起聊天，输入的内容在服务器端和客户端显示， 
 * 然后服务器端也可以输入信息，同样信息也在客户端和服务器端显示 
 */
public class TCPServer {
    private int port = 8189;
    public TCPServer() {
    }
    public TCPServer(int port) {
        this.port = port;
    }  

    public void service() {
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();

            try {                  
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
                while (true) {                       
                    String accept = in.readUTF();
                    System.out.println(accept);
                    String send = scanner.nextLine();
                    System.out.println("Server：" + send);
                    out.writeUTF("Server：" + send);
                }  
            } finally {
                socket.close();
                server.close();
            }  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
    }

    public void ModBusServer(){
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();

            try {
                BufferedInputStream bfIn = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bfOut = new BufferedOutputStream(socket.getOutputStream());

                while (true) {
                    Thread.sleep(5000);
                    //String hexStr = "0DC8 0000 0006 01 01 0000 000A"; //01线圈采集
                    String hexStr = "004C 0000 0006 01 04 0000 0009"; //04输入寄存器采集
                    System.out.println("Collect Request: "+ hexStr);
                    bfOut.write(HexUtils.hexStr2Bytes(hexStr.replace(" ","")));
                    bfOut.flush();

                    byte[] bytes = new byte[27]; //available()有时会提前返回0,所以导致创建的接收数组大小为0.固定空间不用等待
                    bfIn.read(bytes);
                    System.out.println("Response: "+ HexUtils.bytes2HexStr(bytes));

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socket.close();
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // 测试服务1
        //new TCPServer().service();

        // 测试服务2
        new TCPServer().ModBusServer();
    }  
}  

