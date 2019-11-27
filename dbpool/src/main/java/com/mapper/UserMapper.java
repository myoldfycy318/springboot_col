package com.mapper;

import com.aspect.IgnoreHintManager;
import com.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper {

    void insert(User user);

    User selectById(Integer id);

    List<User> queryUsers();

    int saveSendHist(Map<String, String> map);

    User selectByUserId(Integer id);

}
