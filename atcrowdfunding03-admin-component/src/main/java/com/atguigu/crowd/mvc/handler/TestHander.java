package com.atguigu.crowd.mvc.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestHander {
	@ResponseBody
	@RequestMapping("/test/ajax/async.html")
	public String testAsync() throws InterruptedException {
		//Thread.sleep(2000);
		return "succeess";
	}
}
