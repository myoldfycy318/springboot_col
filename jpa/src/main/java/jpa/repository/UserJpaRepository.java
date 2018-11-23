package jpa.repository;

/**
 * Created by shanmin.zhang
 * Date: 18/5/29
 * Time: 下午3:27
 */

import jpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findTopByName(String _name);

    User findTopByNameInOrderByNameAsc(String[] _name);

}
