/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.web;

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

import com.jeeplus.modules.device.entity.WashDevice;
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
import com.jeeplus.modules.device.entity.WashDeviceRecord;
import com.jeeplus.modules.device.service.WashDeviceRecordService;

/**
 * 设备使用记录Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/device/washDeviceRecord")
public class WashDeviceRecordController extends BaseController {

	@Autowired
	private WashDeviceRecordService washDeviceRecordService;
	
	@ModelAttribute
	public WashDeviceRecord get(@RequestParam(required=false) String id) {
		WashDeviceRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washDeviceRecordService.get(id);
		}
		if (entity == null){
			entity = new WashDeviceRecord();
		}
		return entity;
	}
	
	/**
	 * 设备使用记录列表页面
	 */
	@RequiresPermissions("device:washDeviceRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashDeviceRecord washDeviceRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDeviceRecord> page = washDeviceRecordService.findPage(new Page<WashDeviceRecord>(request, response), washDeviceRecord); 
		model.addAttribute("page", page);
		return "modules/device/washDeviceRecordList";
	}

	/**
	 * 查看，增加，编辑设备使用记录表单页面
	 */
	@RequiresPermissions(value={"device:washDeviceRecord:view","device:washDeviceRecord:add","device:washDeviceRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashDeviceRecord washDeviceRecord, Model model) {
		model.addAttribute("washDeviceRecord", washDeviceRecord);
		return "modules/device/washDeviceRecordForm";
	}

	/**
	 * 保存设备使用记录
	 */
	@RequiresPermissions(value={"device:washDeviceRecord:add","device:washDeviceRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashDeviceRecord washDeviceRecord, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washDeviceRecord)){
			return form(washDeviceRecord, model);
		}
		if(!washDeviceRecord.getIsNewRecord()){//编辑表单保存
			WashDeviceRecord t = washDeviceRecordService.get(washDeviceRecord.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washDeviceRecord, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washDeviceRecordService.save(t);//保存
		}else{//新增表单保存
			washDeviceRecordService.save(washDeviceRecord);//保存
		}
		addMessage(redirectAttributes, "保存设备使用记录成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
	}
	
	/**
	 * 删除设备使用记录
	 */
	@RequiresPermissions("device:washDeviceRecord:del")
	@RequestMapping(value = "delete")
	public String delete(WashDeviceRecord washDeviceRecord, RedirectAttributes redirectAttributes) {
		washDeviceRecordService.delete(washDeviceRecord);
		addMessage(redirectAttributes, "删除设备使用记录成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
	}
	
	/**
	 * 批量删除设备使用记录
	 */
	@RequiresPermissions("device:washDeviceRecord:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washDeviceRecordService.delete(washDeviceRecordService.get(id));
		}
		addMessage(redirectAttributes, "删除设备使用记录成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("device:washDeviceRecord:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashDeviceRecord washDeviceRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备使用记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashDeviceRecord> page = washDeviceRecordService.findPage(new Page<WashDeviceRecord>(request, response, -1), washDeviceRecord);
    		new ExportExcel("设备使用记录", WashDeviceRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备使用记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:washDeviceRecord:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashDeviceRecord> list = ei.getDataList(WashDeviceRecord.class);
			for (WashDeviceRecord washDeviceRecord : list){
				try{
					washDeviceRecordService.save(washDeviceRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备使用记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备使用记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备使用记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
    }
	
	/**
	 * 下载导入设备使用记录数据模板
	 */
	@RequiresPermissions("device:washDeviceRecord:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备使用记录数据导入模板.xlsx";
    		List<WashDeviceRecord> list = Lists.newArrayList(); 
    		new ExportExcel("设备使用记录数据", WashDeviceRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDeviceRecord/?repage";
    }
	
	
	/**
	 * 选择设备
	 */
	@RequestMapping(value = "selectwashDevice")
	public String selectwashDevice(WashDevice washDevice, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDevice> page = washDeviceRecordService.findPageBywashDevice(new Page<WashDevice>(request, response),  washDevice);
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
		model.addAttribute("obj", washDevice);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择洗车工
	 */
	@RequestMapping(value = "selectwashMember")
	public String selectwashMember(WashMember washMember, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washDeviceRecordService.findPageBywashMember(new Page<WashMember>(request, response),  washMember);
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
		model.addAttribute("obj", washMember);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}