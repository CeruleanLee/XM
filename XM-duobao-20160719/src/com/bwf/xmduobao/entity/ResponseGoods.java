package com.bwf.xmduobao.entity;

public class ResponseGoods {
	private int code;
	private Goods result;
	public static  class Goods{
		private int gid;
		private long period;
		public int getGid() {
			return gid;
		}
		public void setGid(int gid) {
			this.gid = gid;
		}
		public long getPeriod() {
			return period;
		}
		public void setPeriod(long period) {
			this.period = period;
		}
		
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Goods getResult() {
		return result;
	}
	public void setResult(Goods result) {
		this.result = result;
	}
	
}
