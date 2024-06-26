package com.zdzhai.example.consumer;

import com.zdzhai.example.common.model.Order;
import com.zdzhai.example.common.model.User;
import com.zdzhai.example.common.service.OrderService;
import com.zdzhai.example.common.service.UserService;
import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.config.RpcConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongdong
 * @Date 2024/6/22 22:29
 */
@Slf4j
public class ConsumerExample {
    public static void main(String[] args) {

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        log.info("消费端获取rpcConfig成功：{}", rpcConfig);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        //测试调用userService.getNumber()方法，是否Mock
        //short number = userService.getNumber();
        log.info("消费端 {} mock测试", rpcConfig.isMock() ? "开启" : "未开启");

        User user = new User();
        user.setName("zdzhai");
        //调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("user:" + newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
