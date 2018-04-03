package com.hfjh.dams.shiro.bo;

import java.io.Serializable;


public class UserModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userName;
	private String realName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
