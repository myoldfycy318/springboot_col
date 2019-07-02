package com.szw.learn.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("websocket")
public class WebsocketController {
	
	@Value("${server.port}")
	private String port;
	
	public static final String INDEX = "websocket/index";
	
	/**
	 * @author 七脉
	 * 描述：聊天页
	 * @param topic 发布订阅的频道主题
	 * @param myname 发布者的显示名称
	 * @return
	 */
	@RequestMapping("index/{topic}/{myname}")
	public ModelAndView index(@PathVariable("topic")String topic,@PathVariable("myname")String myname){
		ModelAndView mav = new ModelAndView(INDEX);
		mav.addObject("port", port);
		mav.addObject("topic",topic);
		mav.addObject("myname",myname);
		return mav;
	}
}
