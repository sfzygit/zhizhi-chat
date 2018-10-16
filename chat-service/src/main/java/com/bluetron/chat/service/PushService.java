package com.bluetron.chat.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bluetron.chat.utils.Constants;
import com.bluetron.chat.utils.SHAUtil;

import io.rong.RongCloud;
import io.rong.messages.ImgMessage;
import io.rong.messages.TxtMessage;
import io.rong.messages.VoiceMessage;
import io.rong.methods.message._private.Private;
import io.rong.methods.message.chatroom.Chatroom;
import io.rong.methods.message.discussion.Discussion;
import io.rong.methods.message.group.Group;
import io.rong.methods.message.history.History;
import io.rong.methods.message.system.MsgSystem;
import io.rong.methods.user.User;
import io.rong.models.message.PrivateMessage;
import io.rong.models.response.HistoryMessageResult;
import io.rong.models.response.ResponseResult;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushService {

	@Autowired
	FileService fileService; 
	
	public String getRongToken(String userId, String userName, String portraitUri ) throws Exception {
		
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		
		UserModel userModel = new UserModel().setId(userId)
											.setName(userName)
											.setPortrait(portraitUri);
		
		TokenResult tokenResult = rongCloud.user.register(userModel);
		
		return tokenResult.toString();
	}
	
	public String getRongtokenByHttpClient(String userId, String userName, String portraitUri){
		Random random = new Random(25);
		
		String nonce = String.valueOf(random.nextInt(1000000));
		String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String signature = SHAUtil.sha1Encode(Constants.IM_APP_SECRET+nonce+timestamp);
		String url = Constants.IM_GET_TOKEN_URL;
		url += ".json";
		
		HttpPost post = new HttpPost(url);
		String USER_AGENT = "Mozilla/5.0";
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("App-Key", Constants.IM_APP_KEY);
		post.setHeader("Nonce",nonce);
		post.setHeader("Timestamp",timestamp);
		post.setHeader("Signature",signature);
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("name", userName));
		post.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));
		
		CloseableHttpClient  client =  HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		StringBuffer result = new StringBuffer();
		try {
			httpResponse = client.execute(post);
			String statusLine = httpResponse.getStatusLine().toString();
			log.info(statusLine);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity!=null) {
				InputStream iStream = httpEntity.getContent();
				BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(iStream, Consts.UTF_8));
				String line = "";
		        while ((line = bufferedReader.readLine()) != null) {
		            result.append(line);
		        }
		        iStream.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(httpResponse!=null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(client!=null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result.toString();
	}
	
	
	/**
	 * 发送单聊消息
	 * 发送频率：单个应用每分钟最多发送 6000 条信息，每次接收消息的用户上限为 1000 人，如：一次发送给 1000 人时，示为 1000 条消息。
	 * @return
	 * @throws Exception 
	 */
	public String sendToSingle(String senderId, String[] sendToId, String message) throws Exception {
		
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		Private Private = rongCloud.message.msgPrivate;
//        MsgSystem system = rongCloud.message.system;
//        Group group = rongCloud.message.group;
//        Chatroom chatroom = rongCloud.message.chatroom;
//        Discussion discussion = rongCloud.message.discussion;
//        History history = rongCloud.message.history;
        
        TxtMessage txtMessage = new TxtMessage(message, "");
        
        PrivateMessage privateMessage = new PrivateMessage()
                .setSenderId(senderId)
                .setTargetId(sendToId)
                .setObjectName(txtMessage.getType())
                .setContent(txtMessage)
                .setPushContent("")
                .setPushData("{\"pushData\":\"hello\"}")
                .setCount("4")
                .setVerifyBlacklist(0)
                .setIsPersisted(1)
                .setIsCounted(1)
                .setIsIncludeSender(0);
        ResponseResult privateResult = Private.send(privateMessage);
       
        
		return privateResult.toString();
	}
	
	public String getHistoryMessage(String userId) throws Exception {
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		History history = rongCloud.message.history;
		
		
		
		HistoryMessageResult historyMessageResult = history.get(userId);
		
		return historyMessageResult.toString();
	}
	
	/**
	 * send image to target user
	 * @param senderId
	 * @param targetId
	 * @param imageFile
	 * @return
	 */
	public boolean sendImgMessage(String senderId, String[] targetId, MultipartFile imageFile) {
		
		// return map
		boolean sendFlag = false;
		
		// save or upload file to server
		String fileUri = fileService.saveToLoalServer(imageFile);
		if(fileUri== null || fileUri.isEmpty()) {
			return sendFlag;
		}
		
		// send image message to target users
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		Private Private = rongCloud.message.msgPrivate;
		
		String content = fileService.imageBase64code(imageFile,".jpg");
		if(content == null || content.isEmpty()) {
			return sendFlag;
		}
		String extra = "";
		String imageUri = fileUri;
		ImgMessage imgMessage = new ImgMessage(content, extra, imageUri);
        PrivateMessage privateMessage = new PrivateMessage()
                .setSenderId(senderId)
                .setTargetId(targetId)
                .setObjectName(imgMessage.getType())
                .setContent(imgMessage)
                .setPushContent("")
                .setPushData("{\"pushData\":\"image message\"}")
                .setCount("4")
                .setVerifyBlacklist(0)
                .setIsPersisted(1)
                .setIsCounted(1)
                .setIsIncludeSender(0);
        
        try {
			ResponseResult privateResult = Private.send(privateMessage);
			if(privateResult != null && privateResult.toString().contains("200")) {
				sendFlag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return sendFlag;
	}
	
	/**
	 * send voice message to target user
	 * @param senderId
	 * @param targetId
	 * @param voiceFile
	 * @return
	 */
	public boolean sendVoiceMessage(String senderId, String[] targetId, MultipartFile voiceFile) {
		boolean sendFlag = false;
		// send voice message to target users
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		Private Private = rongCloud.message.msgPrivate;
		String content = fileService.imageBase64code(voiceFile,".amr");
		Long duration = 1L;
		if(content == null || content.isEmpty()) {
			return sendFlag;
		}
		VoiceMessage voiceMessage = new VoiceMessage(content, "", duration);
		PrivateMessage privateMessage = new PrivateMessage()
                .setSenderId(senderId)
                .setTargetId(targetId)
                .setObjectName(voiceMessage.getType())
                .setContent(voiceMessage)
                .setPushContent("")
                .setPushData("{\"pushData\":\"voice message\"}")
                .setCount("4")
                .setVerifyBlacklist(0)
                .setIsPersisted(1)
                .setIsCounted(1)
                .setIsIncludeSender(0);
        
        try {
			ResponseResult privateResult = Private.send(privateMessage);
			if(privateResult != null && privateResult.toString().contains("200")) {
				sendFlag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendFlag;
	}
	
	
}
