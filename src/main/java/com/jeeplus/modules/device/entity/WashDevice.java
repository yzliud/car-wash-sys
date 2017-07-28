/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashDevice extends DataEntity<WashDevice> {
	
	private static final long serialVersionUID = 1L;
	private String mac;		// mac
	private String name;		// 名称
	private String address;		// 地址
	private String status;		// 状态(0-正常 1-离线 2-故障)
	
	public WashDevice() {
		super();
	}

	public WashDevice(String id){
		super(id);
	}

	@ExcelField(title="mac", align=2, sort=1)
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	@ExcelField(title="名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="地址", align=2, sort=3)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="状态(0-正常 1-离线 2-故障)", dictType="device_status", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}