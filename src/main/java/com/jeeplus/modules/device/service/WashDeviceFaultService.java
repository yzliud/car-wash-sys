/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.device.entity.WashDeviceFault;
import com.jeeplus.modules.device.dao.WashDeviceFaultDao;

/**
 * 设备故障Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashDeviceFaultService extends CrudService<WashDeviceFaultDao, WashDeviceFault> {

	public WashDeviceFault get(String id) {
		return super.get(id);
	}
	
	public List<WashDeviceFault> findList(WashDeviceFault washDeviceFault) {
		return super.findList(washDeviceFault);
	}
	
	public Page<WashDeviceFault> findPage(Page<WashDeviceFault> page, WashDeviceFault washDeviceFault) {
		return super.findPage(page, washDeviceFault);
	}
	
	@Transactional(readOnly = false)
	public void save(WashDeviceFault washDeviceFault) {
		super.save(washDeviceFault);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashDeviceFault washDeviceFault) {
		super.delete(washDeviceFault);
	}
	
	
	
	
}