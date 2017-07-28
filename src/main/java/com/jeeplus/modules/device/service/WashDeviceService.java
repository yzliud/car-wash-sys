/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.modules.device.dao.WashDeviceDao;

/**
 * 设备Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashDeviceService extends CrudService<WashDeviceDao, WashDevice> {

	public WashDevice get(String id) {
		return super.get(id);
	}
	
	public List<WashDevice> findList(WashDevice washDevice) {
		return super.findList(washDevice);
	}
	
	public Page<WashDevice> findPage(Page<WashDevice> page, WashDevice washDevice) {
		return super.findPage(page, washDevice);
	}
	
	@Transactional(readOnly = false)
	public void save(WashDevice washDevice) {
		super.save(washDevice);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashDevice washDevice) {
		super.delete(washDevice);
	}
	
	public List<WashDevice> findListCheck(WashDevice washDevice) {
		return dao.findListCheck(washDevice);
	}
	
	
}