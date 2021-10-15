package com.fetchRewards.points.DAO;

public class Response {
	String payer;
	int point;
	
	public Response(String payer, int point) {
		this.payer = payer;
		this.point = point;
	}
	
	public String getPayer() {
		return this.payer;
	}
	
	public int getPoint() {
		return this.point;
	}
}
