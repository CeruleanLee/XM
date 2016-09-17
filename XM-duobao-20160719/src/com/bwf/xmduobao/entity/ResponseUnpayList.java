package com.bwf.xmduobao.entity;

import java.util.List;

public class ResponseUnpayList {

	public int code;
	public UnpayListObj result;
	public static class UnpayListObj{
		public int totalCnt;
		public int moneyAll;
		public List<UnpayGoods> list;
	}
	public class UnpayGoods{
		public String title;
		public String img;
		public int goodsId;
		public long period;
		public int remainTimes;
		public int totalTimes;
		public int category;
		public int joinTimes;
		public int totalMoney;
		public boolean updated;
	}
}
