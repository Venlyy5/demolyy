package com.dfds.demolyy.WebsocketDemo.Demo_1;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 该注解可以将类定义成一个WebSocket服务器端
 * @OnOpen 连接建立成功调用的方法, 表示有浏览器链接过来的时候被调用
 * @OnClose 连接关闭调用的方法, 表示浏览器发出关闭请求的时候被调用
 * @OnMessage 收到客户端消息后调用的方法, 表示浏览器发消息的时候被调用
 * @OnError 发生错误时调用
 */
@ServerEndpoint(value = "/websocketOne/{userId}")
@Component
public class MyServerEndpoint {

    // 记录当前在线连接数
    private static int onlineCount = 0;

    // 存放每个客户端对应的MyWebSocket对象
    //private static CopyOnWriteArraySet<MyServerEndpoint> webSocketSet = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String, MyServerEndpoint> webSocketSet = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //指定的sid，具有唯一性
    private static String sid = "";


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, EndpointConfig config) {
        this.session = session;
        //webSocketSet.add(this); // 加入set中

        //获取WebsocketConfig.java中配置的“sessionId”信息值
        String httpSessionId = (String) config.getUserProperties().get("sessionId");
        sid = userId;
        webSocketSet.put(userId, this);
        addOnlineCount(); // 在线数加1
        System.out.println(userId +" 加入！当前在线人数为 : " + getOnlineCount());
        try {
            sendMessage("您已成功连接！");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
        System.out.println(this+ " 连接关闭！当前在线人数为 : " + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端 "+ this +" 的消息:" + message);
        // 群发消息
        // for (MyServerEndpoint item : webSocketSet) {
        //     try {
        //         item.sendMessage(message);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }

        // 发给指定用户
    }


    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        // this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        // for (MyServerEndpoint item : webSocketSet) {
        //     try {
        //         item.sendMessage(message);
        //     } catch (IOException e) {
        //         continue;
        //     }
        // }
    }


    public boolean sendToUser(String message, String sendUserId) throws IOException {
        if (webSocketSet.get(sendUserId) != null) {
            if (!sid.equals(sendUserId)) {
                return false;
                //webSocketSet.get(sendUserId).sendMessage("用户" + sendUserId + "发来消息：" + message);
            } else {
                webSocketSet.get(sendUserId).sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else {
            //如果用户不在线则返回不在线信息给自己
            return false;
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        MyServerEndpoint.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        MyServerEndpoint.onlineCount--;
    }
}
