package com.bwf.xmduobao.entity;

import java.util.List;

public class PeriodInfo {
	/**商品id**/
	public int goodsId;
	/**商品名字**/
	public String goodsName;
	public String img;
	/**图片地址集合**/
	public List<String> bigImgs;
	/**描述**/
	public String description;
	/**期号**/
	public long period;
	/**总需参与次数***/
	public int totalTimes;
	/**剩余次数**/
	public int remainTimes;
	/**开奖状态 0：进行中；1：揭晓倒计时；2：已揭晓**/
	public int status;
	/**揭晓倒计时剩余时间(毫秒)**/
	public int remainTime;
	/**揭晓时间**/
	public String  revealedTime;
	/**幸运用户信息**/
	public LuckUser luckUser;
	/**开奖号码**/
	public int luckCode;
	/**登入之后需要用到**/
	public List<Integer> duobaoNums;
	/**这一期的夺宝开始时间**/
	public long startDuobaoTime;
	public static class LuckUser{
		/**用户的id**/
		public int uid;
		/**用户的昵称**/
		public String nickname;
		/**用户的地址**/
		public String IPAddress;
		/**用户的ip**/
		public String IP;
		/**头像地址**/
		public String avatar;
		/**总参与次数**/
		public Integer duobaoTimes;
		@Override
		public String toString() {
			return "LuckUser [uid=" + uid + ", nickname=" + nickname + ", IPAddress=" + IPAddress + ", IP=" + IP
					+ ", avatar=" + avatar + ", duobaoTimes=" + duobaoTimes + "]";
		}
	}
	@Override
	public String toString() {
		return "PeriodInfo [goodsId=" + goodsId + ", goodsName=" + goodsName + ", img=" + img + ", bigImgs=" + bigImgs
				+ ", description=" + description + ", period=" + period + ", totalTimes=" + totalTimes
				+ ", remainTimes=" + remainTimes + ", status=" + status + ", remainTime=" + remainTime
				+ ", revealedTime=" + revealedTime + ", luckUser=" + luckUser + ", luckCode=" + luckCode
				+ ", duobaoNums=" + duobaoNums + ", startDuobaoTime=" + startDuobaoTime + "]";
	}
	
}
