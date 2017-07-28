/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.account.entity.WashCompanyPurse;
import com.jeeplus.modules.account.dao.WashCompanyPurseDao;

/**
 * 公司账户Service
 * @author LD
 * @version 2017-05-25
 */
@Service
@Transactional(readOnly = true)
public class WashCompanyPurseService extends CrudService<WashCompanyPurseDao, WashCompanyPurse> {

	public WashCompanyPurse get(String id) {
		return super.get(id);
	}
	
	public List<WashCompanyPurse> findList(WashCompanyPurse washCompanyPurse) {
		return super.findList(washCompanyPurse);
	}
	
	public Page<WashCompanyPurse> findPage(Page<WashCompanyPurse> page, WashCompanyPurse washCompanyPurse) {
		return super.findPage(page, washCompanyPurse);
	}
	
	@Transactional(readOnly = false)
	public void save(WashCompanyPurse washCompanyPurse) {
		super.save(washCompanyPurse);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashCompanyPurse washCompanyPurse) {
		super.delete(washCompanyPurse);
	}
	
	public Page<WashMember> findPageBymember(Page<WashMember> page, WashMember member) {
		member.setPage(page);
		page.setList(dao.findListBymember(member));
		return page;
	}
	
	
	
}