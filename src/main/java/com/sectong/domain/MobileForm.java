package com.sectong.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class MobileForm {

	@NotEmpty
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
