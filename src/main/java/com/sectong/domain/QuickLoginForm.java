package com.sectong.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建用户字段POJO定义
 * 
 * @author vincent
 *
 */
public class QuickLoginForm {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String vcode;

 

	

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserCreateForm [username=" + userName + ", vcode=" + vcode + "]";
	}

}
