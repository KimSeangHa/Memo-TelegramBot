package com.spring.webservice.vo;

public class memoVO {
	private String content;
	private String id;
	private int num;
	private String regDate;
	private String modifyDate;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		System.out.println(regDate);
		this.regDate = regDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
}
