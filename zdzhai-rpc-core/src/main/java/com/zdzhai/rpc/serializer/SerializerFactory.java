package com.zdzhai.rpc.serializer;

import com.zdzhai.rpc.spi.SpiLoader;

/**
 * @author dongdong
 * @Date 2024/6/23 19:44
 * 序列化器工厂（用户获取序列化器对象）
 */
public class SerializerFactory {

    /**
     * 使用SPI方式加载序列化器
     */
    static {
        SpiLoader.load(Serializer.class);
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
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
