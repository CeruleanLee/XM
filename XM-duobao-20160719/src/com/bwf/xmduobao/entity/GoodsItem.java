package com.bwf.xmduobao.entity;

/**
*@author Li gang
*@date  2016年6月6日上午2:36:29
*@declaration
*/
public class GoodsItem {
	private int timesInCar;
	public int getTimesInCar() {
		return timesInCar;
	}
	/**商品名称**/
	private String title;
	/**商品图片地址**/
	private String img;
	/**商品id**/
	private int goodsId;
	/**期号**/
	private long period;
	/**商品总价格**/
	private int price;
	/**该期剩余次数**/
	private int remainTimes;
	/**总需次数**/
	private int totalTimes;
	/**商品类型，0:普通 1:十元商品 2：百元商品**/
	private int category;
	/**一键添加到清单中默认的次数(有的是1，有的是10，由服务器决定)**/
	private int defaultJoinTimes;
	/**该期当前状态，0:正在进行，1:正在揭晓，2:已揭晓**/
	private Integer status;
	/**揭晓时间**/
	private String revealedTime;
	/**幸运号码**/
	private Integer luckCode;
	/**1人次的价格**/
	private int eachTimeMoney;
	/**幸运用户对象**/
	private LuckUser luckUser;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getDefaultJoinTimes() {
		return defaultJoinTimes;
	}

	public void setDefaultJoinTimes(int defaultJoinTimes) {
		this.defaultJoinTimes = defaultJoinTimes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRevealedTime() {
		return revealedTime;
	}

	public void setRevealedTime(String revealedTime) {
		this.revealedTime = revealedTime;
	}

	public Integer getLuckCode() {
		return luckCode;
	}

	public void setLuckCode(Integer luckCode) {
		this.luckCode = luckCode;
	}

	public int getEachTimeMoney() {
		return eachTimeMoney;
	}

	public void setEachTimeMoney(int eachTimeMoney) {
		this.eachTimeMoney = eachTimeMoney;
	}

	public LuckUser getLuckUser() {
		return luckUser;
	}

	public void setLuckUser(LuckUser luckUser) {
		this.luckUser = luckUser;
	}

	public void setTimesInCar(int timesInCar) {
		this.timesInCar = timesInCar;
	}

	public static class LuckUser{
		/**用户的id**/
		public int uid;
		/**用户昵称**/
		public String nickname;
		/**参与次数**/
		public int duobaoTimes;
	}
}
