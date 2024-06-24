package com.zdzhai.rpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdzhai.rpc.model.RpcRequest;
import com.zdzhai.rpc.model.RpcResponse;

import java.io.IOException;

/**
 * @author dongdong
 * @Date 2024/6/23 17:13
 * JSON序列化器
 */
public class JsonSerializer implements Serializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serializer(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, type);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, type);
        }
        return obj;
    }


    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap，无法转成原始对象
     * 因此需要做特殊处理
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterType = rpcRequest.getParameterType();
        Object[] args = rpcRequest.getArgs();

        //循环处理每个参数的类型
        for (int i = 0; i < parameterType.length; i++) {
            Class<?> clazz = parameterType[i];
            //如果类型不同，则重新处理一下类型
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(bytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap，无法转成原始对象
     * 因此需要做特殊处理
     * @param rpcResponse
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        //处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
