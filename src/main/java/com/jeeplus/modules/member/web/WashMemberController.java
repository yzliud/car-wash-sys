/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.web;

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
import com.jeeplus.modules.member.entity.WashMember;
import com.jeeplus.modules.member.service.WashMemberService;

/**
 * 会员Controller
 * @author LD
 * @version 2017-05-19
 */
@Controller
@RequestMapping(value = "${adminPath}/member/washMember")
public class WashMemberController extends BaseController {

	@Autowired
	private WashMemberService washMemberService;
	
	@ModelAttribute
	public WashMember get(@RequestParam(required=false) String id) {
		WashMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washMemberService.get(id);
		}
		if (entity == null){
			entity = new WashMember();
		}
		return entity;
	}
	
	/**
	 * 会员列表页面
	 */
	@RequiresPermissions("member:washMember:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashMember washMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washMemberService.findPage(new Page<WashMember>(request, response), washMember); 
		model.addAttribute("page", page);
		return "modules/member/washMemberList";
	}

	/**
	 * 查看，增加，编辑会员表单页面
	 */
	@RequiresPermissions(value={"member:washMember:view","member:washMember:add","member:washMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashMember washMember, Model model) {
		model.addAttribute("washMember", washMember);
		return "modules/member/washMemberForm";
	}

	/**
	 * 保存会员
	 */
	@RequiresPermissions(value={"member:washMember:add","member:washMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashMember washMember, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washMember)){
			return form(washMember, model);
		}
		if(!washMember.getIsNewRecord()){//编辑表单保存
			WashMember t = washMemberService.get(washMember.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washMember, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washMemberService.save(t);//保存
		}else{//新增表单保存
			washMemberService.save(washMember);//保存
		}
		addMessage(redirectAttributes, "保存会员成功");
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
	}
	
	/**
	 * 删除会员
	 */
	@RequiresPermissions("member:washMember:del")
	@RequestMapping(value = "delete")
	public String delete(WashMember washMember, RedirectAttributes redirectAttributes) {
		washMemberService.delete(washMember);
		addMessage(redirectAttributes, "删除会员成功");
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
	}
	
	/**
	 * 批量删除会员
	 */
	@RequiresPermissions("member:washMember:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washMemberService.delete(washMemberService.get(id));
		}
		addMessage(redirectAttributes, "删除会员成功");
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("member:washMember:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashMember washMember, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "会员"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashMember> page = washMemberService.findPage(new Page<WashMember>(request, response, -1), washMember);
    		new ExportExcel("会员", WashMember.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出会员记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("member:washMember:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashMember> list = ei.getDataList(WashMember.class);
			for (WashMember washMember : list){
				try{
					washMemberService.save(washMember);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条会员记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条会员记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入会员失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
    }
	
	/**
	 * 下载导入会员数据模板
	 */
	@RequiresPermissions("member:washMember:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "会员数据导入模板.xlsx";
    		List<WashMember> list = Lists.newArrayList(); 
    		new ExportExcel("会员数据", WashMember.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
    }
	
	@RequestMapping(value = "forwardSet")
	public String forwardSet(WashMember washMember, Model model) {
		model.addAttribute("washMember", washMember);
		return "modules/member/washMemberFormSet";
	}

	@RequestMapping(value = "set")
	public String set(WashMember washMember, Model model, RedirectAttributes redirectAttributes) throws Exception{
		washMemberService.set(washMember);//保存
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+Global.getAdminPath()+"/member/washMember/?repage";
	}
	

}