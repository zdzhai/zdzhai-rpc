package com.zdzhai.rpc.server.tcp;

import com.zdzhai.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

import java.util.Arrays;

/**
 * @author dongdong
 * @Date 2024/6/21 15:11
 * 创建Vert.x TCP服务器
 */
public class VertxTcpServer implements HttpServer {
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();

        //创建Vertx服务器
        NetServer netServer = vertx.createNetServer();

        //绑定请求处理器
        netServer.connectHandler(new TcpServerHandler());


/*        //监听端口并处理请求
        netServer.connectHandler(socket -> {
            //处理连接
            socket.handler(buffer -> {
                //接收请求
                byte[] requestData = buffer.getBytes();
                System.out.println("Received request from client: " + Arrays.toString(requestData));

                //处理请求
                byte[] responseData =  handleRequest(requestData);

                //发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });*/

        //启动HTTP服务器并监听指定端口
        netServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server is now listening on port " + port);
            }
            else {
                System.err.println("Failed to start TCP server:" + result.cause());
            }
        });
    }

    /**
     * 处理请求
     * @param requestData
     * @return
     */
    private byte[] handleRequest(byte[] requestData) {
        return "Hello, client!".getBytes();
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
