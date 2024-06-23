package com.zdzhai.example.consumer;

import com.zdzhai.example.common.model.User;
import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.model.RpcRequest;
import com.zdzhai.rpc.serializer.JdkSerializer;
import com.zdzhai.rpc.serializer.Serializer;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author dongdong
 * @Date 2024/6/22 10:30
 */
public class JdkSerializerTest extends TestCase {

    public void testSerializer() {
        Serializer jdkSerializer = new JdkSerializer();
        User user = new User();
        user.setName("zzd");
        try {
            byte[] bytes = jdkSerializer.serializer(user);
            User deserializerUser = jdkSerializer.deserializer(bytes, User.class);
            System.out.println("deserializerUser: " + deserializerUser.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterType(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = jdkSerializer.serializer(rpcRequest);
            System.out.println("test:" + Arrays.toString(bodyBytes));
            RpcRequest deserializerRpcRequest = jdkSerializer.deserializer(bodyBytes, RpcRequest.class);
            User deserializerUser = (User) deserializerRpcRequest.getArgs()[0];
            System.out.println("deserializerUser2: " + deserializerUser.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        System.out.println("userService:" + userService);
        //user.setName("zdzhai");
        //调用
        //User newUser = userService.getUser(user);
    }
}