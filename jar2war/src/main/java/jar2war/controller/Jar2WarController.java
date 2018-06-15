package jar2war.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shanmin.zhang
 * Date: 18/6/7
 * Time: 下午3:10
 */

@RestController
public class Jar2WarController {

    @GetMapping("jar2war")
    public String jar2war() {
        return "jar2war.......";
    }


    @GetMapping("/h/{api}")
    public String xx(@RequestParam String api){
        return api;
    }



}
