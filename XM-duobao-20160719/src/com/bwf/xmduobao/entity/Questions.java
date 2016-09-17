package com.bwf.xmduobao.entity;

import java.util.List;

public class Questions {
	public int totalCnt;
	public List<Question> list;
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public List<Question> getList() {
		return list;
	}
	public void setList(List<Question> list) {
		this.list = list;
	}
	public Questions(int totalCnt, List<Question> list) {
		super();
		this.totalCnt = totalCnt;
		this.list = list;
	}
	public Questions() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
