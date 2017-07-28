/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao;

import com.jeeplus.modules.member.entity.WashMember;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.WashTFlow;

/**
 * 交易流水DAO接口
 * @author LD
 * @version 2017-05-19
 */
@MyBatisDao
public interface WashTFlowDao extends CrudDao<WashTFlow> {

	public List<WashMember> findListBymember(WashMember member);
	
}