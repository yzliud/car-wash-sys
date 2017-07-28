/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.meal.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashSetMeal extends DataEntity<WashSetMeal> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 套餐名称
	private String summary;		// 套餐描述
	private Double marketPrice;		// 市场价
	private Double salePrice;		// 销售价
	private String duration;		// 时长
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	
	public WashSetMeal() {
		super();
	}

	public WashSetMeal(String id){
		super(id);
	}

	@ExcelField(title="套餐名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="套餐描述", align=2, sort=2)
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@NotNull(message="市场价不能为空")
	@ExcelField(title="市场价", align=2, sort=3)
	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	@NotNull(message="销售价不能为空")
	@ExcelField(title="销售价", align=2, sort=4)
	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	
	@ExcelField(title="时长", align=2, sort=5)
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="create_time", align=2, sort=7)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="update_time", align=2, sort=9)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}