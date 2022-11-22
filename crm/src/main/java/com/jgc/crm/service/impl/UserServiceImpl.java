package com.jgc.crm.service.impl;

import com.jgc.crm.service.UserService;
import com.jgc.crm.settings.mapper.UserMapper;
import com.jgc.crm.settings.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
   private UserMapper userMapper;
    @Override
    public User querySelectByLoginActAndLoginPwd(Map<String, Object> map) {
        User user=userMapper.selectUserByLoginActAndLoginPwd(map);
        return user;
    }

    @Override
    public List<User> queryUsers() {
        List<User> userList = userMapper.queryUsers();
        return userList;
    }
}
