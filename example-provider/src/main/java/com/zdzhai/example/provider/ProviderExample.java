package com.zdzhai.example.provider;

import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.registry.LocalRegistry;
import com.zdzhai.rpc.server.HttpServer;
import com.zdzhai.rpc.server.VertxHttpServer;

/**
 * @author dongdong
 * @Date 2024/6/22 22:32
 */
public class ProviderExample {
    public static void main(String[] args) {

        //RPC框架初始化
        RpcApplication.init();

        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动Web服务
        HttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
