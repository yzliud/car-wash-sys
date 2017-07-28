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
import com.jeeplus.modules.account.entity.WashTFlow;
import com.jeeplus.modules.account.dao.WashTFlowDao;

/**
 * 交易流水Service
 * @author LD
 * @version 2017-05-19
 */
@Service
@Transactional(readOnly = true)
public class WashTFlowService extends CrudService<WashTFlowDao, WashTFlow> {

	public WashTFlow get(String id) {
		return super.get(id);
	}
	
	public List<WashTFlow> findList(WashTFlow washTFlow) {
		return super.findList(washTFlow);
	}
	
	public Page<WashTFlow> findPage(Page<WashTFlow> page, WashTFlow washTFlow) {
		return super.findPage(page, washTFlow);
	}
	
	@Transactional(readOnly = false)
	public void save(WashTFlow washTFlow) {
		super.save(washTFlow);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashTFlow washTFlow) {
		super.delete(washTFlow);
	}
	
	public Page<WashMember> findPageBymember(Page<WashMember> page, WashMember member) {
		member.setPage(page);
		page.setList(dao.findListBymember(member));
		return page;
	}
	
	
	
}