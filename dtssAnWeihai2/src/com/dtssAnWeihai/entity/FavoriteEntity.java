package com.dtssAnWeihai.entity;

public class FavoriteEntity {
	private String id;
	private String proId;
	private String proName;
	private String proType;
	private String proDesc;
	private String proImage;

	public FavoriteEntity() {
		super();
	}

	public FavoriteEntity(String id, String proId, String proName, String proType, String proDesc, String proImage) {
		super();
		this.id = id;
		this.proId = proId;
		this.proName = proName;
		this.proType = proType;
		this.proDesc = proDesc;
		this.proImage = proImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getProImage() {
		return proImage;
	}

	public void setProImage(String proImage) {
		this.proImage = proImage;
	}

}
