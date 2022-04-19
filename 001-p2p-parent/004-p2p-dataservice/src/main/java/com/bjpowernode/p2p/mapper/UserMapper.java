package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.user.User;

import javax.websocket.server.PathParam;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Integer selectAllUserCount();

    /**
     * 根据手机号码查询用户数据
     * @param phone
     * @return
     */
    User selectUserByPhone(String phone);

    User selectLogin(@PathParam("phone") String phone,@PathParam("loginPassword") String loginPassword);
}