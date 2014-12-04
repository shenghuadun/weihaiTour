package com.dtssAnWeihai.entity;

public class LineEntity {
	private String id;
	private String name;
	private String phone;
	private String image;

	public LineEntity() {
	}

	public LineEntity(String id, String name, String phone, String image) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.image = image;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
