package com.bluetron.chat.utils;

import java.io.UnsupportedEncodingException;
 
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
/**
 * base64 编码 〈一句话功能简述〉 〈功能详细描述〉
 * 
 * @version 2017年7月25日
 * @see Base64
 * @since
 */
public class Base64 {
	/**
	 * 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(byte[] b) {
		String s = null;
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}
 
	/**
	 * 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(String src) {
		return encode(src.getBytes());
	}
 
	/**
	 * 解密
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decode(String s) {
		byte[] b = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}
 
	/**
	 * 解密
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeStr(String s) {
		byte[] b = decode(s);
		try {
			return b != null ? (new String(b, "UTF-8")) : null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
