package com.bluetron.push.um.android;

import java.io.Serializable;

import com.bluetron.push.um.android.PayloadBody;

import lombok.Data;

@Data
public class Payload implements Serializable{
	
	String display_type;// 必填，消息类型: notification(通知)、message(消息)
	
	PayloadBody body = new PayloadBody(); // 必填，消息体。
											// 当display_type=message时，body的内容只需填写custom字段。
											// 当display_type=notification时，body包含如下参数:
											// 通知展现内容:


}
