package com.zdzhai.example.consumer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RpcConfig;
import com.zdzhai.rpc.constant.RpcConstant;
import com.zdzhai.rpc.model.RpcRequest;
import com.zdzhai.rpc.model.RpcResponse;
import com.zdzhai.rpc.model.ServiceMetaInfo;
import com.zdzhai.rpc.registry.Registry;
import com.zdzhai.rpc.registry.RegistryFactory;
import com.zdzhai.rpc.serializer.JdkSerializer;
import com.zdzhai.rpc.serializer.Serializer;
import com.zdzhai.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author dongdong
 * @Date 2024/6/21 20:35
 * 静态代理需要为每一个服务都实现一个代理类，很麻烦
 * 这里使用动态代理来动态生成接口的代理类
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

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
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            //System.out.println("测试输出serviceProxy:" + Arrays.toString(bodyBytes));
            //请求地址被硬编码了，需要使用注册中心和服务发现机制解决
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //todo 可能会存在多个节点，暂时取第一个，后续做负载均衡
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
