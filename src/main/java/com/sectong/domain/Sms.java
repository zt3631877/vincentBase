package com.sectong.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 短信pojo
 * 
 * @author vincent
 *
 */
@Entity
@Table(name = "sms")
public class Sms {

	@Id
	private String id;

	private String mobile;
	private String vcode;
	private Date expiredDatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public Date getExpiredDatetime() {
		return expiredDatetime;
	}

	public void setExpiredDatetime(Date expiredDatetime) {
		this.expiredDatetime = expiredDatetime;
	}

	public void createSms(String id,String mobile, String vcode, Date expiredDatetime) {
		this.id=id;
		this.mobile = mobile;
		this.vcode = vcode;
		this.expiredDatetime = expiredDatetime;
	}

}
