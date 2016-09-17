package com.bwf.xmduobao.entity;

public class ResponseGoodsDetail {
	private int code;
	private GoodsDetail result;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public GoodsDetail getResult() {
		return result;
	}
	public void setResult(GoodsDetail result) {
		this.result = result;
	}
	public ResponseGoodsDetail(int code, GoodsDetail result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponseGoodsDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
