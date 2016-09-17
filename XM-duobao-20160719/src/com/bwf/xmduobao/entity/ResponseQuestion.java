package com.bwf.xmduobao.entity;
/**
*@author Li gang
*@date  2016年6月6日上午2:43:54
*@declaration
*/
public class ResponseQuestion {
	public int code;
	public Questions result;
	public ResponseQuestion(int code, Questions result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponseQuestion() {
		super();
	}
}
