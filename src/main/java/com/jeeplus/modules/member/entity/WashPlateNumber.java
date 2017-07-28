/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 内部车牌Entity
 * @author 菜鸟
 * @version 2017-06-15
 */
public class WashPlateNumber extends DataEntity<WashPlateNumber> {
	
	private static final long serialVersionUID = 1L;
	private String carNumber;		// 车牌号
	private Double price;		// 内部费用
	
	public WashPlateNumber() {
		super();
	}

	public WashPlateNumber(String id){
		super(id);
	}

	@ExcelField(title="车牌号", align=2, sort=1)
	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	
	@ExcelField(title="内部费用", align=2, sort=2)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}