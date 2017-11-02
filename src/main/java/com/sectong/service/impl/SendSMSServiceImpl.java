package com.sectong.service.impl;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sectong.domain.Sms;
import com.sectong.domain.ThirdParty;
import com.sectong.repository.SmsRepository;
import com.sectong.repository.ThirdpartyRepository;
import com.sectong.service.SendSMSService;
import com.sectong.thirdparty.sms.SendSMS;
import com.sectong.thirdparty.sms.request.SmsSendRequest;
import com.sectong.thirdparty.sms.response.SmsSendResponse;
import com.sectong.thirdparty.sms.util.ChuangLanSmsUtil;

/**
 * 短信服务
 * 
 * @author vincent
 *
 */
@Service
public class SendSMSServiceImpl implements SendSMSService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SendSMSServiceImpl.class);
	private SmsRepository smsRepository;
	private ThirdpartyRepository thirdpartyRepository;

	@Autowired
	public SendSMSServiceImpl(SmsRepository smsRepository,
			ThirdpartyRepository thirdpartyRepository) {
		this.smsRepository = smsRepository;
		this.thirdpartyRepository = thirdpartyRepository;
	}
	public static String randomCheckCode(String str) {

		   // 声明返回值
		   String temp = "";

		   // 验证码
		   // 1-9，a-z A-Z ctrl+shift+X   

		   System.out.println("字符串的长度:" + str.length()); // 62

		   Random random = new Random();

		   for (int i = 0; i < 4; i++) {

		    // 随机获取 0-61 数字 4次 charAt(num);
		    int num = random.nextInt(str.length());

		    char c1 = str.charAt(num); // 索引从0开始 到61

		    temp += c1;

		   }

		   return temp;

		  }
	/**
	 * 发送短信
	 */
	@Override
	public String send(String mobile) {
		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = "https://smssh1.253.com/msg/send/json";
		 String str = "0123456789";//abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 String checkCode = randomCheckCode(str);
		// 短信内容
		String msg = "尊敬的用户,您好,您的验证码是"+checkCode+",5分钟内有效。【油多多】";
		// 手机号码
		String phone = mobile;
		// 状态报告
		String report = "true";
		String account ;
		String pswd  ;
		try {
			  account = thirdpartyRepository.findOne("smsUsername").getValue();
			  pswd = thirdpartyRepository.findOne("smsPassword").getValue();
		} catch (Exception e) {
			return "error";
		}
		 
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd,
				msg, phone, report);

		String requestJson = JSON.toJSONString(smsSingleRequest);

		//System.out.println("before request string is: " + requestJson);

		String response = ChuangLanSmsUtil.sendSmsByPost(
				smsSingleRequestServerUrl, requestJson);

		//System.out.println("response after request result is :" + response);

		SmsSendResponse smsSingleResponse = JSON.parseObject(response,
				SmsSendResponse.class);

		//System.out.println("response  toString is :" + smsSingleResponse);
		if("0".equals(smsSingleResponse.getCode())){
			// 保存短信验证码
			Sms sms = new Sms();
			Date today = new Date();
			Date expireDate = new Date(today.getTime() + (1000 * 60 * 5)); // 短信5分钟后过期
			sms.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			sms.setMobile(mobile);
			sms.setVcode(checkCode);
			sms.setExpiredDatetime(expireDate);
			//sms.createSms(mobile, String.valueOf(checkCode), expireDate);
			smsRepository.save(sms);	
			return smsSingleResponse.getCode();
		}else{
			return "error";
		}
	 
	}

	@Override
	public Sms findByUsernameAndVcode(String mobile, String vcode) {
		Sms sms = new Sms();
		sms = smsRepository
				.findFirstByMobileAndVcodeOrderByExpiredDatetimeDesc(mobile,
						vcode);
		LOGGER.info("Finding username and vcode" +mobile+vcode);
		if (sms != null
				&& (sms.getExpiredDatetime().getTime() > new Date().getTime())) {
			LOGGER.info("username/vcode matched and valid");
			return sms;
		}
		LOGGER.info("username/vcode not matched and invalid");
		return null;
	}

	/**
	 * 保存短信配置
	 */
	@Override
	public void saveSmsConfig(String username, String password) {
		ThirdParty thirdParty = new ThirdParty();
		thirdParty.createConfig("smsUsername", username);
		thirdpartyRepository.save(thirdParty);
		thirdParty.createConfig("smsPassword", password);
		thirdpartyRepository.save(thirdParty);

	}

	/**
	 * 检查短信账号状态
	 * 
	 * @return
	 */
	@Override
	public Boolean checkSmsAccountStatus(String account, String password) {

		Boolean ret = false;
		try {
			String status = SendSMS.checkAccountStatus(account, password);
			LOGGER.info("status: '{}'", status);
			switch (status.trim()) {
			case "1":
				ret = true;// 正常
				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return ret;

	}

}
