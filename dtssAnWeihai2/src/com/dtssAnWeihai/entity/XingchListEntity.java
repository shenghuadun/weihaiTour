package com.dtssAnWeihai.entity;

public class XingchListEntity {
	private String id;
	private String name;
	private String fromCity;
	private String toCity;
	private String startDate;
	private String endDate;

	public XingchListEntity() {
		super();
	}

	public XingchListEntity(String id, String name, String fromCity, String toCity, String startDate, String endDate) {
		super();
		this.id = id;
		this.name = name;
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
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
