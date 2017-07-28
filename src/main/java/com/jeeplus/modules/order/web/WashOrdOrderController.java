/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web;

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
import com.jeeplus.modules.order.entity.WashOrdOrder;
import com.jeeplus.modules.order.service.WashOrdOrderService;

/**
 * 订单Controller
 * @author LD
 * @version 2017-05-19
 */
@Controller
@RequestMapping(value = "${adminPath}/order/washOrdOrder")
public class WashOrdOrderController extends BaseController {

	@Autowired
	private WashOrdOrderService washOrdOrderService;
	
	@ModelAttribute
	public WashOrdOrder get(@RequestParam(required=false) String id) {
		WashOrdOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washOrdOrderService.get(id);
		}
		if (entity == null){
			entity = new WashOrdOrder();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("order:washOrdOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashOrdOrder washOrdOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashOrdOrder> page = washOrdOrderService.findPage(new Page<WashOrdOrder>(request, response), washOrdOrder); 
		model.addAttribute("page", page);
		return "modules/order/washOrdOrderList";
	}

	/**
	 * 查看，增加，编辑订单表单页面
	 */
	@RequiresPermissions(value={"order:washOrdOrder:view","order:washOrdOrder:add","order:washOrdOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashOrdOrder washOrdOrder, Model model) {
		model.addAttribute("washOrdOrder", washOrdOrder);
		return "modules/order/washOrdOrderForm";
	}

	/**
	 * 保存订单
	 */
	@RequiresPermissions(value={"order:washOrdOrder:add","order:washOrdOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashOrdOrder washOrdOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washOrdOrder)){
			return form(washOrdOrder, model);
		}
		if(!washOrdOrder.getIsNewRecord()){//编辑表单保存
			WashOrdOrder t = washOrdOrderService.get(washOrdOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washOrdOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washOrdOrderService.save(t);//保存
		}else{//新增表单保存
			washOrdOrderService.save(washOrdOrder);//保存
		}
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
	}
	
	/**
	 * 删除订单
	 */
	@RequiresPermissions("order:washOrdOrder:del")
	@RequestMapping(value = "delete")
	public String delete(WashOrdOrder washOrdOrder, RedirectAttributes redirectAttributes) {
		washOrdOrderService.delete(washOrdOrder);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
	}
	
	/**
	 * 批量删除订单
	 */
	@RequiresPermissions("order:washOrdOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washOrdOrderService.delete(washOrdOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("order:washOrdOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashOrdOrder washOrdOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashOrdOrder> page = washOrdOrderService.findPage(new Page<WashOrdOrder>(request, response, -1), washOrdOrder);
    		new ExportExcel("订单", WashOrdOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("order:washOrdOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashOrdOrder> list = ei.getDataList(WashOrdOrder.class);
			for (WashOrdOrder washOrdOrder : list){
				try{
					washOrdOrderService.save(washOrdOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
    }
	
	/**
	 * 下载导入订单数据模板
	 */
	@RequiresPermissions("order:washOrdOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据导入模板.xlsx";
    		List<WashOrdOrder> list = Lists.newArrayList(); 
    		new ExportExcel("订单数据", WashOrdOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/order/washOrdOrder/?repage";
    }
	
	
	/**
	 * 选择车主
	 */
	@RequestMapping(value = "selectcarPerson")
	public String selectcarPerson(WashMember carPerson, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washOrdOrderService.findPageBycarPerson(new Page<WashMember>(request, response),  carPerson);
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
		model.addAttribute("obj", carPerson);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择洗车工
	 */
	@RequestMapping(value = "selectwashPerson")
	public String selectwashPerson(WashMember washPerson, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		washPerson.setUserType("1");
		Page<WashMember> page = washOrdOrderService.findPageBywashPerson(new Page<WashMember>(request, response),  washPerson);
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
		Page<WashDevice> page = washOrdOrderService.findPageBywashDevice(new Page<WashDevice>(request, response),  washDevice);
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