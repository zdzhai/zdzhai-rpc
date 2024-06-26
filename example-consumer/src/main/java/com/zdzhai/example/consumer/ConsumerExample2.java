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
public class ConsumerExample2 {
    public static void main(String[] args) {

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        log.info("消费端获取rpcConfig成功：{}", rpcConfig);

        OrderService orderService = ServiceProxyFactory.getProxy(OrderService.class);

        Order order = new Order();
        order.setOrderId(0000L);
        //调用
        Order newOrder = orderService.getOrder(order);
        if (newOrder != null) {
            System.out.println("newOrder:" +newOrder.getOrderId());
        } else {
            System.out.println("order == null");
        }
    }
}
