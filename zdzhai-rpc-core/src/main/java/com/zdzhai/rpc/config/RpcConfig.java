package com.zdzhai.rpc.config;

import com.zdzhai.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author dongdong
 * @Date 2024/6/22 16:58
 * RPC框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "zdzhai-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 是否开启mock模拟调用,默认关闭
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;
}
