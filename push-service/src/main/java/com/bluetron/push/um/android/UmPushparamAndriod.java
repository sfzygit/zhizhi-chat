package com.bluetron.push.um.android;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.Adapter;

import com.bluetron.push.request.body.AndroidPushParam;
import com.bluetron.push.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UmPushparamAndriod implements Serializable{
	
	String appKey; // 必填，应用唯一标识
	String timestamp; // 必填，时间戳，10位或者13位均可，时间戳有效期为10分钟
	String type;// 必填，消息发送类型,其值可以为: 
				    //   unicast-单播
				    //   listcast-列播，要求不超过500个device_token
				    //   filecast-文件播，多个device_token可通过文件形式批量发送
				    //   broadcast-广播
				    //   groupcast-组播，按照filter筛选用户群, 请参照filter参数
				    //   customizedcast，通过alias进行推送，包括以下两种case:
				    //     - alias: 对单个或者多个alias进行推送
				    //     - file_id: 将alias存放到文件后，根据file_id来推送
	String device_tokens;// 当type=unicast时, 必填, 表示指定的单个设备
						// 当type=listcast时, 必填, 要求不超过500个, 以英文逗号分隔
	
	String alias_type;// 当type=customizedcast时, 必填
    				  // alias的类型, alias_type可由开发者自定义, 开发者在SDK中
    				  // 调用setAlias(alias, alias_type)时所设置的alias_type
	String alias;  // 当type=customizedcast时, 选填(此参数和file_id二选一)
	    			   // 开发者填写自己的alias, 要求不超过500个alias, 多个alias以英文逗号间隔
	    			   // 在SDK中调用setAlias(alias, alias_type)时所设置的alias
	String file_id;
	
	Object filter;// 当type=groupcast时，必填，用户筛选条件，如用户标签、渠道等，参考附录G。
	
	Payload payload = new Payload();// 必填，JSON格式，具体消息内容(Android最大为1840B)
	
    Object policy;	// 可选，发送策略
					//policy = {  "start_time":"xx",    // 可选，定时发送时，若不填写表示立即发送。
					//    // 定时发送时间不能小于当前时间
					//    // 格式: "yyyy-MM-dd HH:mm:ss"。 
					//    // 注意，start_time只对任务类消息生效。
					//	"expire_time":"xx",    // 可选，消息过期时间，其值不可小于发送时间或者
					//	    // start_time(如果填写了的话)，
					//	    // 如果不填写此参数，默认为3天后过期。格式同start_time
					//	"max_send_num": xx,    // 可选，发送限速，每秒发送的最大条数。最小值1000
					//	    // 开发者发送的消息如果有请求自己服务器的资源，可以考虑此参数。
					//	"out_biz_no": "xx"    // 可选，开发者对消息的唯一标识，服务器会根据这个标识避免重复发送。
					//	    // 有些情况下（例如网络异常）开发者可能会重复调用API导致
					//	    // 消息多次下发到客户端。如果需要处理这种情况，可以考虑此参数。
					//	    // 注意, out_biz_no只对任务类消息生效。
					//   } 
    String production_mode;    // 可选，正式/测试模式。默认为true
						    	// 测试模式只会将消息发给测试设备。测试设备需要到web上添加。
							    // Android: 测试设备属于正式设备的一个子集。
	String description;    // 可选，发送消息描述，建议填写。  
	String mipush;    // 可选，默认为false。当为true时，表示MIUI、EMUI、Flyme系统设备离线转为系统下发
	String mi_activity;    // 可选，mipush值为true时生效，表示走系统通道时打开指定页面acitivity的完整包路径。
	

	// generate post body demo
	public static UmPushparamAndriod generateUmPushParamDemo() {
		UmPushparamAndriod pushparamAndriod = new UmPushparamAndriod();
		pushparamAndriod.setDescription("zhizhi测试");
		pushparamAndriod.setProduction_mode("false");
		pushparamAndriod.setAppKey(Constants.UM_APP_KEY);
		pushparamAndriod.setType("broadcast");
		// set current time
		pushparamAndriod.setTimestamp(Calendar.getInstance().getTimeInMillis()+"");
		// set message expire time
		Calendar expireTime = Calendar.getInstance();
		expireTime.add(Calendar.DAY_OF_MONTH, 6);
		Map<String,String> policy = new HashMap<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		policy.put("expire_time",simpleDateFormat.format(expireTime.getTime()));
		pushparamAndriod.setPolicy(policy);
		// set message body
		pushparamAndriod.getPayload().setDisplay_type("notification");
		pushparamAndriod.getPayload().getBody().setTitle("测试");
		pushparamAndriod.getPayload().getBody().setTicker("测试");
		pushparamAndriod.getPayload().getBody().setText("消息来自zhizhi server");
		pushparamAndriod.getPayload().getBody().setAfter_open("go_app");
		pushparamAndriod.getPayload().getBody().setPlay_vibrate("false");
		pushparamAndriod.getPayload().getBody().setPlay_lights("false");
		pushparamAndriod.getPayload().getBody().setPlay_sound("true");
		
		return pushparamAndriod;
		
	}
	
	// set UM push message parameter
	public void setPushParam(AndroidPushParam androidPushParam) {
		this.device_tokens = androidPushParam.getDevice_tokens();
		this.description = androidPushParam.getDescription();
		this.production_mode = androidPushParam.getProduction_mode();
		this.mipush = "true";
		this.mi_activity = Constants.UM_APP_PACKAGE_NAME;
		this.appKey = Constants.UM_APP_KEY;
		this.type = "unicast";//"unicast";//"broadcast";//"listcast"; // 单播/广播/列播模式
		this.timestamp = Calendar.getInstance().getTimeInMillis()+""; // set current time
		// set expire time
		Map<String,String> policy = new HashMap<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(androidPushParam.getExpire_time() != null && !androidPushParam.getExpire_time().isEmpty()) {
			policy.put("expire_time",androidPushParam.getExpire_time());
		}else {
			
			Calendar expireTime = Calendar.getInstance();
			expireTime.add(Calendar.DAY_OF_MONTH, 6);
			policy.put("expire_time",simpleDateFormat.format(expireTime.getTime()));
		}
		setPolicy(policy);
		// set message body
		this.payload.setDisplay_type(androidPushParam.getDisplay_type());
		this.payload.getBody().setTitle(androidPushParam.getTitle()+" "+simpleDateFormat.format(Calendar.getInstance().getTime()));
		this.payload.getBody().setTicker(androidPushParam.getTicker()+" "+simpleDateFormat.format(Calendar.getInstance().getTime()));
		this.payload.getBody().setText(androidPushParam.getText());
		this.payload.getBody().setAfter_open(androidPushParam.getAfter_open());
		this.payload.getBody().setUrl(androidPushParam.getUrl());
		this.payload.getBody().setPlay_vibrate("false");
		this.payload.getBody().setPlay_lights("false");
		this.payload.getBody().setPlay_sound("true");
		
	}
	
	

	
	
}
