/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;

import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.modules.member.entity.WashMember;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备使用记录Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashDeviceRecord extends DataEntity<WashDeviceRecord> {
	
	private static final long serialVersionUID = 1L;
	private WashDevice washDevice;		// 设备
	private WashMember washMember;		// 洗车工
	private String orderId;		// 订单
	private String msg;		// 发送指令
	private Date operatorTime;		// 操作时间
	private Double duration;		// 时长
	private Date beginOperatorTime;		// 开始 操作时间
	private Date endOperatorTime;		// 结束 操作时间
	
	public WashDeviceRecord() {
		super();
	}

	public WashDeviceRecord(String id){
		super(id);
	}

	@ExcelField(title="设备", align=2, sort=1)
	public WashDevice getWashDevice() {
		return washDevice;
	}

	public void setWashDevice(WashDevice washDevice) {
		this.washDevice = washDevice;
	}
	
	@ExcelField(title="洗车工", align=2, sort=2)
	public WashMember getWashMember() {
		return washMember;
	}

	public void setWashMember(WashMember washMember) {
		this.washMember = washMember;
	}
	
	@ExcelField(title="订单", align=2, sort=3)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@ExcelField(title="发送指令", align=2, sort=4)
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=5)
	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}
	
	@ExcelField(title="时长", align=2, sort=6)
	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}
	
	public Date getBeginOperatorTime() {
		return beginOperatorTime;
	}

	public void setBeginOperatorTime(Date beginOperatorTime) {
		this.beginOperatorTime = beginOperatorTime;
	}
	
	public Date getEndOperatorTime() {
		return endOperatorTime;
	}

	public void setEndOperatorTime(Date endOperatorTime) {
		this.endOperatorTime = endOperatorTime;
	}
		
}