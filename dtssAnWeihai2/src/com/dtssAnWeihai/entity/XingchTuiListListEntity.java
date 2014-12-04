package com.dtssAnWeihai.entity;

public class XingchTuiListListEntity {
	private String nodeId;
	private String desc;
	private String address;
	private String nodeType;

	public XingchTuiListListEntity() {
		super();
	}

	public XingchTuiListListEntity(String nodeId, String desc, String address, String nodeType) {
		super();
		this.nodeId = nodeId;
		this.desc = desc;
		this.address = address;
		this.nodeType = nodeType;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

}
