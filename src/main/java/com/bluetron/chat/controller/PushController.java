package com.bluetron.chat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluetron.chat.service.PushService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im")
public class PushController {

	@Autowired
	PushService pushService;
	
	@GetMapping("/user/getRongtoken")
	public String getIMtoken(
			@RequestParam String userId,
			@RequestParam String userName) {
		
		try {
			String portraitUri = "http://www.rongcloud.cn/images/logo.png";
			return pushService.getRongToken(userId,userName,portraitUri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("{token:null,").append("message:"+e.getMessage()+"}");
			return stringBuilder.toString();
		}
		
	}
	
	@PostMapping("/user/send/single")
	public String sendToSingle(
			@RequestParam String senderId,
			@RequestParam String sendToId,
			@RequestParam String message
			){
		String[] sendToArray;
		sendToArray = sendToId.split(",");
		try {
			return pushService.sendToSingle(senderId, sendToArray, message);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@PostMapping(value="/user/get/history")
	public String postMethodName(
			@RequestParam String userId) {
		
		try {
			return pushService.getHistoryMessage(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
	}
	
}
