package com.zdzhai.rpc.protocol;

import com.zdzhai.rpc.serializer.Serializer;
import com.zdzhai.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author dongdong
 * @Date 2024/6/27 15:17
 * 自定义协议消息编码器
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * @param protocolMessage
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessage.getHeader();
        //依次向缓冲区写入
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumBykey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化器类型不存在！");
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        byte[] bytes = serializer.serializer(protocolMessage.getBody());

        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);

        return buffer;
    }
}
