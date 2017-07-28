package com.jeeplus.modules.report.entity;

import com.jeeplus.common.persistence.DataEntity;

public class Index extends DataEntity<Index> {
	
	private static final long serialVersionUID = 1L;
	
	private String payTime;
	
	private double orderCount;
	
	private double orderCountFee;

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public double getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public double getOrderCountFee() {
		return orderCountFee;
	}

	public void setOrderCountFee(double orderCountFee) {
		this.orderCountFee = orderCountFee;
	}

	
}
