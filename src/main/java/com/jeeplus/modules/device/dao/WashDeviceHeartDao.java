/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.device.entity.WashDeviceHeart;

/**
 * 心跳DAO接口
 * @author LD
 * @version 2017-05-18
 */
@MyBatisDao
public interface WashDeviceHeartDao extends CrudDao<WashDeviceHeart> {

	
}