package com.sectong.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 修改、重置密码POJO表单
 * 
 * @author vincent
 *
 */
public class UpdatePasswordForm {

 

	// 重置密码
	@NotEmpty
	@Size(min = 6, max = 32)
	private String password;

	// 旧密码
	@NotEmpty
	@Size(min = 6, max = 32)
	private String oldPassword;

	 
	public String getPassword() {
		return password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	 
}
