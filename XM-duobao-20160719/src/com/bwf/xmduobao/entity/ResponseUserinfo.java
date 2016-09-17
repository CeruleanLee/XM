package com.bwf.xmduobao.entity;

public class ResponseUserinfo {

	public int code;
	public Userinfo result;
	public static class Userinfo {
		public String uid;
		public String openid;
		public String token;
		public String nickname;
		public String province;
		public String city;
		public String area;
		public String sex;
		public String avatar;
		public int userType;
		public long loginDateTime;
		public String IP;
		public String IPAddress;
		@Override
		public String toString() {
			return "Userinfo [uid=" + uid + ", openid=" + openid + ", token=" + token + ", nickname=" + nickname
					+ ", province=" + province + ", city=" + city + ", area=" + area + ", sex=" + sex + ", avatar="
					+ avatar + ", userType=" + userType + ", loginDateTime=" + loginDateTime + "]";
		}
		
		
		
	}
	@Override
	public String toString() {
		return "ResponseUserinfo [code=" + code + ", result=" + result + "]";
	}

}
