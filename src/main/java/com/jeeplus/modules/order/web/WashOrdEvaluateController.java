/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.order.entity.WashOrdEvaluate;
import com.jeeplus.modules.order.service.WashOrdEvaluateService;

/**
 * 订单评价Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/order/washOrdEvaluate")
public class WashOrdEvaluateController extends BaseController {

	@Autowired
	private WashOrdEvaluateService washOrdEvaluateService;
	
	@ModelAttribute
	public WashOrdEvaluate get(@RequestParam(required=false) String id) {
		WashOrdEvaluate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washOrdEvaluateService.get(id);
		}
		if (entity == null){
			entity = new WashOrdEvaluate();
		}
		return entity;
	}
	
	/**
	 * 订单评价列表页面
	 */
	@RequiresPermissions("order:washOrdEvaluate:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashOrdEvaluate washOrdEvaluate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashOrdEvaluate> page = washOrdEvaluateService.findPage(new Page<WashOrdEvaluate>(request, response), washOrdEvaluate); 
		model.addAttribute("page", page);
		return "modules/order/washOrdEvaluateList";
	}

	/**
	 * 查看，增加，编辑订单评价表单页面
	 */
	@RequiresPermissions(value={"order:washOrdEvaluate:view","order:washOrdEvaluate:add","order:washOrdEvaluate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashOrdEvaluate washOrdEvaluate, Model model) {
		model.addAttribute("washOrdEvaluate", washOrdEvaluate);
		return "modules/order/washOrdEvaluateForm";
	}

	/**
	 * 保存订单评价
	 */
	@RequiresPermissions(value={"order:washOrdEvaluate:add","order:washOrdEvaluate:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashOrdEvaluate washOrdEvaluate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washOrdEvaluate)){
			return form(washOrdEvaluate, model);
		}
		if(!washOrdEvaluate.getIsNewRecord()){//编辑表单保存
			WashOrdEvaluate t = washOrdEvaluateService.get(washOrdEvaluate.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washOrdEvaluate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washOrdEvaluateService.save(t);//保存
		}else{//新增表单保存
			washOrdEvaluateService.save(washOrdEvaluate);//保存
		}
		addMessage(redirectAttributes, "保存订单评价成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
	}
	
	/**
	 * 删除订单评价
	 */
	@RequiresPermissions("order:washOrdEvaluate:del")
	@RequestMapping(value = "delete")
	public String delete(WashOrdEvaluate washOrdEvaluate, RedirectAttributes redirectAttributes) {
		washOrdEvaluateService.delete(washOrdEvaluate);
		addMessage(redirectAttributes, "删除订单评价成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
	}
	
	/**
	 * 批量删除订单评价
	 */
	@RequiresPermissions("order:washOrdEvaluate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washOrdEvaluateService.delete(washOrdEvaluateService.get(id));
		}
		addMessage(redirectAttributes, "删除订单评价成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:washOrdEvaluate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashOrdEvaluate washOrdEvaluate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单评价"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashOrdEvaluate> page = washOrdEvaluateService.findPage(new Page<WashOrdEvaluate>(request, response, -1), washOrdEvaluate);
    		new ExportExcel("订单评价", WashOrdEvaluate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单评价记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("order:washOrdEvaluate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashOrdEvaluate> list = ei.getDataList(WashOrdEvaluate.class);
			for (WashOrdEvaluate washOrdEvaluate : list){
				try{
					washOrdEvaluateService.save(washOrdEvaluate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单评价记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单评价记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单评价失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
    }
	
	/**
	 * 下载导入订单评价数据模板
	 */
	@RequiresPermissions("order:washOrdEvaluate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单评价数据导入模板.xlsx";
    		List<WashOrdEvaluate> list = Lists.newArrayList(); 
    		new ExportExcel("订单评价数据", WashOrdEvaluate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdEvaluate/?repage";
    }
	
	
	

}