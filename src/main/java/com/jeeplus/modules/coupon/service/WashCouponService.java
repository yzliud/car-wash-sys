/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.coupon.entity.WashCoupon;
import com.jeeplus.modules.coupon.dao.WashCouponDao;

/**
 * 优惠卷Service
 * @author LD
 * @version 2017-05-24
 */
@Service
@Transactional(readOnly = true)
public class WashCouponService extends CrudService<WashCouponDao, WashCoupon> {

	public WashCoupon get(String id) {
		return super.get(id);
	}
	
	public List<WashCoupon> findList(WashCoupon washCoupon) {
		return super.findList(washCoupon);
	}
	
	public Page<WashCoupon> findPage(Page<WashCoupon> page, WashCoupon washCoupon) {
		return super.findPage(page, washCoupon);
	}
	
	@Transactional(readOnly = false)
	public void save(WashCoupon washCoupon) {
		super.save(washCoupon);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashCoupon washCoupon) {
		super.delete(washCoupon);
	}
	
	
	
	
}