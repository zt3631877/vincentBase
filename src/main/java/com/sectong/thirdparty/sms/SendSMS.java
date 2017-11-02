package com.sectong.thirdparty.sms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.alibaba.fastjson.JSON;
import com.sectong.thirdparty.sms.request.SmsBalanceRequest;
import com.sectong.thirdparty.sms.response.SmsBalanceResponse;
import com.sectong.thirdparty.sms.util.ChuangLanSmsUtil;

/**
 * ������������������
 * 
 * @author jiekechoo
 *
 */
public class SendSMS {


	/**
	 * ������������������������������
	 * 
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String checkAccountStatus(String account, String password) throws Exception {
		  String smsBalanceRequestUrl = "https://smssh1.253.com/msg/balance/json";
			
			SmsBalanceRequest smsBalanceRequest=new SmsBalanceRequest(account, password);
			
	        String requestJson = JSON.toJSONString(smsBalanceRequest);
			
		//	System.out.println("before request string is: " + requestJson);
			
			String response = ChuangLanSmsUtil.sendSmsByPost(smsBalanceRequestUrl, requestJson);
			
		//	System.out.println("response after request result is : " + response);
			
			SmsBalanceResponse smsVarableResponse = JSON.parseObject(response, SmsBalanceResponse.class);
			
		//	System.out.println("response  toString is : " + smsVarableResponse.getErrorMsg());
			if(smsVarableResponse.getErrorMsg()==null || "".equals(smsVarableResponse.getErrorMsg())){
				return "1";
			}else{
				return smsVarableResponse.getErrorMsg();
			}
 
	}

}