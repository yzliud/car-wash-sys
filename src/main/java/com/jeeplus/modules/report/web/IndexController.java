package com.jeeplus.modules.report.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.echarts.entity.ChinaWeatherDataBean;
import com.jeeplus.modules.report.entity.Index;
import com.jeeplus.modules.report.service.IndexService;

@Controller
@RequestMapping(value = "${adminPath}/report/index")
public class IndexController extends BaseController {
	
	@Autowired
	private IndexService indexService;
	
	@RequestMapping(value = {""})
	public String index(ChinaWeatherDataBean chinaWeatherDataBean, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		/****************折线图*************************/
		//X轴的数据
		List<String> xLineAxisData= new ArrayList<String>();
		//Y轴的数据
		Map<String,List<Double>> yLineAxisData = new HashMap<String,List<Double>>();
		
		List<Index> indexList = indexService.getReport();
		
		List<Double> countLs = new ArrayList<Double>();
		List<Double> payLs = new ArrayList<Double>();

		for(Index index : indexList){
			xLineAxisData.add(index.getPayTime());
			countLs.add(index.getOrderCount());
			payLs.add(index.getOrderCountFee());
		}
		yLineAxisData.put("订单数", countLs);
		yLineAxisData.put("订单金额", payLs);
		
		request.setAttribute("xLineAxisData", xLineAxisData);
		request.setAttribute("yLineAxisData", yLineAxisData);
		
		
		return "modules/report/index";	
	}
}
