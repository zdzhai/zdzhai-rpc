package com.zdzhai.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RpcConfig;
import com.zdzhai.rpc.constant.RpcConstant;
import com.zdzhai.rpc.model.RpcRequest;
import com.zdzhai.rpc.model.RpcResponse;
import com.zdzhai.rpc.model.ServiceMetaInfo;
import com.zdzhai.rpc.protocol.*;
import com.zdzhai.rpc.registry.Registry;
import com.zdzhai.rpc.registry.RegistryFactory;
import com.zdzhai.rpc.serializer.Serializer;
import com.zdzhai.rpc.serializer.SerializerFactory;
import com.zdzhai.rpc.server.tcp.VertxTcpClient;
import io.etcd.jetcd.api.ProclaimResponse;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author dongdong
 * @Date 2024/6/21 20:35
 * 静态代理需要为每一个服务都实现一个代理类，很麻烦
 * 这里使用动态代理来动态生成接口的代理类
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterType(method.getParameterTypes())
                .args(args)
                .build();

        //发送请求
        try {
            //从注册中心获取服务提供者的请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            //log.info("registry {}", System.identityHashCode(registry));

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //todo 可能会存在多个节点，暂时取第一个，后续做负载均衡
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            log.info("消费者请求的服务节点为，{} 服务名称为 {}，服务方法为 {}",
                    selectedServiceMetaInfo.getServiceAddress(), serviceName, method.getName());

            //发送TCP请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            log.info("Connected to TCP server");
                            NetSocket socket = result.result();
                            //构造消息，发送数据
                            ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();

                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(rpcConfig.getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            rpcRequestProtocolMessage.setHeader(header);
                            rpcRequestProtocolMessage.setBody(rpcRequest);

                            //编码并发送请求
                            try {
                                Buffer encodeBuffer = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);
                                socket.write(encodeBuffer);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }

                            //接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    //完成响应
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());

                                } catch (IOException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            });
                        } else {
                            log.error("Failed to connect to TCP server");
                        }
                    });

            RpcResponse rpcResponse = responseFuture.get();
            return rpcResponse.getData();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
