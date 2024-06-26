package com.zdzhai.rpc.registry;

import com.zdzhai.rpc.config.RegistryConfig;
import com.zdzhai.rpc.model.ServiceMetaInfo;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author dongdong
 * @Date 2024/6/25 10:27
 */
public class RegistryTest{

    final EtcdRegistry etcdRegistry = new EtcdRegistry();

    @Before
    public void testInit() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2380");
        etcdRegistry.init(registryConfig);
        //创建并注册ShutDown Hook, JVM退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(etcdRegistry::destroy));
    }

    @Test
    public void testRegister() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        etcdRegistry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        etcdRegistry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        etcdRegistry.register(serviceMetaInfo);

    }

    @Test
    public void testUnRegister() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        etcdRegistry.unRegister(serviceMetaInfo);
    }

    @Test
    public void testServiceDiscovery() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = etcdRegistry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfoList);
    }

    @Test
    public void testHeartBeat() throws Exception {
        testRegister();
        Thread.sleep(60 * 5000L);
    }

}