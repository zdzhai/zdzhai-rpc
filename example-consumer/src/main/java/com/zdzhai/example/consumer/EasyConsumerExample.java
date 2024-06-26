package com.zdzhai.example.consumer;

import com.zdzhai.example.common.model.User;
import com.zdzhai.example.common.service.UserService;

/**
 * @author dongdong
 * @Date 2024/6/20 21:04
 * 简单服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        //UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
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
