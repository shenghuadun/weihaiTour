package com.dtssAnWeihai.entity;

public class XingchMylistEntity {
	private String id;
	private String type;
	private String name;
	private String fromCity;
	private String toCity;
	private String startDate;
	private String endDate;
	private String remarks;
	private String graId;

	public XingchMylistEntity() {
		super();
	}

	public XingchMylistEntity(String id, String type, String name, String fromCity, String toCity, String startDate, String endDate, String remarks, String graId) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.fromCity = fromCity;
		this.toCity = toCity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.remarks = remarks;
		this.graId = graId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGraId() {
		return graId;
	}

	public void setGraId(String graId) {
		this.graId = graId;
	}

}
