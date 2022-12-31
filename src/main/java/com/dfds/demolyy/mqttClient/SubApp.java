package com.dfds.demolyy.mqttClient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubApp {
    public static void main(String[] args) {
        String subTopic = "testTopicLyy/#";

        //String broker = "tcp://broker.emqx.io:1883";
        String broker = "tcp://127.0.0.1:1883";
        String clientId = "emqx_test";
        String userName = "emqx_test";
        String password = "emqx_test_password";

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
            // 订阅
            client.subscribe(subTopic);
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
