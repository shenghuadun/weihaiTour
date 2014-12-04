package com.dtssAnWeihai.entity;

public class GonglueEntity {
	private String id;
	private String name;
	private String image;
	private String type;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GonglueEntity() {
	}

	public GonglueEntity(String id, String name, String image, String type) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.type = type;
	}

}
