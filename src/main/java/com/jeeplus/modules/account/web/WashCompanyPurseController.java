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
import com.jeeplus.modules.account.entity.WashCompanyPurse;
import com.jeeplus.modules.account.service.WashCompanyPurseService;

/**
 * 公司账户Controller
 * @author LD
 * @version 2017-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/account/washCompanyPurse")
public class WashCompanyPurseController extends BaseController {

	@Autowired
	private WashCompanyPurseService washCompanyPurseService;
	
	@ModelAttribute
	public WashCompanyPurse get(@RequestParam(required=false) String id) {
		WashCompanyPurse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washCompanyPurseService.get(id);
		}
		if (entity == null){
			entity = new WashCompanyPurse();
		}
		return entity;
	}
	
	/**
	 * 公司账户列表页面
	 */
	@RequiresPermissions("account:washCompanyPurse:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashCompanyPurse washCompanyPurse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashCompanyPurse> page = washCompanyPurseService.findPage(new Page<WashCompanyPurse>(request, response), washCompanyPurse); 
		model.addAttribute("page", page);
		return "modules/account/washCompanyPurseList";
	}

	/**
	 * 查看，增加，编辑公司账户表单页面
	 */
	@RequiresPermissions(value={"account:washCompanyPurse:view","account:washCompanyPurse:add","account:washCompanyPurse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashCompanyPurse washCompanyPurse, Model model) {
		model.addAttribute("washCompanyPurse", washCompanyPurse);
		return "modules/account/washCompanyPurseForm";
	}

	/**
	 * 保存公司账户
	 */
	@RequiresPermissions(value={"account:washCompanyPurse:add","account:washCompanyPurse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashCompanyPurse washCompanyPurse, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washCompanyPurse)){
			return form(washCompanyPurse, model);
		}
		if(!washCompanyPurse.getIsNewRecord()){//编辑表单保存
			WashCompanyPurse t = washCompanyPurseService.get(washCompanyPurse.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washCompanyPurse, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washCompanyPurseService.save(t);//保存
		}else{//新增表单保存
			washCompanyPurseService.save(washCompanyPurse);//保存
		}
		addMessage(redirectAttributes, "保存公司账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
	}
	
	/**
	 * 删除公司账户
	 */
	@RequiresPermissions("account:washCompanyPurse:del")
	@RequestMapping(value = "delete")
	public String delete(WashCompanyPurse washCompanyPurse, RedirectAttributes redirectAttributes) {
		washCompanyPurseService.delete(washCompanyPurse);
		addMessage(redirectAttributes, "删除公司账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
	}
	
	/**
	 * 批量删除公司账户
	 */
	@RequiresPermissions("account:washCompanyPurse:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washCompanyPurseService.delete(washCompanyPurseService.get(id));
		}
		addMessage(redirectAttributes, "删除公司账户成功");
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("account:washCompanyPurse:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashCompanyPurse washCompanyPurse, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "公司账户"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashCompanyPurse> page = washCompanyPurseService.findPage(new Page<WashCompanyPurse>(request, response, -1), washCompanyPurse);
    		new ExportExcel("公司账户", WashCompanyPurse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出公司账户记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("account:washCompanyPurse:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashCompanyPurse> list = ei.getDataList(WashCompanyPurse.class);
			for (WashCompanyPurse washCompanyPurse : list){
				try{
					washCompanyPurseService.save(washCompanyPurse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条公司账户记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条公司账户记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公司账户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
    }
	
	/**
	 * 下载导入公司账户数据模板
	 */
	@RequiresPermissions("account:washCompanyPurse:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "公司账户数据导入模板.xlsx";
    		List<WashCompanyPurse> list = Lists.newArrayList(); 
    		new ExportExcel("公司账户数据", WashCompanyPurse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/account/washCompanyPurse/?repage";
    }
	
	
	/**
	 * 选择会员
	 */
	@RequestMapping(value = "selectmember")
	public String selectmember(WashMember member, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washCompanyPurseService.findPageBymember(new Page<WashMember>(request, response),  member);
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