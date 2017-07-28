/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.entity;

import com.jeeplus.modules.member.entity.WashMember;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 优惠卷明细Entity
 * @author LD
 * @version 2017-05-24
 */
public class WashCouponDetail extends DataEntity<WashCouponDetail> {
	
	private static final long serialVersionUID = 1L;
	private String couponId;		// 优惠卷ID
	private String couponNo;		// 优惠卷编码
	private String couponName;		// 优惠卷名称
	private String flag;		// 类型(0-抵用卷 1-满减)
	private WashMember washMember;		// 会员ID
	private Double totalAmount;		// 满足金额
	private Double discountAmount;		// 抵用金额
	private Date effectiveTime;		// 生效时间
	private Date failureTime;		// 失效时间
	private String status;		// 状态(0-未领取 1-已领取 2-已使用)
	private String orderNo;		// 订单号
	
	public WashCouponDetail() {
		super();
	}

	public WashCouponDetail(String id){
		super(id);
	}

	@ExcelField(title="优惠卷ID", align=2, sort=1)
	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	@ExcelField(title="优惠卷编码", align=2, sort=2)
	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	
	@ExcelField(title="优惠卷名称", align=2, sort=3)
	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	
	@ExcelField(title="类型", align=2, sort=4)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@ExcelField(title="会员", align=2, sort=5,fieldType=WashMember.class,value="washMember.mobile")
	public WashMember getWashMember() {
		return washMember;
	}

	public void setWashMember(WashMember washMember) {
		this.washMember = washMember;
	}
	
	@ExcelField(title="满足金额", align=2, sort=6)
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@ExcelField(title="抵用金额", align=2, sort=7)
	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="生效时间", align=2, sort=8)
	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="失效时间", align=2, sort=9)
	public Date getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}
	
	@ExcelField(title="状态", dictType="coupon_status", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="订单号", align=2, sort=11)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
}