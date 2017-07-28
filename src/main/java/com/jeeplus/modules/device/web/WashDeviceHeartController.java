/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.web;

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
import com.jeeplus.modules.device.entity.WashDeviceHeart;
import com.jeeplus.modules.device.service.WashDeviceHeartService;

/**
 * 心跳Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/device/washDeviceHeart")
public class WashDeviceHeartController extends BaseController {

	@Autowired
	private WashDeviceHeartService washDeviceHeartService;
	
	@ModelAttribute
	public WashDeviceHeart get(@RequestParam(required=false) String id) {
		WashDeviceHeart entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washDeviceHeartService.get(id);
		}
		if (entity == null){
			entity = new WashDeviceHeart();
		}
		return entity;
	}
	
	/**
	 * 心跳列表页面
	 */
	@RequiresPermissions("device:washDeviceHeart:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashDeviceHeart washDeviceHeart, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDeviceHeart> page = washDeviceHeartService.findPage(new Page<WashDeviceHeart>(request, response), washDeviceHeart); 
		model.addAttribute("page", page);
		return "modules/device/washDeviceHeartList";
	}

	/**
	 * 查看，增加，编辑心跳表单页面
	 */
	@RequiresPermissions(value={"device:washDeviceHeart:view","device:washDeviceHeart:add","device:washDeviceHeart:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashDeviceHeart washDeviceHeart, Model model) {
		model.addAttribute("washDeviceHeart", washDeviceHeart);
		return "modules/device/washDeviceHeartForm";
	}

	/**
	 * 保存心跳
	 */
	@RequiresPermissions(value={"device:washDeviceHeart:add","device:washDeviceHeart:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashDeviceHeart washDeviceHeart, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washDeviceHeart)){
			return form(washDeviceHeart, model);
		}
		if(!washDeviceHeart.getIsNewRecord()){//编辑表单保存
			WashDeviceHeart t = washDeviceHeartService.get(washDeviceHeart.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washDeviceHeart, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washDeviceHeartService.save(t);//保存
		}else{//新增表单保存
			washDeviceHeartService.save(washDeviceHeart);//保存
		}
		addMessage(redirectAttributes, "保存心跳成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
	}
	
	/**
	 * 删除心跳
	 */
	@RequiresPermissions("device:washDeviceHeart:del")
	@RequestMapping(value = "delete")
	public String delete(WashDeviceHeart washDeviceHeart, RedirectAttributes redirectAttributes) {
		washDeviceHeartService.delete(washDeviceHeart);
		addMessage(redirectAttributes, "删除心跳成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
	}
	
	/**
	 * 批量删除心跳
	 */
	@RequiresPermissions("device:washDeviceHeart:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washDeviceHeartService.delete(washDeviceHeartService.get(id));
		}
		addMessage(redirectAttributes, "删除心跳成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("device:washDeviceHeart:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashDeviceHeart washDeviceHeart, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "心跳"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashDeviceHeart> page = washDeviceHeartService.findPage(new Page<WashDeviceHeart>(request, response, -1), washDeviceHeart);
    		new ExportExcel("心跳", WashDeviceHeart.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出心跳记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:washDeviceHeart:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashDeviceHeart> list = ei.getDataList(WashDeviceHeart.class);
			for (WashDeviceHeart washDeviceHeart : list){
				try{
					washDeviceHeartService.save(washDeviceHeart);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条心跳记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条心跳记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入心跳失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
    }
	
	/**
	 * 下载导入心跳数据模板
	 */
	@RequiresPermissions("device:washDeviceHeart:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "心跳数据导入模板.xlsx";
    		List<WashDeviceHeart> list = Lists.newArrayList(); 
    		new ExportExcel("心跳数据", WashDeviceHeart.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceHeart/?repage";
    }
	
	
	

}