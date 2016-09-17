package com.bwf.xmduobao.entity;

import java.util.List;

public class UserDuobaoTimes {

	private int totalCnt;
	private String timeCountForCompute;
	private int luckCode;
	private int totalTimes;
	private List<UserDuobaoTime> timesList;
	
	public List<UserDuobaoTime> getTimesList() {
		return timesList;
	}
	public void setTimesList(List<UserDuobaoTime> timesList) {
		this.timesList = timesList;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getTimeCountForCompute() {
		return timeCountForCompute;
	}
	public void setTimeCountForCompute(String timeCountForCompute) {
		this.timeCountForCompute = timeCountForCompute;
	}
	public int getLuckCode() {
		return luckCode;
	}
	public void setLuckCode(int luckCode) {
		this.luckCode = luckCode;
	}
	public int getTotalTimes() {
		return totalTimes;
	}
	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}
	
}
