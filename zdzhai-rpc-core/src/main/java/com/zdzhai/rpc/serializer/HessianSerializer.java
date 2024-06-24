package com.zdzhai.rpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author dongdong
 * @Date 2024/6/23 19:37
 * Hessian序列化器
 */
public class HessianSerializer implements Serializer {
    @Override
    public <T> byte[] serializer(T object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(bos);
        hessianOutput.writeObject(object);
        return bos.toByteArray();
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(bis);
        return (T) hessianInput.readObject(type);
    }
}
