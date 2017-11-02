package com.sectong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sectong.domain.LoginForm;
import com.sectong.domain.MobileForm;
import com.sectong.message.Message;
import com.sectong.service.SendSMSService;
import com.sectong.validator.ValidatorUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/api/v1", name = "第三方API")
@Api(description = "第三方API")
public class ThirdPartyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyController.class);
	private Message message = new Message();

	private SendSMSService sendSMSService;

	@Autowired
	public ThirdPartyController(SendSMSService sendSMSService) {
		this.sendSMSService = sendSMSService;
	}

	/**
	 * 请求短信验证码
	 * 
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "requestSMS", method = RequestMethod.POST)
	@ApiOperation(value = "请求短信验证码接口", notes = "请求短信验证码接口，POST请求，参数mobile必须为11位手机号码")
	public ResponseEntity<Message> requestSMS(@Valid @RequestBody MobileForm mobilef,
			HttpServletRequest request) {
		 
		if (mobilef==null||mobilef.getMobile().isEmpty()) {
			LOGGER.info("手机号码输入为空");
			message.setMsg(0, "手机号码不能为空");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		String mobile=mobilef.getMobile();
		if (!ValidatorUtil.isMobile(mobile)) {
			LOGGER.info("手机号码错误：{}", mobile);
			message.setMsg(0, "手机号码错误");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}

		String ret = sendSMSService.send(mobile);
		if (ret.equals("0")) {
			LOGGER.info("验证码发送成功：{}", mobile);
			message.setMsg(1, "验证码发送成功");
		} else {
			LOGGER.info("验证码发送失败：{}", mobile);
			message.setMsg(0, "验证码发送失败：" + ret);
		}

		LOGGER.info("Access ThirdPartyController.requestSMS");
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

}
