/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wash.entity;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作记录Entity
 * @author LD
 * @version 2017-05-20
 */
public class WashWorkDeviceRecord extends DataEntity<WashWorkDeviceRecord> {
	
	private static final long serialVersionUID = 1L;
	private WashMember washPerson;		// 洗车工
	private WashDevice washDevice;		// 设备
	private String washDeviceMac;		// 设备MAC
	private Date workOnTime;		// 上班时间
	private Date workOffTime;		// 下班时间
	private String flag;		// 标识(0-上班 1-下班)
	private Date beginWorkOnTime;		// 开始 上班时间
	private Date endWorkOnTime;		// 结束 上班时间
	
	public WashWorkDeviceRecord() {
		super();
	}

	public WashWorkDeviceRecord(String id){
		super(id);
	}

	@ExcelField(title="洗车工", align=2, sort=1, fieldType=WashMember.class,value="washPerson.mobile")
	public WashMember getWashPerson() {
		return washPerson;
	}

	public void setWashPerson(WashMember washPerson) {
		this.washPerson = washPerson;
	}
	
	@ExcelField(title="设备", align=2, sort=2, fieldType=WashDevice.class,value="washDevice.name")
	public WashDevice getWashDevice() {
		return washDevice;
	}

	public void setWashDevice(WashDevice washDevice) {
		this.washDevice = washDevice;
	}
	
	@ExcelField(title="设备MAC", align=2, sort=3)
	public String getWashDeviceMac() {
		return washDeviceMac;
	}

	public void setWashDeviceMac(String washDeviceMac) {
		this.washDeviceMac = washDeviceMac;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上班时间", align=2, sort=4)
	public Date getWorkOnTime() {
		return workOnTime;
	}

	public void setWorkOnTime(Date workOnTime) {
		this.workOnTime = workOnTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下班时间", align=2, sort=5)
	public Date getWorkOffTime() {
		return workOffTime;
	}

	public void setWorkOffTime(Date workOffTime) {
		this.workOffTime = workOffTime;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public Date getBeginWorkOnTime() {
		return beginWorkOnTime;
	}

	public void setBeginWorkOnTime(Date beginWorkOnTime) {
		this.beginWorkOnTime = beginWorkOnTime;
	}
	
	public Date getEndWorkOnTime() {
		return endWorkOnTime;
	}

	public void setEndWorkOnTime(Date endWorkOnTime) {
		this.endWorkOnTime = endWorkOnTime;
	}
		
}