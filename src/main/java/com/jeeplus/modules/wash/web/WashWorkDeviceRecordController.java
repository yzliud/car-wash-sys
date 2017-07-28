/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.wash.web;

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
import com.jeeplus.modules.device.entity.WashDevice;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.wash.entity.WashWorkDeviceRecord;
import com.jeeplus.modules.wash.service.WashWorkDeviceRecordService;

/**
 * 工作记录Controller
 * @author LD
 * @version 2017-05-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wash/washWorkDeviceRecord")
public class WashWorkDeviceRecordController extends BaseController {

	@Autowired
	private WashWorkDeviceRecordService washWorkDeviceRecordService;
	
	@ModelAttribute
	public WashWorkDeviceRecord get(@RequestParam(required=false) String id) {
		WashWorkDeviceRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washWorkDeviceRecordService.get(id);
		}
		if (entity == null){
			entity = new WashWorkDeviceRecord();
		}
		return entity;
	}
	
	/**
	 * 工作记录列表页面
	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashWorkDeviceRecord washWorkDeviceRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashWorkDeviceRecord> page = washWorkDeviceRecordService.findPage(new Page<WashWorkDeviceRecord>(request, response), washWorkDeviceRecord); 
		model.addAttribute("page", page);
		return "modules/wash/washWorkDeviceRecordList";
	}

	/**
	 * 查看，增加，编辑工作记录表单页面
	 */
	@RequiresPermissions(value={"wash:washWorkDeviceRecord:view","wash:washWorkDeviceRecord:add","wash:washWorkDeviceRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashWorkDeviceRecord washWorkDeviceRecord, Model model) {
		model.addAttribute("washWorkDeviceRecord", washWorkDeviceRecord);
		return "modules/wash/washWorkDeviceRecordForm";
	}

	/**
	 * 保存工作记录
	 */
	@RequiresPermissions(value={"wash:washWorkDeviceRecord:add","wash:washWorkDeviceRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashWorkDeviceRecord washWorkDeviceRecord, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washWorkDeviceRecord)){
			return form(washWorkDeviceRecord, model);
		}
		if(!washWorkDeviceRecord.getIsNewRecord()){//编辑表单保存
			WashWorkDeviceRecord t = washWorkDeviceRecordService.get(washWorkDeviceRecord.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washWorkDeviceRecord, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washWorkDeviceRecordService.save(t);//保存
		}else{//新增表单保存
			washWorkDeviceRecordService.save(washWorkDeviceRecord);//保存
		}
		addMessage(redirectAttributes, "保存工作记录成功");
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
	}
	
	/**
	 * 删除工作记录
	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:del")
	@RequestMapping(value = "delete")
	public String delete(WashWorkDeviceRecord washWorkDeviceRecord, RedirectAttributes redirectAttributes) {
		washWorkDeviceRecordService.delete(washWorkDeviceRecord);
		addMessage(redirectAttributes, "删除工作记录成功");
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
	}
	
	/**
	 * 批量删除工作记录
	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washWorkDeviceRecordService.delete(washWorkDeviceRecordService.get(id));
		}
		addMessage(redirectAttributes, "删除工作记录成功");
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashWorkDeviceRecord washWorkDeviceRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashWorkDeviceRecord> page = washWorkDeviceRecordService.findPage(new Page<WashWorkDeviceRecord>(request, response, -1), washWorkDeviceRecord);
    		new ExportExcel("工作记录", WashWorkDeviceRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出工作记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashWorkDeviceRecord> list = ei.getDataList(WashWorkDeviceRecord.class);
			for (WashWorkDeviceRecord washWorkDeviceRecord : list){
				try{
					washWorkDeviceRecordService.save(washWorkDeviceRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工作记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工作记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
    }
	
	/**
	 * 下载导入工作记录数据模板
	 */
	@RequiresPermissions("wash:washWorkDeviceRecord:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工作记录数据导入模板.xlsx";
    		List<WashWorkDeviceRecord> list = Lists.newArrayList(); 
    		new ExportExcel("工作记录数据", WashWorkDeviceRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/wash/washWorkDeviceRecord/?repage";
    }
	
	
	/**
	 * 选择洗车工
	 */
	@RequestMapping(value = "selectwashPerson")
	public String selectwashPerson(WashMember washPerson, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washWorkDeviceRecordService.findPageBywashPerson(new Page<WashMember>(request, response),  washPerson);
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
		model.addAttribute("obj", washPerson);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择设备
	 */
	@RequestMapping(value = "selectwashDevice")
	public String selectwashDevice(WashDevice washDevice, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDevice> page = washWorkDeviceRecordService.findPageBywashDevice(new Page<WashDevice>(request, response),  washDevice);
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
	

}