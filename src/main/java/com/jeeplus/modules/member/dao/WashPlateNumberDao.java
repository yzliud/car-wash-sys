/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.member.entity.WashPlateNumber;

/**
 * 内部车牌DAO接口
 * @author 菜鸟
 * @version 2017-06-15
 */
@MyBatisDao
public interface WashPlateNumberDao extends CrudDao<WashPlateNumber> {

	public void updateFee(WashPlateNumber entity);
	
}