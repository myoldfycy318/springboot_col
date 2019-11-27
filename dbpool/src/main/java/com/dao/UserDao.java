package com.dao;

import com.aspect.IgnoreHintManager;
import com.entity.User;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@IgnoreHintManager
public class UserDao implements UserMapper {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(User user) {

    }

    @Override
    public User selectById(Integer id) {
        return null;
    }

    @Override
    public List<User> queryUsers() {
        return userMapper.queryUsers();
    }

    @Override
    public int saveSendHist(Map<String, String> map) {
        return 0;
    }

    @Override
    public User selectByUserId(Integer id) {
        return userMapper.selectByUserId(id);
    }

}
