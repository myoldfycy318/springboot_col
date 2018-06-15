package jpa.service;

/**
 * Created by shanmin.zhang
 * Date: 18/5/29
 * Time: 下午3:25
 */
import jpa.domain.User;

import java.util.List;


public interface IUserService
{
    public List<User> findAll();

    public User saveUser(User book);

    public User findOne(long id);

    public void delete(long id);

    public List<User> findByName(String name);

}