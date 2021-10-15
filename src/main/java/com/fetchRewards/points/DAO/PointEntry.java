package com.fetchRewards.points.DAO;

public class PointEntry {
	String payer;
	int points;
	String timeStamp;
	
	PointEntry(String payer, int points, String timestamp) {
		this.payer = payer;
		this.points = points;
		this.timeStamp = timestamp;
	}
	
	public String getPayer() {
		return this.payer;
	}
	
	public void setPayer(String payer) {
		this.payer = payer;
	}
	
	public int getPoint() {
		return this.points;
	}
	
	public void setPayer(int points) {
		this.points = points;
	}
	
	public String getTimeStamp() {
		return this.timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
