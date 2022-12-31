package com.dfds.demolyy.TCP;
import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP-Client
 * 客户端向服务器端发送连接请求后，就被动地等待服务器的响应。典型的TCP客户端经过下面三步骤：
 * 1.创建一个Socket实例：构造函数向指定的远程主机和端口建立一个TCP连接
 * 2.通过Socket的I/O流与服务端通信
 * 3.使用Socket类的close方法关闭连接
 * 注意用到的输入输出流DataInputStream和DataOutputStream，成对出现，最好用字节流
 */
public class TCPClient {
    private String host = "localhost";
    private int port = 502;
    public TCPClient() {
    }
    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 聊天室Demo-发送字符串
     */
    public void chat() {
        try {
            Socket socket = new Socket(host, port);
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                Scanner scanner = new Scanner(System.in);
                while (true) {
                    // 聊天室Demo-发送字符串
                    String send = scanner.nextLine();
                    System.out.println("Client："+ send);
                    out.writeUTF("Client："+ send);  // Client -> Server
                    System.out.println(in.readUTF());   // Server <- Client
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ModBus Master(Client)发送请求给Slave(Server)
     */
    public void MasterClient(){
        try {
            Socket socket = new Socket(host, port);
            try {
                BufferedInputStream bfIn = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bfOut = new BufferedOutputStream(socket.getOutputStream());

                String hex = "000000000006010300000003";
                bfOut.write(HexUtils.hexStr2Bytes(hex));
                bfOut.flush();
                System.out.println("Send: "+ hex);

                //Thread.sleep(1000); // 阻塞等待客户端write数据到服务端
                //byte[] bytes = new byte[bfIn.available()];
                byte[] bytes = new byte[15]; //available()有时会提前返回0,所以导致创建的接收数组大小为0.固定空间不用等待
                bfIn.read(bytes);
                System.out.print("Recv: "+ HexUtils.bytes2HexStr(bytes));
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //new TCPClient().chat();
        new TCPClient().MasterClient();
    }
}

