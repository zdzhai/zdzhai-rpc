package com.zdzhai.example.provider;

import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.registry.LocalRegistry;
import com.zdzhai.rpc.server.HttpServer;
import com.zdzhai.rpc.server.VertxHttpServer;

/**
 * @author dongdong
 * @Date 2024/6/20 21:00
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {

        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动Web服务
        HttpServer server = new VertxHttpServer();
        server.doStart(8080);

    }
}
