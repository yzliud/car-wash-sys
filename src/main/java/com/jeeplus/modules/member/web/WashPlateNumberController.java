/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.jeeplus.modules.member.entity.WashPlateNumber;
import com.jeeplus.modules.member.service.WashPlateNumberService;

/**
 * 内部车牌Controller
 * @author 菜鸟
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/member/washPlateNumber")
public class WashPlateNumberController extends BaseController {

	@Autowired
	private WashPlateNumberService washPlateNumberService;
	
	@ModelAttribute
	public WashPlateNumber get(@RequestParam(required=false) String id) {
		WashPlateNumber entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washPlateNumberService.get(id);
		}
		if (entity == null){
			entity = new WashPlateNumber();
		}
		return entity;
	}
	
	/**
	 * 内部车牌列表页面
	 */
	@RequiresPermissions("member:washPlateNumber:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashPlateNumber washPlateNumber, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashPlateNumber> page = washPlateNumberService.findPage(new Page<WashPlateNumber>(request, response), washPlateNumber); 
		model.addAttribute("page", page);
		return "modules/member/washPlateNumberList";
	}

	/**
	 * 查看，增加，编辑内部车牌表单页面
	 */
	@RequiresPermissions(value={"member:washPlateNumber:view","member:washPlateNumber:add","member:washPlateNumber:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashPlateNumber washPlateNumber, Model model) {
		model.addAttribute("washPlateNumber", washPlateNumber);
		return "modules/member/washPlateNumberForm";
	}

	/**
	 * 保存内部车牌
	 */
	@RequiresPermissions(value={"member:washPlateNumber:add","member:washPlateNumber:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashPlateNumber washPlateNumber, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washPlateNumber)){
			return form(washPlateNumber, model);
		}
		if(!washPlateNumber.getIsNewRecord()){//编辑表单保存
			WashPlateNumber t = washPlateNumberService.get(washPlateNumber.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washPlateNumber, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washPlateNumberService.save(t);//保存
		}else{//新增表单保存
			washPlateNumberService.save(washPlateNumber);//保存
		}
		addMessage(redirectAttributes, "保存内部车牌成功");
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
	}
	
	/**
	 * 删除内部车牌
	 */
	@RequiresPermissions("member:washPlateNumber:del")
	@RequestMapping(value = "delete")
	public String delete(WashPlateNumber washPlateNumber, RedirectAttributes redirectAttributes) {
		washPlateNumberService.delete(washPlateNumber);
		addMessage(redirectAttributes, "删除内部车牌成功");
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
	}
	
	/**
	 * 批量删除内部车牌
	 */
	@RequiresPermissions("member:washPlateNumber:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washPlateNumberService.delete(washPlateNumberService.get(id));
		}
		addMessage(redirectAttributes, "删除内部车牌成功");
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("member:washPlateNumber:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashPlateNumber washPlateNumber, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "内部车牌"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashPlateNumber> page = washPlateNumberService.findPage(new Page<WashPlateNumber>(request, response, -1), washPlateNumber);
    		new ExportExcel("内部车牌", WashPlateNumber.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出内部车牌记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("member:washPlateNumber:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;

			String rtnMsg = "";
			String rowMsg = "";
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashPlateNumber> list = ei.getDataList(WashPlateNumber.class);
			int size = 2 ;
			for (WashPlateNumber washPlateNumber : list){
				size++;
				rowMsg = "";
				try{
					if(washPlateNumber == null || washPlateNumber.getCarNumber() == null || ("").equals(washPlateNumber.getCarNumber())){
						rowMsg = rowMsg + " 车牌";
					}
					if(washPlateNumber == null || washPlateNumber.getPrice() == null || ("").equals(washPlateNumber.getPrice())){
						rowMsg = rowMsg + " 价格";
					}
					if(rowMsg.equals("") ){
						washPlateNumberService.excelImport(washPlateNumber);
						successNum++;
					}else{
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
				if(!rowMsg.equals("")){
					rtnMsg = rtnMsg + "第"+size+"行"+rowMsg+" 数据有误";
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条内部车牌记录。"+rtnMsg);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条内部车牌记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入内部车牌失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
    }
	
	/**
	 * 下载导入内部车牌数据模板
	 */
	@RequiresPermissions("member:washPlateNumber:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "内部车牌数据导入模板.xlsx";
    		List<WashPlateNumber> list = Lists.newArrayList(); 
    		new ExportExcel("内部车牌数据", WashPlateNumber.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
    }
	
	
	/**
	 * 批量设置内部车牌
	 */
	@RequestMapping(value = "updateFee")
	public String updateFee(String fee, RedirectAttributes redirectAttributes) {
		WashPlateNumber washPlateNumber = new WashPlateNumber();
		washPlateNumber.setPrice(Double.valueOf(fee));
		washPlateNumber.preUpdate();
		washPlateNumberService.updateFee(washPlateNumber);

		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+Global.getAdminPath()+"/member/washPlateNumber/?repage";
	}
	
	@ResponseBody
	@RequestMapping(value = "checkCarNumber")
	public String checkName(String carNumber, String oldCarNumber, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		boolean flag = false;
		if (carNumber !=null && carNumber.equals(oldCarNumber)) {
			flag = false;
		} else if (carNumber !=null ) {
			WashPlateNumber washPlateNumber = washPlateNumberService.findUniqueByProperty("car_number", carNumber);
			if(null != washPlateNumber ){
				flag = true;
			}
		}
		String result = "";
		if(flag){
			result = "{\"rtnCode\":0}";
		}else{
			result = "{\"rtnCode\":500}";
		}
		
		response.reset();
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		response.getOutputStream().write(result.getBytes("utf-8"));
		response.getOutputStream().flush();

		return null;
	}

}