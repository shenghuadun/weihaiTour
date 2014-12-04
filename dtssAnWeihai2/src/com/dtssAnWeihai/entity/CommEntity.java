package com.dtssAnWeihai.entity;

public class CommEntity {
	private String id;
	private String feel;
	private String createTime;
	private String prodName;
	private String prodId;

	public CommEntity() {
		super();
	}

	public CommEntity(String id, String feel, String createTime, String prodName, String prodId) {
		super();
		this.id = id;
		this.feel = feel;
		this.createTime = createTime;
		this.prodName = prodName;
		this.prodId = prodId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

}
