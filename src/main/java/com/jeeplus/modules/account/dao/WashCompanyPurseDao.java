/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.dao;

import com.jeeplus.modules.member.entity.WashMember;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.account.entity.WashCompanyPurse;

/**
 * 公司账户DAO接口
 * @author LD
 * @version 2017-05-25
 */
@MyBatisDao
public interface WashCompanyPurseDao extends CrudDao<WashCompanyPurse> {

	public List<WashMember> findListBymember(WashMember member);
	
}