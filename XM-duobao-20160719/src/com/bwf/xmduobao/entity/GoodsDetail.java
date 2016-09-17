package com.bwf.xmduobao.entity;

import java.util.List;

/**
 * 图文详情
 * @author Administrator
 *
 */
public class GoodsDetail {
	private List<String> list;
	private String declaration;
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public GoodsDetail(List<String> list, String declaration) {
		super();
		this.list = list;
		this.declaration = declaration;
	}
	public GoodsDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
