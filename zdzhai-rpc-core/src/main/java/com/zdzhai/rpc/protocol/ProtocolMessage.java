package com.zdzhai.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dongdong
 * @Date 2024/6/27 13:19
 * 自定义协议消息结构类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;


    /**
     * 协议消息头
     */
    @Data
    public static class Header {

        /**
         * 魔数（保证安全）
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化方式
         */
        private byte serializer;

        /**
         * 消息类型
         */
        private byte type;

        /**
         * 消息状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}
