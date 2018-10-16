package com.bluetron.chat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.MimeMappings.Mapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bluetron.chat.constant.ReturnCode;
import com.bluetron.chat.dto.ReturnObject;
import com.bluetron.chat.service.PushService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;



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
			return pushService.getRongtokenByHttpClient(userId,userName,portraitUri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("{token:null,").append("message:"+e.getMessage()+"}");
			return stringBuilder.toString();
		}
		
	}
	
	@RequestMapping(value="/user/sendImageMessage", method=RequestMethod.POST)
	public ReturnObject requestMethodName(
			@RequestParam String senderId,
			@RequestParam String[] targetId,
			@RequestParam MultipartFile file) {
		
		ReturnObject returnObject = new ReturnObject();
		
		try {
			if(pushService.sendImgMessage(senderId, targetId, file)) {
				returnObject.setCode(ReturnCode.SECCESS.getCode());
				returnObject.setMessage(ReturnCode.SECCESS.getDescription());
			}else {
				returnObject.setCode(ReturnCode.ERROR.getCode());
				returnObject.setMessage("发送消息失败！");;
			}
			
		}catch(Exception e) {
			
			returnObject.setCode(ReturnCode.ERROR.getCode());
			returnObject.setMessage(e.getMessage());;
		}
		
		return returnObject;
	}
	
}
