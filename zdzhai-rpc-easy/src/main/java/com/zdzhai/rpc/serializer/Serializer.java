package com.zdzhai.rpc.serializer;

import java.io.IOException;

/**
 * @author dongdong
 * @Date 2024/6/21 17:11
 * 序列化器接口
 */
public interface Serializer {

    /**
     * 序列化 将对象变为可传输的字节数组
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serializer(T object) throws IOException;

    /**
     * 反序列化 将字节数据恢复为对象
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserializer(byte[] bytes, Class<T> type) throws IOException;
}
