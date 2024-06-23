package com.zdzhai.example.common.service;

import com.zdzhai.example.common.model.User;

/**
 * @author dongdong
 * @Date 2024/6/20 20:54
 */
public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * 测试Mock功能
     * @return
     */
    default short getNumber() {
        return 1;
    }
}
