/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 优惠卷Entity
 * @author LD
 * @version 2017-05-24
 */
public class WashCoupon extends DataEntity<WashCoupon> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 优惠卷名称
	private String flag;		// 类型(0-抵用卷 1-满减)
	private Integer quantity;		// 数量
	private Double totalAmount;		// 满足金额
	private Double discountAmount;		// 抵用金额
	private Date effectiveTime;		// 生效时间
	private Date failureTime;		// 失效时间
	private String status;		// 状态(0-未发放 1-已发放)
	
	public WashCoupon() {
		super();
	}

	public WashCoupon(String id){
		super(id);
	}

	@ExcelField(title="优惠卷名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="类型", dictType="coupon_flag", align=2, sort=2)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=3)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@ExcelField(title="满足金额", align=2, sort=4)
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@NotNull(message="抵用金额不能为空")
	@ExcelField(title="抵用金额", align=2, sort=5)
	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="生效时间不能为空")
	@ExcelField(title="生效时间", align=2, sort=6)
	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="失效时间不能为空")
	@ExcelField(title="失效时间", align=2, sort=7)
	public Date getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}
	
	@ExcelField(title="状态", dictType="coupon_public_status", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}