/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备故障Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashDeviceFault extends DataEntity<WashDeviceFault> {
	
	private static final long serialVersionUID = 1L;
	private String mac;		// 设备MAC
	private String deviceName;		// 设备名称
	private String deviceAddress;		// 设备地址
	private String status;		// 状态（0-未处理 1已处理）
	private Date faultTime;		// 故障时间
	private String faultDesc;		// 故障问题
	private Date handleTime;		// 处理时间
	
	public WashDeviceFault() {
		super();
	}

	public WashDeviceFault(String id){
		super(id);
	}

	@ExcelField(title="设备MAC", align=2, sort=1)
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	@ExcelField(title="设备名称", align=2, sort=2)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@ExcelField(title="设备地址", align=2, sort=3)
	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
	//@ExcelField(title="状态（0-未处理 1已处理）", dictType="device_fault_status", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="故障时间", align=2, sort=4)
	public Date getFaultTime() {
		return faultTime;
	}

	public void setFaultTime(Date faultTime) {
		this.faultTime = faultTime;
	}
	
	@ExcelField(title="故障问题", align=2, sort=5)
	public String getFaultDesc() {
		return faultDesc;
	}

	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	//@ExcelField(title="处理时间", align=2, sort=7)
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	
}