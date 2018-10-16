package com.bluetron.push.um.android;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class PayloadBody implements Serializable{

	 	String ticker;    // 必填，通知栏提示文字
	    String title;    // 必填，通知标题
	    String text;    // 必填，通知文字描述 
	
	    				// 自定义通知图标:
	    String icon;    // 可选，状态栏图标ID，R.drawable.[smallIcon]，
					    // 如果没有，默认使用应用图标。
					    // 图片要求为24*24dp的图标，或24*24px放在drawable-mdpi下。
					    // 注意四周各留1个dp的空白像素
	    
	    String largeIcon;    // 可选，通知栏拉开后左侧图标ID，R.drawable.[largeIcon]，
	    // 图片要求为64*64dp的图标，
	    // 可设计一张64*64px放在drawable-mdpi下，
	    // 注意图片四周留空，不至于显示太拥挤
	    String img;    // 可选，通知栏大图标的URL链接。该字段的优先级大于largeIcon。
	                    // 该字段要求以http或者https开头。
	
	    // 自定义通知声音:
	    String sound;    // 可选，通知声音，R.raw.[sound]。
	                    // 如果该字段为空，采用SDK默认的声音，即res/raw/下的
	                    // umeng_push_notification_default_sound声音文件。如果
	                    // SDK默认声音文件不存在，则使用系统默认Notification提示音。
	
	    				// 自定义通知样式:
	    String builder_id;   // 可选，默认为0，用于标识该通知采用的样式。使用该参数时，
	                        // 开发者必须在SDK里面实现自定义通知栏样式。
	
	    								// 通知到达设备后的提醒方式，注意，"true/false"为字符串
	    String play_vibrate;    // 可选，收到通知是否震动，默认为"true"
	    String play_lights;        // 可选，收到通知是否闪灯，默认为"true"
	    String play_sound;        // 可选，收到通知是否发出声音，默认为"true"
	
	    						// 点击"通知"的后续行为，默认为打开app。
	    String after_open;    // 可选，默认为"go_app"，值可以为:
	                        //   "go_app": 打开应用
	                        //   "go_url": 跳转到URL
	                        //   "go_activity": 打开特定的activity
	                        //   "go_custom": 用户自定义内容。
	    String url;    // 当after_open=go_url时，必填。
	                    // 通知栏点击后跳转的URL，要求以http或者https开头
	    String activity;    // 当after_open=go_activity时，必填。
	                        // 通知栏点击后打开的Activity
	    Object custom;//{}    // 当display_type=message时, 必填
	                        // 当display_type=notification且
	                        // after_open=go_custom时，必填
	                        // 用户自定义内容，可以为字符串或者JSON格式。
	    
	    Map<String, String> extra; // 可选，JSON格式，用户自定义key-value。只对"通知"
	                               // (display_type=notification)生效。
	                               // 可以配合通知到达后，打开App/URL/Activity使用。

}
