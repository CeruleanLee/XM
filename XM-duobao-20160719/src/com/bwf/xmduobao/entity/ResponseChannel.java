package com.bwf.xmduobao.entity;
/**
*@author Li gang
*@date  2016年6月6日上午2:43:54
*@declaration
*/
public class ResponseChannel {
	public int code;
	public Channels result;
	public ResponseChannel(int code, Channels result) {
		super();
		this.code = code;
		this.result = result;
	}
	public ResponseChannel() {
		super();
	}
}
