/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.jeeplus.modules.member.entity.WashMember;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.account.entity.WashTFlow;
import com.jeeplus.modules.account.service.WashTFlowService;

/**
 * 交易流水Controller
 * @author LD
 * @version 2017-05-19
 */
@Controller
@RequestMapping(value = "${adminPath}/account/washTFlow")
public class WashTFlowController extends BaseController {

	@Autowired
	private WashTFlowService washTFlowService;
	
	@ModelAttribute
	public WashTFlow get(@RequestParam(required=false) String id) {
		WashTFlow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washTFlowService.get(id);
		}
		if (entity == null){
			entity = new WashTFlow();
		}
		return entity;
	}
	
	/**
	 * 交易流水列表页面
	 */
	@RequiresPermissions("account:washTFlow:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashTFlow washTFlow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashTFlow> page = washTFlowService.findPage(new Page<WashTFlow>(request, response), washTFlow); 
		model.addAttribute("page", page);
		return "modules/account/washTFlowList";
	}

	/**
	 * 查看，增加，编辑交易流水表单页面
	 */
	@RequiresPermissions(value={"account:washTFlow:view","account:washTFlow:add","account:washTFlow:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashTFlow washTFlow, Model model) {
		model.addAttribute("washTFlow", washTFlow);
		return "modules/account/washTFlowForm";
	}

	/**
	 * 保存交易流水
	 */
	@RequiresPermissions(value={"account:washTFlow:add","account:washTFlow:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashTFlow washTFlow, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washTFlow)){
			return form(washTFlow, model);
		}
		if(!washTFlow.getIsNewRecord()){//编辑表单保存
			WashTFlow t = washTFlowService.get(washTFlow.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washTFlow, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washTFlowService.save(t);//保存
		}else{//新增表单保存
			washTFlowService.save(washTFlow);//保存
		}
		addMessage(redirectAttributes, "保存交易流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
	}
	
	/**
	 * 删除交易流水
	 */
	@RequiresPermissions("account:washTFlow:del")
	@RequestMapping(value = "delete")
	public String delete(WashTFlow washTFlow, RedirectAttributes redirectAttributes) {
		washTFlowService.delete(washTFlow);
		addMessage(redirectAttributes, "删除交易流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
	}
	
	/**
	 * 批量删除交易流水
	 */
	@RequiresPermissions("account:washTFlow:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washTFlowService.delete(washTFlowService.get(id));
		}
		addMessage(redirectAttributes, "删除交易流水成功");
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:washTFlow:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashTFlow washTFlow, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "交易流水"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashTFlow> page = washTFlowService.findPage(new Page<WashTFlow>(request, response, -1), washTFlow);
    		new ExportExcel("交易流水", WashTFlow.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出交易流水记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:washTFlow:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashTFlow> list = ei.getDataList(WashTFlow.class);
			for (WashTFlow washTFlow : list){
				try{
					washTFlowService.save(washTFlow);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条交易流水记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条交易流水记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入交易流水失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
    }
	
	/**
	 * 下载导入交易流水数据模板
	 */
	@RequiresPermissions("account:washTFlow:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "交易流水数据导入模板.xlsx";
    		List<WashTFlow> list = Lists.newArrayList(); 
    		new ExportExcel("交易流水数据", WashTFlow.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washTFlow/?repage";
    }
	
	
	/**
	 * 选择会员
	 */
	@RequestMapping(value = "selectmember")
	public String selectmember(WashMember member, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washTFlowService.findPageBymember(new Page<WashMember>(request, response),  member);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", member);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}