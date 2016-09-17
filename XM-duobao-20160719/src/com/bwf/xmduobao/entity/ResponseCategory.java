package com.bwf.xmduobao.entity;

public class ResponseCategory {
	private int code;
	private Categorys result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Categorys getResult() {
		return result;
	}
	public void setResult(Categorys result) {
		this.result = result;
	}
	public ResponseCategory(int code, Categorys result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponseCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
