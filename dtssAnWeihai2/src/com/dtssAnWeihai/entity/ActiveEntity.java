package com.dtssAnWeihai.entity;

public class ActiveEntity {
	private String id;
	private String actEndDate;
	private String actTime;
	private String image;
	private String actStartDate;
	private String actName;
	private String actType;

	public ActiveEntity() {
		super();
	}

	public ActiveEntity(String id, String actEndDate, String actTime, String image, String actStartDate, String actName, String actType) {
		super();
		this.id = id;
		this.actEndDate = actEndDate;
		this.actTime = actTime;
		this.image = image;
		this.actStartDate = actStartDate;
		this.actName = actName;
		this.actType = actType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActEndDate() {
		return actEndDate;
	}

	public void setActEndDate(String actEndDate) {
		this.actEndDate = actEndDate;
	}

	public String getActTime() {
		return actTime;
	}

	public void setActTime(String actTime) {
		this.actTime = actTime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getActStartDate() {
		return actStartDate;
	}

	public void setActStartDate(String actStartDate) {
		this.actStartDate = actStartDate;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

}
