package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.user.User;

public interface UserService {
    Integer queryAllUserCount();

    User queryUserByPhone(String phone);

    User addUser(String phone, String loginPassword) throws Exception;

    int modifyUserByRealName(User user);

    User login(String phone, String loginPassword);
}
