/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单Entity
 * @author LD
 * @version 2017-05-19
 */
public class WashOrdOrder extends DataEntity<WashOrdOrder> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private WashMember carPerson;		// 车主
	private WashMember washPerson;		// 洗车工
	private WashDevice washDevice;		// 设备
	private String deviceMac;		// 设备MAC
	private String mobile;		// 手机号
	private String carNumber;     //车牌
	private String setMealId;		// 套餐
	private Double totalFee;		// 总费用
	private Double discountFee;		// 优惠金额
	private Double realFee;		// 实际费用
	private Date orderTime;		// 下单时间
	private Date payTime;		// 付款时间
	private Date endTime;		// 结束时间
	private String orderStatus;		// 订单状态（0-未付款 1-已付款 2-处理中 3-待评价 9-已完结）
	private String payMode;		// 支付方式(0-微信 1-支付宝 2-银行卡 3-现金)
	private String paySerialNumber;		// 支付流水号
	private Double washFee;		// wash_fee
	private Date beginPayTime;		// 开始 付款时间
	private Date endPayTime;		// 结束 付款时间
	
	public WashOrdOrder() {
		super();
	}

	public WashOrdOrder(String id){
		super(id);
	}

	@ExcelField(title="订单号", align=2, sort=1)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="车主", align=2, sort=2,fieldType=WashMember.class,value="carPerson.mobile")
	public WashMember getCarPerson() {
		return carPerson;
	}

	public void setCarPerson(WashMember carPerson) {
		this.carPerson = carPerson;
	}
	
	@ExcelField(title="洗车工", align=2, sort=3,fieldType=WashMember.class,value="washPerson.mobile")
	public WashMember getWashPerson() {
		return washPerson;
	}

	public void setWashPerson(WashMember washPerson) {
		this.washPerson = washPerson;
	}
	
	@ExcelField(title="设备", align=2, sort=4,fieldType=WashDevice.class,value="washDevice.name")
	public WashDevice getWashDevice() {
		return washDevice;
	}

	public void setWashDevice(WashDevice washDevice) {
		this.washDevice = washDevice;
	}
	
	@ExcelField(title="设备MAC", align=2, sort=5)
	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	
	@ExcelField(title="套餐", align=2, sort=6)
	public String getSetMealId() {
		return setMealId;
	}

	public void setSetMealId(String setMealId) {
		this.setMealId = setMealId;
	}
	
	@ExcelField(title="总费用", align=2, sort=7)
	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	
	@ExcelField(title="优惠金额", align=2, sort=8)
	public Double getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(Double discountFee) {
		this.discountFee = discountFee;
	}
	
	@ExcelField(title="实际费用", align=2, sort=9)
	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下单时间", align=2, sort=10)
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="付款时间", align=2, sort=11)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=12)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="订单状态", dictType="order_status", align=2, sort=13)
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	@ExcelField(title="支付方式", dictType="pay_mode", align=2, sort=14)
	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	
	@ExcelField(title="支付流水号", align=2, sort=15)
	public String getPaySerialNumber() {
		return paySerialNumber;
	}

	public void setPaySerialNumber(String paySerialNumber) {
		this.paySerialNumber = paySerialNumber;
	}
	
	@ExcelField(title="wash_fee", align=2, sort=16)
	public Double getWashFee() {
		return washFee;
	}

	public void setWashFee(Double washFee) {
		this.washFee = washFee;
	}
	
	@ExcelField(title="订单手机号", align=2, sort=17)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="车牌号", align=2, sort=18)
	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	
	public Date getBeginPayTime() {
		return beginPayTime;
	}

	public void setBeginPayTime(Date beginPayTime) {
		this.beginPayTime = beginPayTime;
	}
	
	public Date getEndPayTime() {
		return endPayTime;
	}

	public void setEndPayTime(Date endPayTime) {
		this.endPayTime = endPayTime;
	}
		
}