package jpa.repository;

/**
 * Created by shanmin.zhang
 * Date: 18/5/29
 * Time: 下午3:23
 */

import jpa.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends Repository<User, Long> {

    List<User> findByNameAndAddress(String name, String address);

    @Query(value = "from User u where u.name=:name")
    List<User> findByName1(@Param("name") String name);

    @Query(value = "select * from #{#entityName} u where u.name=?1", nativeQuery = true)
    List<User> findByName2(String name);

    /**
     * 由于我们在实体类中声明了@NamedQuery注解，实际上findByName方法会使用@NamedQuery注解标注的查询语句去查询；
     * @param name
     * @return
     */
    List<User> findByName(String name);
}