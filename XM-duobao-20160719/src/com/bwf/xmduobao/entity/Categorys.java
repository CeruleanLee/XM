package com.bwf.xmduobao.entity;

import java.util.List;

public class Categorys {
	private int totalCnt;
	private List<Category> list;
	public Categorys(int totalCnt, List<Category> list) {
		super();
		this.totalCnt = totalCnt;
		this.list = list;
	}
	public Categorys() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public List<Category> getList() {
		return list;
	}
	public void setList(List<Category> list) {
		this.list = list;
	}
	
}
