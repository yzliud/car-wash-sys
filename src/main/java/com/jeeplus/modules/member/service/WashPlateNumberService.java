/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.member.entity.WashPlateNumber;
import com.jeeplus.modules.member.dao.WashPlateNumberDao;

/**
 * 内部车牌Service
 * @author 菜鸟
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class WashPlateNumberService extends CrudService<WashPlateNumberDao, WashPlateNumber> {

	public WashPlateNumber get(String id) {
		return super.get(id);
	}
	
	public List<WashPlateNumber> findList(WashPlateNumber washPlateNumber) {
		return super.findList(washPlateNumber);
	}
	
	public Page<WashPlateNumber> findPage(Page<WashPlateNumber> page, WashPlateNumber washPlateNumber) {
		return super.findPage(page, washPlateNumber);
	}
	
	@Transactional(readOnly = false)
	public void save(WashPlateNumber washPlateNumber) {
		super.save(washPlateNumber);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashPlateNumber washPlateNumber) {
		super.delete(washPlateNumber);
	}
	
	@Transactional(readOnly = false)
	public void updateFee(WashPlateNumber washPlateNumber) {
		dao.updateFee(washPlateNumber);
	}
	
	@Transactional(readOnly = false)
	public WashPlateNumber findUniqueByProperty(String propertyName, Object value) {
		return super.findUniqueByProperty(propertyName, value);
	}
	
	@Transactional(readOnly = false)
	public void excelImport(WashPlateNumber washPlateNumber) {
		WashPlateNumber wpn = super.findUniqueByProperty("car_number", washPlateNumber.getCarNumber());
		if(null != wpn){
			washPlateNumber.setId(wpn.getId());
		}
		super.save(washPlateNumber);
	}
}