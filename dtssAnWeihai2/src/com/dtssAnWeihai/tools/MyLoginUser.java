package com.dtssAnWeihai.tools;

import com.dtssAnWeihai.entity.LoginUser;

/**
 * 存放登录用户的值
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-4-24
 */
public class MyLoginUser {
	private LoginUser user = new LoginUser();
	private volatile static MyLoginUser instance;

	public static MyLoginUser getInstance() {
		if (null == instance) {
			instance = new MyLoginUser();
		}
		return instance;
	}

	public void setUser(LoginUser userInfo) {
		this.user = userInfo;
	}

	public LoginUser getUser() {
		return user;
	}
}
