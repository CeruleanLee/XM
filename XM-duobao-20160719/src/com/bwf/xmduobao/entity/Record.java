package com.bwf.xmduobao.entity;

public class Record {

	private int times;
	private String time;
	private User user;
	public static class User{
		private String nickName;
		private int uid;
		private String IPAddress;
		private String IP;
		private String avatar;
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public int getUid() {
			return uid;
		}
		public void setUid(int uid) {
			this.uid = uid;
		}
		public String getIPAddress() {
			return IPAddress;
		}
		public void setIPAddress(String iPAddress) {
			IPAddress = iPAddress;
		}
		public String getIP() {
			return IP;
		}
		public void setIP(String iP) {
			IP = iP;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
