package com.zdzhai.rpc.server;

import io.vertx.core.Vertx;

/**
 * @author dongdong
 * @Date 2024/6/21 15:11
 * 创建Vert.x HTTP服务器
 */
public class VertxHttpServer implements HttpServer {
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();

        //创建Vertx服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //绑定请求处理器
        server.requestHandler(new HttpServerHandler());

        //监听端口并处理请求
        /*server.requestHandler(request -> {
            System.out.println("Received request:" + request.method() + " " + request.uri());

            //发送响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x HTTP server");
        });*/

        //启动HTTP服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            }
            else {
                System.err.println("Failed to start server:" + result.cause());
            }
        });
    }
}
