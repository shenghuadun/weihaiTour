package com.dtssAnWeihai.tools;

import com.dtssAnWeihai.entity.LocationEntity;

/**
 * 存放单例模式值
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-24
 */
public class MyStatusEntity {

	private volatile static MyStatusEntity instance;
	private LocationEntity locationEntity = new LocationEntity();
	private String searchtypeString = "";

	// 单例模式中获取唯一的MyLoginUser实例
	public static MyStatusEntity getInstance() {
		if (null == instance) {
			instance = new MyStatusEntity();
		}
		return instance;
	}

//	public void setLocationEntity(LocationEntity locationEntity) {
//		this.locationEntity = locationEntity;
//	}
//
//	public LocationEntity getLocationEntity() {
//		return locationEntity;
//	}

	public void setSearchtypeString(String searchtypeString) {
		this.searchtypeString = searchtypeString;
	}

	public String getSearchtypeString() {
		return searchtypeString;
	}

	private MyStatusEntity() {
	}
}
