package com.dtssAnWeihai.entity;

import java.io.Serializable;

public class LocationEntity implements Serializable {
	private static final long serialVersionUID = 3105891319868806338L;
	private String province;
	private String city;
	private String district;
	private String street;
	private String streetNo;
	private double latitude;
	private double lontitude;
	private String address;

	public LocationEntity() {
		super();
	}

	public LocationEntity(String province, String city, String district, String street, String streetNo, double latitude, double lontitude, String address) {
		super();
		this.province = province;
		this.city = city;
		this.district = district;
		this.street = street;
		this.streetNo = streetNo;
		this.latitude = latitude;
		this.lontitude = lontitude;
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLontitude() {
		return lontitude;
	}

	public void setLontitude(double lontitude) {
		this.lontitude = lontitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
