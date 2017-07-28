/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.meal.web;

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
import com.jeeplus.modules.meal.entity.WashSetMeal;
import com.jeeplus.modules.meal.service.WashSetMealService;

/**
 * 套餐Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/meal/washSetMeal")
public class WashSetMealController extends BaseController {

	@Autowired
	private WashSetMealService washSetMealService;
	
	@ModelAttribute
	public WashSetMeal get(@RequestParam(required=false) String id) {
		WashSetMeal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washSetMealService.get(id);
		}
		if (entity == null){
			entity = new WashSetMeal();
		}
		return entity;
	}
	
	/**
	 * 套餐列表页面
	 */
	@RequiresPermissions("meal:washSetMeal:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashSetMeal washSetMeal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashSetMeal> page = washSetMealService.findPage(new Page<WashSetMeal>(request, response), washSetMeal); 
		model.addAttribute("page", page);
		return "modules/meal/washSetMealList";
	}

	/**
	 * 查看，增加，编辑套餐表单页面
	 */
	@RequiresPermissions(value={"meal:washSetMeal:view","meal:washSetMeal:add","meal:washSetMeal:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashSetMeal washSetMeal, Model model) {
		model.addAttribute("washSetMeal", washSetMeal);
		return "modules/meal/washSetMealForm";
	}

	/**
	 * 保存套餐
	 */
	@RequiresPermissions(value={"meal:washSetMeal:add","meal:washSetMeal:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashSetMeal washSetMeal, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washSetMeal)){
			return form(washSetMeal, model);
		}
		if(!washSetMeal.getIsNewRecord()){//编辑表单保存
			WashSetMeal t = washSetMealService.get(washSetMeal.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washSetMeal, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washSetMealService.save(t);//保存
		}else{//新增表单保存
			washSetMealService.save(washSetMeal);//保存
		}
		addMessage(redirectAttributes, "保存套餐成功");
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
	}
	
	/**
	 * 删除套餐
	 */
	@RequiresPermissions("meal:washSetMeal:del")
	@RequestMapping(value = "delete")
	public String delete(WashSetMeal washSetMeal, RedirectAttributes redirectAttributes) {
		washSetMealService.delete(washSetMeal);
		addMessage(redirectAttributes, "删除套餐成功");
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
	}
	
	/**
	 * 批量删除套餐
	 */
	@RequiresPermissions("meal:washSetMeal:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washSetMealService.delete(washSetMealService.get(id));
		}
		addMessage(redirectAttributes, "删除套餐成功");
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("meal:washSetMeal:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashSetMeal washSetMeal, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashSetMeal> page = washSetMealService.findPage(new Page<WashSetMeal>(request, response, -1), washSetMeal);
    		new ExportExcel("套餐", WashSetMeal.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出套餐记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("meal:washSetMeal:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashSetMeal> list = ei.getDataList(WashSetMeal.class);
			for (WashSetMeal washSetMeal : list){
				try{
					washSetMealService.save(washSetMeal);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条套餐记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条套餐记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入套餐失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
    }
	
	/**
	 * 下载导入套餐数据模板
	 */
	@RequiresPermissions("meal:washSetMeal:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐数据导入模板.xlsx";
    		List<WashSetMeal> list = Lists.newArrayList(); 
    		new ExportExcel("套餐数据", WashSetMeal.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/meal/washSetMeal/?repage";
    }
	
	
	

}