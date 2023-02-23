package com.dfds.demolyy.WebsocketDemo.Demo_1;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class MyClientEndpoint {
	
	private Session session;

	@OnOpen
	public void onOpen(Session session) throws IOException {
		System.out.println("ClientEndpoint已连接, SessionID= "+ session.getId());
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message) {
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

    @OnClose
    public void onClose() throws Exception{
    }

    public void closeSocket() throws IOException{
        this.session.close();
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);

    }

    //启动客户端并建立链接
    public void start(String uri) {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			this.session = container.connectToServer(MyClientEndpoint.class, URI.create(uri));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}