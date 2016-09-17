package com.bwf.xmduobao.entity;
/**
*@author Li gang
*@date  2016年6月6日上午2:43:54
*@declaration
*/
public class ResponseGoodsItem {
	private int code;
	private GoodsItems result;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public GoodsItems getResult() {
		return result;
	}
	public void setResult(GoodsItems result) {
		this.result = result;
	}
	public ResponseGoodsItem(int code, GoodsItems result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponseGoodsItem() {
		super();
	}
}
