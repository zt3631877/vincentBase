package com.sectong.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建用户字段POJO定义
 * 
 * @author vincent
 *
 */
public class UserCreateForm {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String password;

	@NotEmpty
	private String vcode;

	 

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	@Override
	public String toString() {
		return "UserCreateForm [username=" + userName + ", password=" + password + "]";
	}

}
