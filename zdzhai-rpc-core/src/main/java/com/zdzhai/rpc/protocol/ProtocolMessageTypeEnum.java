package com.zdzhai.rpc.protocol;

import lombok.Getter;

/**
 * @author dongdong
 * @Date 2024/6/27 13:27
 * 协议消息状态枚举类
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;


    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum statusEnum : ProtocolMessageTypeEnum.values()) {
            if (statusEnum.key == key) {
                return statusEnum;
            }
        }
        return null;
    }
}
