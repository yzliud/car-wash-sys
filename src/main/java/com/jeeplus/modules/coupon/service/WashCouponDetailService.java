/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.coupon.entity.WashCouponDetail;
import com.jeeplus.modules.coupon.dao.WashCouponDetailDao;

/**
 * 优惠卷明细Service
 * @author LD
 * @version 2017-05-24
 */
@Service
@Transactional(readOnly = true)
public class WashCouponDetailService extends CrudService<WashCouponDetailDao, WashCouponDetail> {

	public WashCouponDetail get(String id) {
		return super.get(id);
	}
	
	public List<WashCouponDetail> findList(WashCouponDetail washCouponDetail) {
		return super.findList(washCouponDetail);
	}
	
	public Page<WashCouponDetail> findPage(Page<WashCouponDetail> page, WashCouponDetail washCouponDetail) {
		return super.findPage(page, washCouponDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(WashCouponDetail washCouponDetail) {
		super.save(washCouponDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashCouponDetail washCouponDetail) {
		super.delete(washCouponDetail);
	}
	
	public Page<WashMember> findPageBywashMember(Page<WashMember> page, WashMember washMember) {
		washMember.setPage(page);
		page.setList(dao.findListBywashMember(washMember));
		return page;
	}
	
	
	
}