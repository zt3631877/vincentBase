package com.sectong.domain;

import java.util.Date;


/**
 * 创建用户字段POJO定义
 * 
 * @author vincent
 *
 */
public class UserInfoAll {


	private Long userId; 
	private String userName;//账户名
	private String userRealName;//实名
	private Long companyId;
	private String companyName;
	private Long pointId;
	private String pointName;
	private String authority;// 0不是商户1是商户
	private Long integral;//用户积分
	private Double balance;//余额
	private Double consumptionAmount;//消费金额
	private Long consumptionCount;//消费次数
	private String createTime;//创建时间
	private String isManager;

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



	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public Long getIntegral() {
		return integral;
	}

	public void setIntegral(Long integral) {
		this.integral = integral;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(Double consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public Long getConsumptionCount() {
		return consumptionCount;
	}

	public void setConsumptionCount(Long consumptionCount) {
		this.consumptionCount = consumptionCount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getPointId() {
		return pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	 

	

	 
}
