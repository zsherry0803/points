package com.fetchRewards.points.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.fetchRewards.points.DAO.Payer;
import com.fetchRewards.points.DAO.PointEntry;
import com.fetchRewards.points.DAO.Response;

public class PointsService {
	Set<String> payers;
	boolean isUpdated;
	List<Response> pointBalance; 
	Map<String, Payer> payersMap;
	Set<String> updatedPayers;
	
	public PointsService() {
		payers = new HashSet<>();
		isUpdated = false;
		pointBalance = new ArrayList<>();
		payersMap = new HashMap<>();
		updatedPayers = new HashSet<>();
	}
	
	public void addBalance(PointEntry point) {
		String payerName = point.getPayer();
		updatedPayers.add(payerName);
		if(!payers.contains(payerName)) {
			Payer payer = new Payer(payerName);
			payersMap.put(payerName, payer);
			payers.add(payerName);
		}
		Payer payer = payersMap.get(payerName);
		payer.addPoint(point);
		isUpdated = true;
	}
	
	public List<Response> getBalance() throws Exception {
		if(!isUpdated) {
			return pointBalance;
		}
		for(String payerName: updatedPayers) {
			Payer payer = payersMap.get(payerName);
			payer.mergePoints();
		}
		
		List<Response> results = new ArrayList<>();
		for(String payerName: payers) {
			Payer payer = payersMap.get(payerName);
			results.add(new Response(payerName, payer.getBalance()));
		}
		
		isUpdated = false;
		updatedPayers = new HashSet<>();
		pointBalance = results;
		
		return results;
	}
	
	public List<Response> useBalance(int point) throws Exception {
		for(String payerName: updatedPayers) {
			Payer payer = payersMap.get(payerName);
			payer.mergePoints();
		}

		updatedPayers = new HashSet<>();
		isUpdated = true;
		
		TreeMap<Date, List<String>> map = new TreeMap<>();
		for(String payer: payers) {
			TreeMap<Date, Integer> payerMap = payersMap.get(payer).getPoint();
			if(!payerMap.isEmpty()) {
				Date oldestDate = payerMap.lastKey();
				if(oldestDate!=null) {
					if(!map.containsKey(oldestDate)) {
						map.put(oldestDate, new ArrayList<String>());
					} 
					List<String> temp = map.get(oldestDate);
					temp.add(payer);
					map.put(oldestDate, temp);
				}
			}
		}
		
		Map<String, Integer> pointsUsed = new HashMap<>();
		while(point>0 && !map.isEmpty()) {
			Entry<Date, List<String>> entry = map.firstEntry();
			Date date = entry.getKey();
			String payerToUse = entry.getValue().get(0);
			int pointToUse = Math.min(payersMap.get(payerToUse).getPoint().get(date), point);
			point -= pointToUse;
			payersMap.get(payerToUse).usePoint(date, pointToUse);
			entry.getValue().remove(0);
			if(entry.getValue().size()>0) {
				map.put(date, entry.getValue());
			} else {
				map.remove(date);
			}
			pointsUsed.put(payerToUse, pointsUsed.getOrDefault(payerToUse, 0) + pointToUse);
			TreeMap<Date, Integer> payerMap = payersMap.get(payerToUse).getPoint();
			Date oldestDate = payerMap.lowerKey(date);
				if(oldestDate!=null) {
					if(!map.containsKey(oldestDate)) {
					map.put(oldestDate, new ArrayList<String>());
				} 
				List<String> temp = map.get(oldestDate);
				temp.add(payerToUse);
				map.put(oldestDate, temp);
			}
		}
		
		List<Response> result = new ArrayList<>();
		for(String key: pointsUsed.keySet()) {
			result.add(new Response(key, -pointsUsed.get(key)));
		}
		
		return result;
	}
}
