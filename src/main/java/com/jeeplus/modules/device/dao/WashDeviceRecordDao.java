/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.dao;

import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.modules.member.entity.WashMember;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.device.entity.WashDeviceRecord;

/**
 * 设备使用记录DAO接口
 * @author LD
 * @version 2017-05-18
 */
@MyBatisDao
public interface WashDeviceRecordDao extends CrudDao<WashDeviceRecord> {

	public List<WashDevice> findListBywashDevice(WashDevice washDevice);
	public List<WashMember> findListBywashMember(WashMember washMember);
	
}