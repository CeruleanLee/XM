package com.bwf.xmduobao.entity;

public class UserDuobaoTime {

	private String dateTime;
	private String timeForCompute;
	private String userNickName;
	private int uid;
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getTimeForCompute() {
		return timeForCompute;
	}
	public void setTimeForCompute(String timeForCompute) {
		this.timeForCompute = timeForCompute;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public UserDuobaoTime(String dateTime, String timeForCompute, String userNickName, int uid) {
		super();
		this.dateTime = dateTime;
		this.timeForCompute = timeForCompute;
		this.userNickName = userNickName;
		this.uid = uid;
	}
	public UserDuobaoTime() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
