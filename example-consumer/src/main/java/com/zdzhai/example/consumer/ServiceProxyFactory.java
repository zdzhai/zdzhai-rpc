package com.zdzhai.example.consumer;

import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.proxy.MockServiceProxy;
import com.zdzhai.rpc.proxy.ServiceProxy;

import java.lang.reflect.Proxy;

/**
 * @author dongdong
 * @Date 2024/6/21 20:45
 * 服务代理工厂，用于创建代理对象
 */
public class ServiceProxyFactory {
    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static  <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }

    /**
     * 根据服务类获取默认代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }
}