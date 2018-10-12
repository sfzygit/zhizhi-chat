package com.bluetron.chat.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Signature;
import java.util.Calendar;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.springframework.stereotype.Service;

import com.bluetron.chat.utils.Constants;
import com.bluetron.chat.utils.SHAUtil;

import io.rong.RongCloud;
import io.rong.messages.TxtMessage;
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

@Service
public class PushService {

	
	public String getRongToken(String userId, String userName, String portraitUri ) throws Exception {
		
		RongCloud rongCloud = RongCloud.getInstance(Constants.IM_APP_KEY, Constants.IM_APP_SECRET);
		
		UserModel userModel = new UserModel().setId(userId)
											.setName(userName)
											.setPortrait(portraitUri);
		
		TokenResult tokenResult = rongCloud.user.register(userModel);
		
		return tokenResult.toString();
	}
	
	public String getIMtoken(String userId, String userName, String portraitUri) throws ClientProtocolException, IOException {
		Random random = new Random(25);
		
		String nonce = String.valueOf(random.nextInt(1000000));
		String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String signature = SHAUtil.sha1Encode(Constants.IM_APP_KEY+nonce+timestamp);
		String url = Constants.IM_GET_TOKEN_URL;
		url += ".json";
		
		HttpPost post = new HttpPost(url);
		String USER_AGENT = "Mozilla/5.0";
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("App-Key", Constants.IM_APP_KEY);
		post.setHeader("Nonce",nonce);
		post.setHeader("Timestamp",timestamp);
		post.setHeader("Signature",signature);
		
		HttpParams params = post.getParams();
		params.setParameter("userId", "0");
		params.setParameter("name", "user_0");
		if(portraitUri != null && !portraitUri.isEmpty()) {
			params.setParameter("portraitUri", portraitUri);
		}
		post.setParams(params);
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
        StringBuffer result = new StringBuffer();
		response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
		rd.close();
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
	
}
