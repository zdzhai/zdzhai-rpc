package com.zdzhai.rpc.registry;

import com.zdzhai.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author dongdong
 * @Date 2024/6/26 12:54
 * 注册中心服务缓存
 */
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param serviceCache
     */
    void writeCache(List<ServiceMetaInfo> serviceCache) {
        this.serviceCache = serviceCache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache() {
        this.serviceCache = null;
    }
}
