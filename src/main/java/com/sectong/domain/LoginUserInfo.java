package com.sectong.domain;


/**
 * 加油字段POJO定义
 * 
 * @author vincent
 *
 */
public class LoginUserInfo {

 
	private String userName;
	private Long userId;
	private Long companyId;
	private Long pointId;
	private String companyName;
	private String authority;// 0不是商户1是商户
	private String ciphertext;//密文
	private String cipherqrcode;//二维码内容
 

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	public String getCipherqrcode() {
		return cipherqrcode;
	}

	public void setCipherqrcode(String cipherqrcode) {
		this.cipherqrcode = cipherqrcode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	 

	

	 
}
