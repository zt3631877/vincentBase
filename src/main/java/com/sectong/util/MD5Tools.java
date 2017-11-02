package com.sectong.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * MD5加密工具类 <功能详细描述>
 * 
 * @author chenlujun
 * @version [版本号, 2014年10月1日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class MD5Tools {
	public static String getMD5Str(String str) { 
		String md5str ="";
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
	//	DateUtil.DateToString(DateUtil.addDay(new Date(),20),DateStyle.YYYY_MM_DD);
 
		// 16位加密，从第9位到25�?
		md5str = md5StrBuff.substring(10, 29).toString().toUpperCase()+DateUtil.addDay(new Date(),20).getDay()+md5StrBuff.substring(2, 15).toString().toUpperCase()+DateUtil.addDay(new Date(),20).getYear()+md5StrBuff.substring(5, 16).toString().toUpperCase()+DateUtil.addDay(new Date(),20).getMonth();
	
		return md5str.substring(18, 25)+md5str.substring(0, 8);

	}
	//获取二维码内容
	public static String getMD5StrFormTime(String str) { 
		String  md5str="";
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
	//	DateUtil.DateToString(DateUtil.addDay(new Date(),20),DateStyle.YYYY_MM_DD);

        md5str=md5StrBuff.substring(1, 29).toString().toUpperCase()+DateUtil.DateToString(DateUtil.addMinute(new Date(), 10),DateStyle.YYYYMMDDHHMM)+md5StrBuff.substring(2, 15).toString().toUpperCase()+md5StrBuff.substring(5, 26).toString().toUpperCase();
		return getMD5Str(md5str);

	}
	//比较二维码内容
	public static String[] getMD5StrFormTimes(String str) { 
		String[] md5str=new String[5];
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
	//	DateUtil.DateToString(DateUtil.addDay(new Date(),20),DateStyle.YYYY_MM_DD);
		//5分钟有效
		 for(int i=10;i<15;i++){
			 md5str[i-10]=getMD5Str(md5StrBuff.substring(1, 29).toString().toUpperCase()+DateUtil.DateToString(DateUtil.addMinute(new Date(), i-4),DateStyle.YYYYMMDDHHMM)+md5StrBuff.substring(2, 15).toString().toUpperCase()+md5StrBuff.substring(5, 26).toString().toUpperCase());
		 }
	 
	
		return md5str;

	}
	public static String getEncryption(String str) { 
		return getMD5Str(getMD5Str(str));
	}
	 
	
}