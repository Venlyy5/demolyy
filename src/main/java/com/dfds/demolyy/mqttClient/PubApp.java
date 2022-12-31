package com.dfds.demolyy.mqttClient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public class PubApp {
    public static void main(String[] args) {
        String pubTopic = "testTopicLyy/1";
        int qos = 2;

        //String broker = "tcp://broker.emqx.io:1883";
        String broker = "tcp://127.0.0.1:1883";
        String clientId = "emqx_test2";
        String userName = "emqx_test2";
        String password = "emqx_test_password2";

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(userName);
            connOpts.setPassword(password.toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);
            // 设置回调
            client.setCallback(new OnMessageCallback());
            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);

            String next = "";
            while (!"exit".equals(next)){
                System.out.println("请输入要发送的消息:");
                Scanner sc = new Scanner(System.in);
                next = sc.next();
                // 消息发布所需参数
                MqttMessage message = new MqttMessage(next.getBytes());
                message.setQos(qos);
                client.publish(pubTopic, message);
                System.out.println("Message published");
            }

            client.disconnect();
            System.out.println("Disconnected");
            client.close();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
