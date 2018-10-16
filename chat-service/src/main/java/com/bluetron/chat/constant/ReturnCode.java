package com.bluetron.chat.constant;

import lombok.Data;

public enum ReturnCode {

	SECCESS("200","成功"),
	ERROR("400","失败");
	String code;
	String description;
	
	private  ReturnCode(String code, String description) {
		this.code = code;
		this.description = description;
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
