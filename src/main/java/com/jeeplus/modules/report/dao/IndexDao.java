package com.jeeplus.modules.report.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.report.entity.Index;

@MyBatisDao
public interface IndexDao extends CrudDao<Index>{

	public List<Index> getReport();

}