package com.bwf.xmduobao.entity;

public class ResopnseRecord {
	private int code;
	private Records result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Records getResult() {
		return result;
	}
	public void setResult(Records result) {
		this.result = result;
	}
	public ResopnseRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResopnseRecord(int code, Records result) {
		super();
		this.code = code;
		this.result = result;
	}
	
}
