package com.fetchRewards.points.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fetchRewards.points.DAO.PointEntry;
import com.fetchRewards.points.DAO.Response;
import com.fetchRewards.points.service.PointsService;

@RestController
public class PointsController {
	PointsService ps = new PointsService();
	
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    public void addBalance(@RequestBody PointEntry point) {
        ps.addBalance(point);
    }
    
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public List<Response> getBalance() {
    	List<Response> result = new ArrayList<>();
    	try {
    		result = ps.getBalance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    @RequestMapping(value = "/useBalance", method = RequestMethod.POST)
    public List<Response> useBalance(int point) {
    	List<Response> result = new ArrayList<>();
        try {
        	result = ps.useBalance(point);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
}
