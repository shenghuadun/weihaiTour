package com.dtssAnWeihai.entity;

public class SearchListEntity {
	private String id;
	private String name;
	private String address;
	private String image;
	private String distance;

	public SearchListEntity() {
		super();
	}

	public SearchListEntity(String id, String name, String address, String image, String distance) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.image = image;
		this.distance = distance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
