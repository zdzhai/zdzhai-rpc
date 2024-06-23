package com.zdzhai.rpc.registry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongdong
 * @Date 2024/6/21 17:01
 * 本地服务注册器
 */
public class LocalRegistry {
    //使用线程安全的ConcurrentHashMap存储服务注册信息，key为服务名称，value为服务的实现类。
    // 之后就可以根据要调用的服务名称获取到对应的实现类 利用反射进行方法调用了。
    private static final ConcurrentHashMap<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 服务注册
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    public static void delete(String serviceName) {
        map.remove(serviceName);
    }

}
