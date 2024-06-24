package com.zdzhai.example.consumer;

import com.zdzhai.example.common.model.User;
import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RpcConfig;

/**
 * @author dongdong
 * @Date 2024/6/22 22:29
 */
public class ConsumerExample {
    public static void main(String[] args) {

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        //测试调用userService.getNumber()方法，是否Mock
        short number = userService.getNumber();
        System.out.println("mock测试:" + number);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);

        User user = new User();
        user.setName("zdzhai");
        //调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
