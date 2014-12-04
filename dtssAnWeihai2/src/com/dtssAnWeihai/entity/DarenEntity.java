package com.dtssAnWeihai.entity;

public class DarenEntity {
	private String id;
	private String title;
	private String guiname;
	private String image;
	private String content;

	public DarenEntity() {
		super();
	}

	public DarenEntity(String id, String title, String guiname, String image, String content) {
		super();
		this.id = id;
		this.title = title;
		this.guiname = guiname;
		this.image = image;
		this.content = content;
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

	public String getGuiname() {
		return guiname;
	}

	public void setGuiname(String guiname) {
		this.guiname = guiname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
