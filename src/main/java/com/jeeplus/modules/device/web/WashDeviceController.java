/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
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
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.ZipFile;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.device.entity.WashDevice;
import com.jeeplus.modules.device.service.WashDeviceService;
import com.jeeplus.modules.tools.utils.TwoDimensionCode;

/**
 * 设备Controller
 * @author LD
 * @version 2017-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/device/washDevice")
public class WashDeviceController extends BaseController {
	
	@Autowired
	private WashDeviceService washDeviceService;
	
	@ModelAttribute
	public WashDevice get(@RequestParam(required=false) String id) {
		WashDevice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = washDeviceService.get(id);
		}
		if (entity == null){
			entity = new WashDevice();
		}
		return entity;
	}
	
	/**
	 * 设备列表页面
	 */
	@RequiresPermissions("device:washDevice:list")
	@RequestMapping(value = {"list", ""})
	public String list(WashDevice washDevice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WashDevice> page = washDeviceService.findPage(new Page<WashDevice>(request, response), washDevice); 
		model.addAttribute("page", page);
		return "modules/device/washDeviceList";
	}

	/**
	 * 查看，增加，编辑设备表单页面
	 */
	@RequiresPermissions(value={"device:washDevice:view","device:washDevice:add","device:washDevice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WashDevice washDevice, Model model) {
		model.addAttribute("washDevice", washDevice);
		return "modules/device/washDeviceForm";
	}

	/**
	 * 保存设备
	 */
	@RequiresPermissions(value={"device:washDevice:add","device:washDevice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WashDevice washDevice, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception{
		if (!beanValidator(model, washDevice)){
			return form(washDevice, model);
		}
		
		//生成二维码
		String realPath = request.getSession().getServletContext().getRealPath("/") + File.separator + "qrcode" + File.separator;
		FileUtils.createDirectory(realPath);
		String name = washDevice.getMac() + ".png"; //encoderImgId此处二维码的图片名
		String filePath = realPath + name;  //存放路径
		
		StringBuffer url = request.getRequestURL();  
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();  
		String content = tempContextUrl + Global.getConfig("qrcode.url") + washDevice.getMac();
		TwoDimensionCode.encoderQRCode(content , filePath, "png");//执行生成二维码
		
		if(!washDevice.getIsNewRecord()){//编辑表单保存
			WashDevice t = washDeviceService.get(washDevice.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(washDevice, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			washDeviceService.save(t);//保存
		}else{//新增表单保存
			washDeviceService.save(washDevice);//保存
		}
		addMessage(redirectAttributes, "保存设备成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
	}
	
	/**
	 * 删除设备
	 */
	@RequiresPermissions("device:washDevice:del")
	@RequestMapping(value = "delete")
	public String delete(WashDevice washDevice, RedirectAttributes redirectAttributes) {
		washDeviceService.delete(washDevice);
		addMessage(redirectAttributes, "删除设备成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
	}
	
	/**
	 * 批量删除设备
	 */
	@RequiresPermissions("device:washDevice:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			washDeviceService.delete(washDeviceService.get(id));
		}
		addMessage(redirectAttributes, "删除设备成功");
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("device:washDevice:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WashDevice washDevice, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WashDevice> page = washDeviceService.findPage(new Page<WashDevice>(request, response, -1), washDevice);
    		new ExportExcel("设备", WashDevice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出设备记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:washDevice:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WashDevice> list = ei.getDataList(WashDevice.class);
			for (WashDevice washDevice : list){
				try{
					washDeviceService.save(washDevice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
    }
	
	/**
	 * 下载导入设备数据模板
	 */
	@RequiresPermissions("device:washDevice:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备数据导入模板.xlsx";
    		List<WashDevice> list = Lists.newArrayList(); 
    		new ExportExcel("设备数据", WashDevice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/washDevice/?repage";
    }
	
	@ResponseBody
	@RequestMapping(value = "checkMac")
	public String checkMac(String mac, String oldMac, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		WashDevice u=new WashDevice();
		u.setMac(mac);
		boolean flag = false;
		if (mac !=null && mac.equals(oldMac)) {
			flag = false;
		} else if (mac !=null ) {
			List<WashDevice> list = washDeviceService.findListCheck(u);
			if(list != null && list.size() > 0 ){
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
	
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String name, String oldName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		WashDevice u=new WashDevice();
		u.setMac(name);
		boolean flag = false;
		if (name !=null && name.equals(oldName)) {
			flag = false;
		} else if (name !=null ) {
			List<WashDevice> list = washDeviceService.findListCheck(u);
			if(list != null && list.size() > 0 ){
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
	
	@RequestMapping(value = {"zip"})
	public String zip(WashDevice washDevice, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<WashDevice> list = washDeviceService.findList(washDevice); 
		ZipFile.exportZip(list, request, response);
		return null;
	}

	@RequestMapping(value = {"img"})
	public String img(String mac, HttpServletRequest request, HttpServletResponse response, Model model) {
		String strDirPath = request.getSession().getServletContext().getRealPath("/");
		//设置浏览器显示的内容类型为Zip  (很重要,欺骗浏览器下载的是zip文件,就不会自己打开了)  
	    response.setContentType("application/zip");  
	    //设置内容作为附件下载  fileName有后缀,比如1.jpg  
	    String filename = mac + ".png";
	    response.setHeader("Content-Disposition", "attachment; filename="+filename);  
	    ServletOutputStream out = null;  
	    try {  
	        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
	        //InputStream inputStream = FileManageUtils.downLoadFile(paths);//此处是为了获得输出流  
	        FileInputStream inputStream = new FileInputStream(strDirPath + File.separator + "qrcode" + File.separator + mac + ".png"); 
	        // 3.通过response获取ServletOutputStream对象(out)  
	        out = response.getOutputStream();  
	        int b = 0;  
	        byte[] buffer = new byte[1024];  
	        while (b != -1) {  
	            b = inputStream.read(buffer);  
	            // 4.写到输出流(out)中  
	            out.write(buffer, 0, b);  
	        }  
	        inputStream.close();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (out != null)  
	            out.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        try {  
	            if (out != null)  
	            out.flush();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }
	    return null;
	}
}