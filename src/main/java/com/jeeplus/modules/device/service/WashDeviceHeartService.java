/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.device.entity.WashDeviceHeart;
import com.jeeplus.modules.device.dao.WashDeviceHeartDao;

/**
 * 心跳Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashDeviceHeartService extends CrudService<WashDeviceHeartDao, WashDeviceHeart> {

	public WashDeviceHeart get(String id) {
		return super.get(id);
	}
	
	public List<WashDeviceHeart> findList(WashDeviceHeart washDeviceHeart) {
		return super.findList(washDeviceHeart);
	}
	
	public Page<WashDeviceHeart> findPage(Page<WashDeviceHeart> page, WashDeviceHeart washDeviceHeart) {
		return super.findPage(page, washDeviceHeart);
	}
	
	@Transactional(readOnly = false)
	public void save(WashDeviceHeart washDeviceHeart) {
		super.save(washDeviceHeart);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashDeviceHeart washDeviceHeart) {
		super.delete(washDeviceHeart);
	}
	
	
	
	
}