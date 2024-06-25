package com.zdzhai.rpc.registry;

import com.zdzhai.rpc.spi.SpiLoader;

/**
 * @author dongdong
 * @Date 2024/6/24 19:07
 * 注册中心工厂类
 */
public class RegistryFactory {
    /**
     * 使用SPI方式加载注册中心
     */
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 使用Map做序列化器映射
     */
    /*private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap() {{
       put(SerializerKeys.JDK, new JdkSerializer());
       put(SerializerKeys.JSON, new JsonSerializer());
       put(SerializerKeys.KRYO, new KryoSerializer());
       put(SerializerKeys.HESSIAN, new HessianSerializer());
    }};*/

    /**
     * 默认序列化器
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取序列化器实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
