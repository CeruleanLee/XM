package com.bwf.xmduobao.entity;

import java.util.List;

public class ResponseWinHistory {
	public int code;
	public WinHistoryItems result;
	public static class WinHistoryItems{
		public int pageNum;
		public int pageSize;
		public int totalCnt;
		public String timestamp;
		public List<WinHistoryItem> list;
	}

	public static class WinHistoryItem{
		public String period;
		public int status;
		public Long remainTime;
		public String  startTime;
		public String  cutoffTime;
		public String  revealedTime;
		public LuckUser luckUser;
		public Integer luckCode;
	}
	public static class LuckUser{
		public int uid;
		public String nickname;
		public String IPAddress;
		public String IP;
		public String avatar;
		public Integer duobaoTimes;

	}

}