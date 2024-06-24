package com.zdzhai.rpc.server;

import com.zdzhai.rpc.RpcApplication;
import com.zdzhai.rpc.model.RpcRequest;
import com.zdzhai.rpc.model.RpcResponse;
import com.zdzhai.rpc.registry.LocalRegistry;
import com.zdzhai.rpc.serializer.JdkSerializer;
import com.zdzhai.rpc.serializer.Serializer;
import com.zdzhai.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author dongdong
 * @Date 2024/6/21 17:32
 * 请求处理器
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {


    //1.反序列化请求参数为对象，并从请求对象中获取参数。
    //2.根据服务名称从本地注册器中获取到对应的服务实现类。
    //3.通过反射机制调用方法，得到返回结果。
    //4.对返回结果进行封装和序列化，并写入到响应中。

    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //记录日志
        System.out.println("Received request:" + request.method() + " " + request.uri());

        //异步处理http请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            //System.out.println("HttpServerHandler:" + Arrays.toString(bytes));
            RpcRequest rpcRequest = null;

            try {
                 rpcRequest = serializer.deserializer(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                //对rpcResponse序列化并响应
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //序列化rpcResponse并响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            //序列化
            byte[] bytes = serializer.serializer(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
