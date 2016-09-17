package com.bwf.xmduobao.entity;

import java.util.List;

public class Records {
	public int totalCnt;
	public List<Record> list;
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public List<Record> getList() {
		return list;
	}
	public void setList(List<Record> list) {
		this.list = list;
	}
	public Records(int totalCnt, List<Record> list) {
		super();
		this.totalCnt = totalCnt;
		this.list = list;
	}
	public Records() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
