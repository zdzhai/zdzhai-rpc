package com.zdzhai.example.common.model;

import java.io.Serializable;

/**
 * @author dongdong
 * @Date 2024/6/20 20:52
 * 用户
 */
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
