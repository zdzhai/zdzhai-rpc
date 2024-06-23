package com.zdzhai.example.consumer;

import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RpcConfig;

/**
 * @author dongdong
 * @Date 2024/6/22 22:29
 */
public class ConsumerExample {
    public static void main(String[] args) {

        //测试调用userService.getNumber()方法，是否Mock
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        System.out.println(userService.getClass());
        short number = userService.getNumber();
        System.out.println(number);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);
    }
}
