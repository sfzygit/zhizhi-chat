package com.bluetron.push.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bluetron.push.request.body.AndroidPushParam;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
	
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/serviceA")
	public String serviceA() {
		
		return "serviceA from push-service demo";
	}
	
	@PostMapping("/service/test_a")
	public String testa( @RequestBody AndroidPushParam pushParam
			) {
		return JSON.toJSONString(pushParam);
	}
	
	@PostMapping("/service/test_b")
	public String testb(
			@RequestBody AndroidPushParam pushParam ) {
		String url = "http://localhost:8090/demo/service/test_a";
		JSONObject postBody = JSON.parseObject(JSON.toJSON(pushParam).toString());
		
		JSONObject resBody = restTemplate.postForEntity(url, postBody, JSONObject.class).getBody();
		return resBody.toJSONString();
	}
}
