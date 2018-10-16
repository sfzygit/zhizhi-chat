package com.bluetron.push.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bluetron.push.um.android.UmPushparamAndriod;
import com.bluetron.push.utils.Constants;
import com.bluetron.push.utils.MD5Util;

@Service
public class PushService {
	
	/**
	 * push message to mobile device(s)
	 * @param pushparamAndriod
	 * @return
	 * @throws Exception
	 */
	public String pushMessage(UmPushparamAndriod pushparamAndriod) throws Exception{
		String method = "POST";
		String url = Constants.UM_HOST+Constants.UM_SEND_API_PATH;
		String postBodyStr = JSON.toJSON(pushparamAndriod).toString();
		// md5encode $method$url$requestBody$app_master_secret
		String toEncodeMsg =  method+url+postBodyStr+Constants.UM_APP_MASTER_SECRET;
		String mysign="";
		
		try {
			mysign = MD5Util.md5Encode(toEncodeMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		url += "?sign="+mysign;
		HttpPost post = new HttpPost(url);
		// The user agent
		String USER_AGENT = "Mozilla/5.0";
		post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBodyStr, "UTF-8");
        post.setEntity(se);
       
        // Send the post request and get the response
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
}
