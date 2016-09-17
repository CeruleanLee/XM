package com.bwf.xmduobao.entity;

public class ResponsePeriodInfo {
	public int code;
	public PeriodInfo result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public PeriodInfo getResult() {
		return result;
	}
	public void setResult(PeriodInfo result) {
		this.result = result;
	}
	public ResponsePeriodInfo(int code, PeriodInfo result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponsePeriodInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ResponsePeriodInfo [code=" + code + ", result=" + result + "]";
	}
	
}
