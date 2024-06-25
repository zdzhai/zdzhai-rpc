package com.zdzhai.rpc.config;

import lombok.Data;

/**
 * @author dongdong
 * @Date 2024/6/24 11:28
 * RPC框架注册中心配置类
 * 让用户配置连接注册中心的信息
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类型
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 过期时间（毫秒）
     */
    private Long timeout = 10000L;

}
