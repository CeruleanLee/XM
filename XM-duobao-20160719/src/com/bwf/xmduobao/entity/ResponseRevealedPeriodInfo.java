package com.bwf.xmduobao.entity;

public class ResponseRevealedPeriodInfo {
	public int code;
	public RevealedPeriodInfos result;
	public static class RevealedPeriodInfos{
		public int pageNum;
		public int pageSize;
		public int totalCnt;
		public java.util.List<RevealedPeriodInfo> list;
	}
	public static class RevealedPeriodInfo{
		public int goodsId;//商品id
		public String goodsName;//商品名字
		public String img;//商品图片
		public long period;//期号
		public int status;//当期状态
		public String remainTime;//剩余时间
		public String  revealedTime;//揭晓时间
		public LuckUser luckUser; //幸运用户对象
		public Integer luckCode;//幸运号码
		public long enterTime;
	}
	public static class LuckUser{
		public int uid;//幸运用户id
		public String nickname;//幸运用户昵称
		public Integer duobaoTimes;//幸运用户参与次数
	}
}
