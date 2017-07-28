/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coupon.web;

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
import com.jeeplus.modules.coupon.CouponUuid;
import com.jeeplus.modules.coupon.entity.WashCoupon;
import com.jeeplus.modules.coupon.entity.WashCouponDetail;
import com.jeeplus.modules.coupon.service.WashCouponDetailService;
import com.jeeplus.modules.coupon.service.WashCouponService;

/**
 * 优惠卷Controller
 * @author LD
 * @version 2017-05-24
 */
@Controller
@RequestMapping(value = "${adminPath}/coupon/washCoupon")
public class WashCouponController extends BaseController {

	@Autowired
	private WashCouponService washCouponService;
	
	@Autowired
	private WashCouponDetailService washCouponDetailService;
	
	@ModelAttribute
	public WashCoupon get(@RequestParam(required=false) String id) {
		WashCoupon entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washCouponService.get(id);
		}
		if (entity == null){
			entity = new WashCoupon();
		}
		return entity;
	}
	
	/**
	 * 优惠卷列表页面
	 */
	@RequiresPermissions("coupon:washCoupon:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashCoupon washCoupon, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashCoupon> page = washCouponService.findPage(new Page<WashCoupon>(request, response), washCoupon); 
		model.addAttribute("page", page);
		return "modules/coupon/washCouponList";
	}

	/**
	 * 查看，增加，编辑优惠卷表单页面
	 */
	@RequiresPermissions(value={"coupon:washCoupon:view","coupon:washCoupon:add","coupon:washCoupon:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashCoupon washCoupon, Model model) {
		model.addAttribute("washCoupon", washCoupon);
		return "modules/coupon/washCouponForm";
	}

	/**
	 * 保存优惠卷
	 */
	@RequiresPermissions(value={"coupon:washCoupon:add","coupon:washCoupon:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashCoupon washCoupon, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, washCoupon)){
			return form(washCoupon, model);
		}
		if(!washCoupon.getIsNewRecord()){//编辑表单保存
			WashCoupon t = washCouponService.get(washCoupon.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washCoupon, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washCouponService.save(t);//保存
		}else{//新增表单保存
			washCoupon.setStatus("0");
			washCouponService.save(washCoupon);//保存
		}
		addMessage(redirectAttributes, "保存优惠卷成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
	}
	
	/**
	 * 删除优惠卷
	 */
	@RequiresPermissions("coupon:washCoupon:del")
	@RequestMapping(value = "delete")
	public String delete(WashCoupon washCoupon, RedirectAttributes redirectAttributes) {
		washCouponService.delete(washCoupon);
		addMessage(redirectAttributes, "删除优惠卷成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
	}
	
	/**
	 * 批量删除优惠卷
	 */
	@RequiresPermissions("coupon:washCoupon:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washCouponService.delete(washCouponService.get(id));
		}
		addMessage(redirectAttributes, "删除优惠卷成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("coupon:washCoupon:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashCoupon washCoupon, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "优惠卷"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashCoupon> page = washCouponService.findPage(new Page<WashCoupon>(request, response, -1), washCoupon);
    		new ExportExcel("优惠卷", WashCoupon.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出优惠卷记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("coupon:washCoupon:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashCoupon> list = ei.getDataList(WashCoupon.class);
			for (WashCoupon washCoupon : list){
				try{
					washCouponService.save(washCoupon);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条优惠卷记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条优惠卷记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入优惠卷失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
    }
	
	/**
	 * 下载导入优惠卷数据模板
	 */
	@RequiresPermissions("coupon:washCoupon:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "优惠卷数据导入模板.xlsx";
    		List<WashCoupon> list = Lists.newArrayList(); 
    		new ExportExcel("优惠卷数据", WashCoupon.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
    }
	
	/**
	 * 批量删除优惠卷
	 */
	@RequiresPermissions("coupon:washCoupon:release")
	@RequestMapping(value = "release")
	public String release(String id, RedirectAttributes redirectAttributes) {
		
		WashCoupon washCoupon = washCouponService.get(id);
		washCoupon.setStatus("1");
		washCouponService.save(washCoupon);
		boolean pass = true;
		WashCouponDetail washCouponDetail = null;
		for(int i=0;i<washCoupon.getQuantity();i++){
			while(pass){
				washCouponDetail = new WashCouponDetail();
				String couponNo = CouponUuid.getCouponNo(19).toUpperCase();
				washCouponDetail.setCouponNo(couponNo);
				List<WashCouponDetail> list = washCouponDetailService.findList(washCouponDetail);
				if(list != null && list.size()>0){
					pass = true;
				}else{					
					washCouponDetail.setCouponName(washCoupon.getName());
					washCouponDetail.setCouponId(washCoupon.getId());
					washCouponDetail.setDiscountAmount(washCoupon.getDiscountAmount());
					washCouponDetail.setEffectiveTime(washCoupon.getEffectiveTime());
					washCouponDetail.setFailureTime(washCoupon.getFailureTime());
					washCouponDetail.setFlag(washCoupon.getFlag());
					washCouponDetail.setRemarks(washCoupon.getRemarks());
					washCouponDetail.setTotalAmount(washCoupon.getTotalAmount());
					washCouponDetail.setStatus("0");
					washCouponDetailService.save(washCouponDetail);
					pass = false;
				}
			}
			pass = true;
		}
		addMessage(redirectAttributes, "生成优惠卷成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/washCoupon/?repage";
	}
	

}