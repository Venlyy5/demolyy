package com.dfds.demolyy.HttpServerTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer_2 {
	public static void main(String[] args) throws Exception {
		new HTTPServer_2().startHttp();
	}
 
	private void startHttp() throws Exception {
		try {
			ServerSocket serverSocket = new ServerSocket(6788);
			//初始化一个线程池
			ExecutorService executorService = Executors.newFixedThreadPool(8);
 
			System.out.println("打开服务！");
			Socket socket;
			while (true) {
				socket = serverSocket.accept();
				executorService.execute(new Handler(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	class Handler implements Runnable {
		Socket socket;
		public Handler(Socket socket) {
			this.socket = socket;
		}
 
		@Override
		public void run() {
			System.out.println("端口号" + socket.getPort());
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();

				//读取浏览器的头中包含的数据
				byte buffer[] = new byte[inputStream.available()];
				
				int i = -1;
				inputStream.read(buffer);
				String readHead = new String(buffer);
 
				System.out.println("请求的浏览器的数据：\n" + readHead + "\n________\n");

				String string = "Hello!";
 
				//设置响应数据的头
				String split = "\r\n";
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("HTTP/1.1 200 OK\r\n");
				stringBuffer.append("Content-Type:text/html" + split);
				stringBuffer.append("Content-Length:" + string.length() + split);
				stringBuffer.append("\r\n");
				stringBuffer.append(string);
 
				System.out.println("响应的数据"+stringBuffer.toString());
				outputStream.write(stringBuffer.toString().getBytes());

				outputStream.flush();
				outputStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}