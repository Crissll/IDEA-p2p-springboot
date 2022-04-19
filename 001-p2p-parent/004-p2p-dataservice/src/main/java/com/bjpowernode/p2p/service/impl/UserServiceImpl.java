package com.bjpowernode.p2p.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.RedisService;
import com.bjpowernode.p2p.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;



    @Override
    public Integer queryAllUserCount() {
        Integer allUserCount = (Integer )redisService.getRedisCode(Constants.ALL_USER_COUNT);
        if (!ObjectUtils.allNotNull(allUserCount)){
            synchronized (this){
                allUserCount = (Integer )redisService.getRedisCode(Constants.ALL_USER_COUNT);
                if (!ObjectUtils.allNotNull(allUserCount)){
                    allUserCount = userMapper.selectAllUserCount();
                    redisService.put(Constants.ALL_USER_COUNT, allUserCount,3, TimeUnit.HOURS);
                }
            }
        }
        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    @Transactional
    public User addUser(String phone, String loginPassword) throws Exception {
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int rows = userMapper.insertSelective(user);
        if (rows ==0){
            throw new Exception("注册失败");
        }

//        User userDetail = userMapper.selectUserByPhone(phone);
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(user.getId());
        financeAccount.setAvailableMoney(Constants.ACTIVITY_REWARD);
        int faRows = financeAccountMapper.insertSelective(financeAccount);
        if (faRows==0){
            throw new Exception("奖励领取失败");
        }

        return user;
    }

    @Override
    public int modifyUserByRealName(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {
        return userMapper.selectLogin(phone,loginPassword);
    }


}
