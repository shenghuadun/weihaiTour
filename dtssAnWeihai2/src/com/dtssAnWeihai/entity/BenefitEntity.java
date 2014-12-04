package com.dtssAnWeihai.entity;

import java.io.Serializable;

/**
 * 举优惠-实体类
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-17
 */
public class BenefitEntity implements Serializable {

	private static final long serialVersionUID = -1117372937637143731L;
	private String unitId;
	private String benName;
	private String benCond;
	private String image;
	private String address;
	private String benDesc;// 使用说明
	private String latitude;
	private String longitude;
	private String distance;
	private String benStartDate;
	private String benEndDate;
	private String jifenPrice;
	private String coupon;// 优惠具体内容

	public BenefitEntity() {
		super();
	}

	public BenefitEntity(String unitId, String benName, String benCond, String image, String address, String benDesc, String latitude, String longitude, String distance, String benStartDate, String benEndDate, String jifenPrice, String coupon) {
		super();
		this.unitId = unitId;
		this.benName = benName;
		this.benCond = benCond;
		this.image = image;
		this.address = address;
		this.benDesc = benDesc;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.benStartDate = benStartDate;
		this.benEndDate = benEndDate;
		this.jifenPrice = jifenPrice;
		this.coupon = coupon;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getBenName() {
		return benName;
	}

	public void setBenName(String benName) {
		this.benName = benName;
	}

	public String getBenCond() {
		return benCond;
	}

	public void setBenCond(String benCond) {
		this.benCond = benCond;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBenDesc() {
		return benDesc;
	}

	public void setBenDesc(String benDesc) {
		this.benDesc = benDesc;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getBenStartDate() {
		return benStartDate;
	}

	public void setBenStartDate(String benStartDate) {
		this.benStartDate = benStartDate;
	}

	public String getBenEndDate() {
		return benEndDate;
	}

	public void setBenEndDate(String benEndDate) {
		this.benEndDate = benEndDate;
	}

	public String getJifenPrice() {
		return jifenPrice;
	}

	public void setJifenPrice(String jifenPrice) {
		this.jifenPrice = jifenPrice;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

}
