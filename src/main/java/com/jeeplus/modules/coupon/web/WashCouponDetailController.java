/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.web;

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
import com.jeeplus.modules.coupon.entity.WashCouponDetail;
import com.jeeplus.modules.coupon.service.WashCouponDetailService;

/**
 * 优惠卷明细Controller
 * @author LD
 * @version 2017-05-24
 */
@Controller
@RequestMapping(value = "${adminPath}/coupon/washCouponDetail")
public class WashCouponDetailController extends BaseController {

	@Autowired
	private WashCouponDetailService washCouponDetailService;
	
	@ModelAttribute
	public WashCouponDetail get(@RequestParam(required=false) String id) {
		WashCouponDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washCouponDetailService.get(id);
		}
		if (entity == null){
			entity = new WashCouponDetail();
		}
		return entity;
	}
	
	/**
	 * 优惠卷明细列表页面
	 */
	@RequiresPermissions("coupon:washCouponDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashCouponDetail washCouponDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashCouponDetail> page = washCouponDetailService.findPage(new Page<WashCouponDetail>(request, response), washCouponDetail); 
		model.addAttribute("page", page);
		return "modules/coupon/washCouponDetailList";
	}

	/**
	 * 查看，增加，编辑优惠卷明细表单页面
	 */
	@RequiresPermissions(value={"coupon:washCouponDetail:view","coupon:washCouponDetail:add","coupon:washCouponDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashCouponDetail washCouponDetail, Model model) {
		model.addAttribute("washCouponDetail", washCouponDetail);
		return "modules/coupon/washCouponDetailForm";
	}

	/**
	 * 保存优惠卷明细
	 */
	@RequiresPermissions(value={"coupon:washCouponDetail:add","coupon:washCouponDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashCouponDetail washCouponDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washCouponDetail)){
			return form(washCouponDetail, model);
		}
		if(!washCouponDetail.getIsNewRecord()){//编辑表单保存
			WashCouponDetail t = washCouponDetailService.get(washCouponDetail.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washCouponDetail, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washCouponDetailService.save(t);//保存
		}else{//新增表单保存
			washCouponDetailService.save(washCouponDetail);//保存
		}
		addMessage(redirectAttributes, "保存优惠卷明细成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
	}
	
	/**
	 * 删除优惠卷明细
	 */
	@RequiresPermissions("coupon:washCouponDetail:del")
	@RequestMapping(value = "delete")
	public String delete(WashCouponDetail washCouponDetail, RedirectAttributes redirectAttributes) {
		washCouponDetailService.delete(washCouponDetail);
		addMessage(redirectAttributes, "删除优惠卷明细成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
	}
	
	/**
	 * 批量删除优惠卷明细
	 */
	@RequiresPermissions("coupon:washCouponDetail:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washCouponDetailService.delete(washCouponDetailService.get(id));
		}
		addMessage(redirectAttributes, "删除优惠卷明细成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("coupon:washCouponDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashCouponDetail washCouponDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "优惠卷明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashCouponDetail> page = washCouponDetailService.findPage(new Page<WashCouponDetail>(request, response, -1), washCouponDetail);
    		new ExportExcel("优惠卷明细", WashCouponDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出优惠卷明细记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("coupon:washCouponDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashCouponDetail> list = ei.getDataList(WashCouponDetail.class);
			for (WashCouponDetail washCouponDetail : list){
				try{
					washCouponDetailService.save(washCouponDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条优惠卷明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条优惠卷明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入优惠卷明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
    }
	
	/**
	 * 下载导入优惠卷明细数据模板
	 */
	@RequiresPermissions("coupon:washCouponDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "优惠卷明细数据导入模板.xlsx";
    		List<WashCouponDetail> list = Lists.newArrayList(); 
    		new ExportExcel("优惠卷明细数据", WashCouponDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCouponDetail/?repage";
    }
	
	
	/**
	 * 选择会员ID
	 */
	@RequestMapping(value = "selectwashMember")
	public String selectwashMember(WashMember washMember, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashMember> page = washCouponDetailService.findPageBywashMember(new Page<WashMember>(request, response),  washMember);
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