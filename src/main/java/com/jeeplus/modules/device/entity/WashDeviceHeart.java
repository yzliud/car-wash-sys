/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 心跳Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashDeviceHeart extends DataEntity<WashDeviceHeart> {
	
	private static final long serialVersionUID = 1L;
	private String mac;		// mac
	private Date operatorTime;		// 操作时间
	private Date beginOperatorTime;		// 开始 操作时间
	private Date endOperatorTime;		// 结束 操作时间
	
	public WashDeviceHeart() {
		super();
	}

	public WashDeviceHeart(String id){
		super(id);
	}

	@ExcelField(title="mac", align=2, sort=1)
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=2)
	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
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