package com.bluetron.push.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bluetron.push.request.body.AndroidPushParam;
import com.bluetron.push.service.PushService;
import com.bluetron.push.um.android.UmPushparamAndriod;
import com.bluetron.push.utils.Constants;
import com.bluetron.push.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/push")
@Slf4j
public class PushController {
	
	@Autowired
	RestTemplate mRestTemplate;
	
	@Autowired PushService pushService;
	
	@PostMapping(value="/send")
	public String sendUnicast(
			@RequestBody AndroidPushParam pushParam
			) {
		log.info("invoke api : /push/send, requestBody: pushParam="+JSON.toJSONString(pushParam));
		
		UmPushparamAndriod pushparamAndriod = new UmPushparamAndriod();
		pushparamAndriod.setPushParam(pushParam);
		log.info("UmPushparamAndriod ="+JSON.toJSONString(pushparamAndriod));
		String result;
		try {
			result = pushService.pushMessage(pushparamAndriod);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
}
