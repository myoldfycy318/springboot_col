package jpa;

import jpa.domain.User;
import jpa.repository.UserJpaRepository;
import jpa.service.IUserService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataJpaApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private IUserService iUserService;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	public void 保存(){

		User user = new User();
		user.setAddress("地址xx");
		user.setName("名字ccc`1");

		User user1 = iUserService.saveUser(user);
		System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(user1));
	}

	@Test
	public void 查询(){

		String [] _name = {"名字ccc`1","xx"};
		User user = userJpaRepository.findTopByNameInOrderByNameAsc(_name);
		System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(user));
	}

}
