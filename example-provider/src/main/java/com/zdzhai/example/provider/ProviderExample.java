package com.zdzhai.example.provider;

import com.zdzhai.example.common.service.OrderService;
import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RegistryConfig;
import com.zdzhai.rpc.config.RpcConfig;
import com.zdzhai.rpc.model.ServiceMetaInfo;
import com.zdzhai.rpc.registry.LocalRegistry;
import com.zdzhai.rpc.registry.Registry;
import com.zdzhai.rpc.registry.RegistryFactory;
import com.zdzhai.rpc.server.HttpServer;
import com.zdzhai.rpc.server.VertxHttpServer;
import com.zdzhai.rpc.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongdong
 * @Date 2024/6/22 22:32
 */
@Slf4j
public class ProviderExample {
    public static void main(String[] args) {

        //RPC框架初始化
        RpcApplication.init();

        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务orderService
        String orderServiceName = OrderService.class.getName();
        LocalRegistry.register(orderServiceName, OrderServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registryInstance = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName(orderServiceName);
        serviceMetaInfo2.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo2.setServicePort(rpcConfig.getServerPort());
        try {
            registryInstance.register(serviceMetaInfo);
            registryInstance.register(serviceMetaInfo2);
        } catch (Exception e) {
            log.error("服务注册失败", e);
            throw new RuntimeException(e);
        }

        //启动TCP服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

        /*//启动Web服务
        HttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());*/
    }

}
