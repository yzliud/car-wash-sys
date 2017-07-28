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
import com.jeeplus.modules.device.entity.WashDeviceFault;
import com.jeeplus.modules.device.service.WashDeviceFaultService;

/**
 * 设备故障Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/device/washDeviceFault")
public class WashDeviceFaultController extends BaseController {

	@Autowired
	private WashDeviceFaultService washDeviceFaultService;
	
	@ModelAttribute
	public WashDeviceFault get(@RequestParam(required=false) String id) {
		WashDeviceFault entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washDeviceFaultService.get(id);
		}
		if (entity == null){
			entity = new WashDeviceFault();
		}
		return entity;
	}
	
	/**
	 * 设备故障列表页面
	 */
	@RequiresPermissions("device:washDeviceFault:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashDeviceFault washDeviceFault, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDeviceFault> page = washDeviceFaultService.findPage(new Page<WashDeviceFault>(request, response), washDeviceFault); 
		model.addAttribute("page", page);
		return "modules/device/washDeviceFaultList";
	}

	/**
	 * 查看，增加，编辑设备故障表单页面
	 */
	@RequiresPermissions(value={"device:washDeviceFault:view","device:washDeviceFault:add","device:washDeviceFault:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashDeviceFault washDeviceFault, Model model) {
		model.addAttribute("washDeviceFault", washDeviceFault);
		return "modules/device/washDeviceFaultForm";
	}

	/**
	 * 保存设备故障
	 */
	@RequiresPermissions(value={"device:washDeviceFault:add","device:washDeviceFault:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashDeviceFault washDeviceFault, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washDeviceFault)){
			return form(washDeviceFault, model);
		}
		if(!washDeviceFault.getIsNewRecord()){//编辑表单保存
			WashDeviceFault t = washDeviceFaultService.get(washDeviceFault.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washDeviceFault, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washDeviceFaultService.save(t);//保存
		}else{//新增表单保存
			washDeviceFaultService.save(washDeviceFault);//保存
		}
		addMessage(redirectAttributes, "保存设备故障成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
	}
	
	/**
	 * 删除设备故障
	 */
	@RequiresPermissions("device:washDeviceFault:del")
	@RequestMapping(value = "delete")
	public String delete(WashDeviceFault washDeviceFault, RedirectAttributes redirectAttributes) {
		washDeviceFaultService.delete(washDeviceFault);
		addMessage(redirectAttributes, "删除设备故障成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
	}
	
	/**
	 * 批量删除设备故障
	 */
	@RequiresPermissions("device:washDeviceFault:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washDeviceFaultService.delete(washDeviceFaultService.get(id));
		}
		addMessage(redirectAttributes, "删除设备故障成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("device:washDeviceFault:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashDeviceFault washDeviceFault, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备故障"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashDeviceFault> page = washDeviceFaultService.findPage(new Page<WashDeviceFault>(request, response, -1), washDeviceFault);
    		new ExportExcel("设备故障", WashDeviceFault.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备故障记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:washDeviceFault:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashDeviceFault> list = ei.getDataList(WashDeviceFault.class);
			for (WashDeviceFault washDeviceFault : list){
				try{
					washDeviceFaultService.save(washDeviceFault);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备故障记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备故障记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备故障失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
    }
	
	/**
	 * 下载导入设备故障数据模板
	 */
	@RequiresPermissions("device:washDeviceFault:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备故障数据导入模板.xlsx";
    		List<WashDeviceFault> list = Lists.newArrayList(); 
    		new ExportExcel("设备故障数据", WashDeviceFault.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceFault/?repage";
    }
	
	
	

}