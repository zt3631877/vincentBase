package com.sectong.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		System.out.println("authorityauthorityauthorityauthorityauthority:"+authority);
		this.authority = authority;
	}

	public String getUsername() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Authority [id=" + id + ", username=" + username
				+ ", authoritiy=" + authority + "]";
	}

}
