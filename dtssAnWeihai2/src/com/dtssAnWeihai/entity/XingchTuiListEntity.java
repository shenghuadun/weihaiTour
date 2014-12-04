package com.dtssAnWeihai.entity;

public class XingchTuiListEntity {
	private String startDate;
	private String dayDetail;
	private String day;

	public XingchTuiListEntity() {
		super();
	}

	public XingchTuiListEntity(String startDate, String dayDetail, String day) {
		super();
		this.startDate = startDate;
		this.dayDetail = dayDetail;
		this.day = day;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getDayDetail() {
		return dayDetail;
	}

	public void setDayDetail(String dayDetail) {
		this.dayDetail = dayDetail;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
