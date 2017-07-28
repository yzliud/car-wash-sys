/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.meal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.meal.entity.WashSetMeal;
import com.jeeplus.modules.meal.dao.WashSetMealDao;

/**
 * 套餐Service
 * @author LD
 * @version 2017-05-18
 */
@Service
@Transactional(readOnly = true)
public class WashSetMealService extends CrudService<WashSetMealDao, WashSetMeal> {

	public WashSetMeal get(String id) {
		return super.get(id);
	}
	
	public List<WashSetMeal> findList(WashSetMeal washSetMeal) {
		return super.findList(washSetMeal);
	}
	
	public Page<WashSetMeal> findPage(Page<WashSetMeal> page, WashSetMeal washSetMeal) {
		return super.findPage(page, washSetMeal);
	}
	
	@Transactional(readOnly = false)
	public void save(WashSetMeal washSetMeal) {
		super.save(washSetMeal);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashSetMeal washSetMeal) {
		super.delete(washSetMeal);
	}
	
	
	
	
}