package jpa.service.impl;

/**
 * Created by shanmin.zhang
 * Date: 18/5/29
 * Time: 下午3:25
 */

import com.alibaba.fastjson.JSONObject;
import jpa.domain.User;
import jpa.repository.UserJpaRepository;
import jpa.repository.UserRepository;
import jpa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    public List<User> findByName(String name) {
        List<User> userList1 = null;//userRepository.findByName1(name);
        List<User> userList2 = userRepository.findByName2(name);
        List<User> userList3 = userRepository.findByNameAndAddress(name, "3");
        System.out.println("userList1:" + JSONObject.toJSONString(userList1));
        System.out.println("userList2:" + JSONObject.toJSONString(userList2));
        System.out.println("userList3:" + userList3);
        return userRepository.findByName(name);
    }

    public User saveUser(User book) {
       return userJpaRepository.save(book);
    }

    @Override
    @Cacheable("users")
    public User findOne(long id) {
        System.out.println("Cached Pages");
        return userJpaRepository.findOne(id);
    }

    public void delete(long id) {
        userJpaRepository.delete(id);
    }
}
