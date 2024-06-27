package com.zdzhai.rpc.protocol;

import cn.hutool.core.util.IdUtil;
import com.zdzhai.rpc.model.RpcRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dongdong
 * @Date 2024/6/27 15:56
 */
public class ProtocolMessageTest {


    @Test
    public void testMessageEncoderAndDecoder() throws IOException {

        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion("1.0");
        rpcRequest.setParameterType(new Class[]{String.class, String.class});
        rpcRequest.setArgs(new Object[]{"aa", "bb"});

        rpcRequestProtocolMessage.setHeader(header);
        rpcRequestProtocolMessage.setBody(rpcRequest);


        Buffer encode = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);
        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(encode);
        Assert.assertNotNull(message);
    }
}