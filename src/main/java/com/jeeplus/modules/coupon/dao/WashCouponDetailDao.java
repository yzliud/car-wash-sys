/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.dao;

import com.jeeplus.modules.member.entity.WashMember;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.coupon.entity.WashCouponDetail;

/**
 * 优惠卷明细DAO接口
 * @author LD
 * @version 2017-05-24
 */
@MyBatisDao
public interface WashCouponDetailDao extends CrudDao<WashCouponDetail> {

	public List<WashMember> findListBywashMember(WashMember washMember);
	
}