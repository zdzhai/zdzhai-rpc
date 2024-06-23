package com.zdzhai.rpc.server;

/**
 * @author dongdong
 * @Date 2024/6/21 15:10
 * HTTP服务接口
 */
public interface HttpServer {

    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);
}
