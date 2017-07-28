/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.member.dao.WashMemberDao;

/**
 * 会员Service
 * @author LD
 * @version 2017-05-19
 */
@Service
@Transactional(readOnly = true)
public class WashMemberService extends CrudService<WashMemberDao, WashMember> {

	public WashMember get(String id) {
		return super.get(id);
	}
	
	public List<WashMember> findList(WashMember washMember) {
		return super.findList(washMember);
	}
	
	public Page<WashMember> findPage(Page<WashMember> page, WashMember washMember) {
		return super.findPage(page, washMember);
	}
	
	@Transactional(readOnly = false)
	public void save(WashMember washMember) {
		super.save(washMember);
	}
	
	@Transactional(readOnly = false)
	public void delete(WashMember washMember) {
		super.delete(washMember);
	}
	
	@Transactional(readOnly = false)
	public void set(WashMember washMember) {
		dao.set(washMember);
	}
	
	
}