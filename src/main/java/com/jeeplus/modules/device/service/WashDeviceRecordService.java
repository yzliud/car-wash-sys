/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.device.entity.WashDeviceRecord;
import com.jeeplus.modules.device.dao.WashDeviceRecordDao;

/**
 * 设备使用记录Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashDeviceRecordService extends CrudService<WashDeviceRecordDao, WashDeviceRecord> {

	public WashDeviceRecord get(String id) {
		return super.get(id);
	}
	
	public List<WashDeviceRecord> findList(WashDeviceRecord washDeviceRecord) {
		return super.findList(washDeviceRecord);
	}
	
	public Page<WashDeviceRecord> findPage(Page<WashDeviceRecord> page, WashDeviceRecord washDeviceRecord) {
		return super.findPage(page, washDeviceRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(WashDeviceRecord washDeviceRecord) {
		super.save(washDeviceRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashDeviceRecord washDeviceRecord) {
		super.delete(washDeviceRecord);
	}
	
	public Page<WashDevice> findPageBywashDevice(Page<WashDevice> page, WashDevice washDevice) {
		washDevice.setPage(page);
		page.setList(dao.findListBywashDevice(washDevice));
		return page;
	}
	public Page<WashMember> findPageBywashMember(Page<WashMember> page, WashMember washMember) {
		washMember.setPage(page);
		page.setList(dao.findListBywashMember(washMember));
		return page;
	}
	
	
	
}