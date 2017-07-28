/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wash.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.wash.entity.WashWorkDeviceRecord;
import com.jeeplus.modules.wash.dao.WashWorkDeviceRecordDao;

/**
 * 工作记录Service
 * @author LD
 * @version 2017-05-20
 */
@Service
@Transactional(readOnly = true)
public class WashWorkDeviceRecordService extends CrudService<WashWorkDeviceRecordDao, WashWorkDeviceRecord> {

	public WashWorkDeviceRecord get(String id) {
		return super.get(id);
	}
	
	public List<WashWorkDeviceRecord> findList(WashWorkDeviceRecord washWorkDeviceRecord) {
		return super.findList(washWorkDeviceRecord);
	}
	
	public Page<WashWorkDeviceRecord> findPage(Page<WashWorkDeviceRecord> page, WashWorkDeviceRecord washWorkDeviceRecord) {
		return super.findPage(page, washWorkDeviceRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(WashWorkDeviceRecord washWorkDeviceRecord) {
		super.save(washWorkDeviceRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashWorkDeviceRecord washWorkDeviceRecord) {
		super.delete(washWorkDeviceRecord);
	}
	
	public Page<WashMember> findPageBywashPerson(Page<WashMember> page, WashMember washPerson) {
		washPerson.setPage(page);
		page.setList(dao.findListBywashPerson(washPerson));
		return page;
	}
	public Page<WashDevice> findPageBywashDevice(Page<WashDevice> page, WashDevice washDevice) {
		washDevice.setPage(page);
		page.setList(dao.findListBywashDevice(washDevice));
		return page;
	}
	
	
	
}