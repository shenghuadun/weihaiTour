package com.dtssAnWeihai.entity;

import java.io.Serializable;

public class LoginUser implements Serializable {
	private static final long serialVersionUID = 7944174430798841905L;
	private String id; // 用户id
	private String userId; // 用户名
	private String address; // 地址
	private String email; // 邮箱
	private String sex; // 性别
	private String phone; // 电话号码
	private String userName; // 真实姓名

	public LoginUser() {
		super();
	}

	public LoginUser(String id, String userId, String address, String email, String sex, String phone, String userName) {
		super();
		this.id = id;
		this.userId = userId;
		this.address = address;
		this.email = email;
		this.sex = sex;
		this.phone = phone;
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
