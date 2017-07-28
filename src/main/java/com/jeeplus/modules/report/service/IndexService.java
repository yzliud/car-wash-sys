package com.jeeplus.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.report.dao.IndexDao;
import com.jeeplus.modules.report.entity.Index;

@Service
public class IndexService extends CrudService<IndexDao, Index> {

	public List<Index> getReport() {
		return dao.getReport();
	}
}
