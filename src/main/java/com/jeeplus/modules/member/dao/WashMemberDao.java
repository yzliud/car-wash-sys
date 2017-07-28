/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.member.entity.WashMember;

/**
 * 会员DAO接口
 * @author LD
 * @version 2017-05-19
 */
@MyBatisDao
public interface WashMemberDao extends CrudDao<WashMember> {

	public int set(WashMember entity);
}