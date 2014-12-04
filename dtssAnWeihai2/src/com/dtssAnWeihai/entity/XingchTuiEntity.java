package com.dtssAnWeihai.entity;

public class XingchTuiEntity {
	private String id;
	private String title;
	private String startDate;
	private String endDate;

	public XingchTuiEntity() {
		super();
	}

	public XingchTuiEntity(String id, String title, String startDate, String endDate) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
