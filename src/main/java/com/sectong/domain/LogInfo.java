package com.sectong.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  pojo
 * 
 * @author vincent
 *
 */
@Entity
@Table(name = "log_info")
public class LogInfo {

	@Id
	private String id;

	private String createTime; 
	 
	private String msg; 
	
 
 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	 
 
 
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	 
}
