/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.order.entity.WashOrdEvaluate;
import com.jeeplus.modules.order.dao.WashOrdEvaluateDao;

/**
 * 订单评价Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashOrdEvaluateService extends CrudService<WashOrdEvaluateDao, WashOrdEvaluate> {

	public WashOrdEvaluate get(String id) {
		return super.get(id);
	}
	
	public List<WashOrdEvaluate> findList(WashOrdEvaluate washOrdEvaluate) {
		return super.findList(washOrdEvaluate);
	}
	
	public Page<WashOrdEvaluate> findPage(Page<WashOrdEvaluate> page, WashOrdEvaluate washOrdEvaluate) {
		return super.findPage(page, washOrdEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void save(WashOrdEvaluate washOrdEvaluate) {
		super.save(washOrdEvaluate);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashOrdEvaluate washOrdEvaluate) {
		super.delete(washOrdEvaluate);
	}
	
	
	
	
}