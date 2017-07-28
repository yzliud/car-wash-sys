/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wash.dao;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.device.entity.WashDevice;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.wash.entity.WashWorkDeviceRecord;

/**
 * 工作记录DAO接口
 * @author LD
 * @version 2017-05-20
 */
@MyBatisDao
public interface WashWorkDeviceRecordDao extends CrudDao<WashWorkDeviceRecord> {

	public List<WashMember> findListBywashPerson(WashMember washPerson);
	public List<WashDevice> findListBywashDevice(WashDevice washDevice);
	
}