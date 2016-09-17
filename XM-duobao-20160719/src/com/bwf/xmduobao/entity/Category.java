package com.bwf.xmduobao.entity;

public class Category {

	private int categoryId;
	private String categoryName;
	private String imgUrl;
	private int goodsTotal;
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getGoodsTotal() {
		return goodsTotal;
	}
	public void setGoodsTotal(int goodsTotal) {
		this.goodsTotal = goodsTotal;
	}
	public Category(int categoryId, String categoryName, String imgUrl, int goodsTotal) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.imgUrl = imgUrl;
		this.goodsTotal = goodsTotal;
	}
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
