package sb_websocket.com;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 * Created by shanmin.zhang
 * Date: 18/8/14
 * Time: 下午6:54
 */
@RestController
public class HellowWorld {


    @RequestMapping("/h/{userId}")
    public String say(@PathVariable("userId") String userId) {
        return "xx->" + userId;
    }
}
