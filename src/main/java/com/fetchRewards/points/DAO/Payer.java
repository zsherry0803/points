package com.fetchRewards.points.DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Payer {
	String payer;
	int balance;
	TreeMap<Date, Integer> point;
	
	public Payer(String payer) {
		this.payer = payer;
		this.balance = 0;
		this.point = new TreeMap<>((i, j)-> (j.compareTo((i))));
	}
	
	public TreeMap<Date, Integer> getPoint() {
		return this.point;
	}
	
	public void addPoint(PointEntry entry) {
		int entryPoint = entry.points;
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date entryDate = new Date();
		try {
			entryDate = dateParser.parse(entry.timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.balance += entryPoint;
		
		if(point.containsKey(entryDate)) {
			point.put(entryDate, point.get(entryDate) + entryPoint);
		} else {
			point.put(entryDate, entryPoint);
		}
	}
	
	public int getBalance() {
		return this.balance;
	}
	
	public void mergePoints() throws Exception {
		int current = 0;
		Set<Date> toRemove = new HashSet<>();
		for(Date date: point.keySet()) {
			current += point.get(date);
			if(current<=0) {
				toRemove.add(date);
			} else if(current!=point.get(date)) {
				point.put(date, current);
			}
			
			current = Math.min(current, 0);
		}
		
		for(Date d: toRemove) {
			point.remove(d);
		}
		
		if(current<0) {
			throw new Exception("invalid balance");
		}
	}
	
	public void usePoint(Date date, int value) {
		int newValue = this.point.get(date)-value;
		if(newValue <= 0) {
			this.point.remove(date);
		} else {
			this.point.put(date, this.point.get(date)-value);
		}
		
		this.balance -= value;
	}
	
	public Date getOldestPoints() {
		return this.point.lastKey();
	}
	
	public int getPointInDate(Date date) {
		if(this.point.containsKey(date)) {
			return this.point.get(date);
		}
		return 0;
	}
}
