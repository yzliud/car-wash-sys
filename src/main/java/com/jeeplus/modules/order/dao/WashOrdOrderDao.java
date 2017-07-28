/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.dao;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.order.entity.WashOrdOrder;

/**
 * 订单DAO接口
 * @author LD
 * @version 2017-05-19
 */
@MyBatisDao
public interface WashOrdOrderDao extends CrudDao<WashOrdOrder> {

	public List<WashMember> findListBycarPerson(WashMember carPerson);
	public List<WashMember> findListBywashPerson(WashMember washPerson);
	public List<WashDevice> findListBywashDevice(WashDevice washDevice);
	
}