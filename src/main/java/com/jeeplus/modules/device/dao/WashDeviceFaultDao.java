/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.device.entity.WashDeviceFault;

/**
 * 设备故障DAO接口
 * @author LD
 * @version 2017-05-18
 */
@MyBatisDao
public interface WashDeviceFaultDao extends CrudDao<WashDeviceFault> {

	
}