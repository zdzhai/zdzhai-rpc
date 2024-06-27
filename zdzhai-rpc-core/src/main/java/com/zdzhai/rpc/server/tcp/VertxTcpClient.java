package com.zdzhai.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongdong
 * @Date 2024/6/27 13:51
 * VertxTCP客户端
 */
@Slf4j
public class VertxTcpClient {

    public void start() {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
           if (result.succeeded()) {
                log.info("Connected to TCP server");
                NetSocket socket = result.result();
                //发送数据
               socket.write("Hello, server!");

               //接收响应
               socket.handler(buffer -> {
                   System.out.println("Received response from server: " + buffer.toString());
               });
           } else {
               log.error("Failed to connect to TCP server");
           }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
