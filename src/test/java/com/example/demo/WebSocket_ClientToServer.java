package com.example.demo;

import com.dfds.demolyy.WebsocketDemo.Demo_1.MyClientEndpoint;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocket_ClientToServer {

    public static void main(String[] args) throws IOException {
        //Timer timer = new Timer();
        // 每隔两秒给客户端发送一条消息
        //timer.schedule(new MyTask(), 1000, 2000);

        // 给客户端发送自定义消息
        //putin();
        //System.exit(0);
    }

    static int num = 0;
    static class MyTask extends TimerTask {
        @Override
        public void run() {
            MyClientEndpoint client = new MyClientEndpoint();
            client.start("ws://localhost:8080/websocketOne");

            try {
                num++;
                client.sendMessage("设备上报数据 " + num);
                client.closeSocket();
                System.out.println(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putin() throws IOException {
        MyClientEndpoint client = new MyClientEndpoint();
        client.start("ws://localhost:8080/websocketOne/66");

        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String input= "";

        while (!input.equals("exit")){
            System.out.println("请输入消息内容: ");
            input = br.readLine();
            client.sendMessage("设备上报数据: " + input);

        }
        client.closeSocket();
        System.out.println("ClientEndpoint 退出");
    }

}
