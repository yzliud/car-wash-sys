/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.order.entity.WashOrdOrder;
import com.jeeplus.modules.order.dao.WashOrdOrderDao;

/**
 * 订单Service
 * @author LD
 * @version 2017-05-19
 */
@Service
@Transactional(readOnly = true)
public class WashOrdOrderService extends CrudService<WashOrdOrderDao, WashOrdOrder> {

	public WashOrdOrder get(String id) {
		return super.get(id);
	}
	
	public List<WashOrdOrder> findList(WashOrdOrder washOrdOrder) {
		return super.findList(washOrdOrder);
	}
	
	public Page<WashOrdOrder> findPage(Page<WashOrdOrder> page, WashOrdOrder washOrdOrder) {
		return super.findPage(page, washOrdOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(WashOrdOrder washOrdOrder) {
		super.save(washOrdOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashOrdOrder washOrdOrder) {
		super.delete(washOrdOrder);
	}
	
	public Page<WashMember> findPageBycarPerson(Page<WashMember> page, WashMember carPerson) {
		carPerson.setPage(page);
		page.setList(dao.findListBycarPerson(carPerson));
		return page;
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