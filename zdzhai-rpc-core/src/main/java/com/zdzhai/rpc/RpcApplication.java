package com.zdzhai.rpc;

import com.zdzhai.rpc.config.RegistryConfig;
import com.zdzhai.rpc.config.RpcConfig;
import com.zdzhai.rpc.constant.RpcConstant;
import com.zdzhai.rpc.registry.Registry;
import com.zdzhai.rpc.registry.RegistryFactory;
import com.zdzhai.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongdong
 * @Date 2024/6/22 17:16
 * RPC框架应用类
 * 相当于holder,存放了项目全局用到的变量，使用双重检锁式单例实现
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 支持自定义传入配置
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", rpcConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);

        //创建并注册ShutDown Hook, JVM退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 框架初始化，读取配置文件
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREDIX);
        } catch (Exception e) {
            log.error("配置文件初始化错误，{}",e);
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置对象
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
