/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单评价Entity
 * @author LD
 * @version 2017-05-18
 */
public class WashOrdEvaluate extends DataEntity<WashOrdEvaluate> {
	
	private static final long serialVersionUID = 1L;
	private String grade;		// 评价等级(1,2,3,4,5)
	private String flag;		// 标识(0-好评 1-中评 2-差评)
	private String evaluate;		// 评价
	private String addEvaluate;		// 追加评价
	private String status;		// 状态（0-1次评价 1-2次评价）
	private String orderNo;		// 
	
	@ExcelField(title="订单号", align=2, sort=1)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public WashOrdEvaluate() {
		super();
	}

	public WashOrdEvaluate(String id){
		super(id);
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@ExcelField(title="标识", dictType="evaluate_flag", align=2, sort=2)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@ExcelField(title="评价", align=2, sort=3)
	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
	@ExcelField(title="追加评价", align=2, sort=4)
	public String getAddEvaluate() {
		return addEvaluate;
	}

	public void setAddEvaluate(String addEvaluate) {
		this.addEvaluate = addEvaluate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}