package com.sectong.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 第三方配置信息
 * 
 * @author vincent
 *
 */
@Entity
@Table(name = "third_party")
public class ThirdParty {

	@Id
	private String config;

	private String value;

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void createConfig(String config, String value) {
		this.config = config;
		this.value = value;
	}

}
