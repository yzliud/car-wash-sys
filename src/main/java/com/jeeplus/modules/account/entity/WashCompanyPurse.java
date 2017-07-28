/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity;

import com.jeeplus.modules.member.entity.WashMember;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 公司账户Entity
 * @author LD
 * @version 2017-05-25
 */
public class WashCompanyPurse extends DataEntity<WashCompanyPurse> {
	
	private static final long serialVersionUID = 1L;
	private WashMember member;		// 会员
	private String tflowNo;		// 流水号
	private String ttype;		// 交易类型（00 正常10 冻结90 异常）
	private Date tdatetime;		// 交易时间
	private String comment;		// 描述
	private Double income;		// 收入
	private Double pay;		// 支出
	private Double balance;		// 余额
	
	public WashCompanyPurse() {
		super();
	}

	public WashCompanyPurse(String id){
		super(id);
	}

	@ExcelField(title="会员", align=2, sort=1)
	public WashMember getMember() {
		return member;
	}

	public void setMember(WashMember member) {
		this.member = member;
	}
	
	@ExcelField(title="流水号", align=2, sort=2)
	public String getTflowNo() {
		return tflowNo;
	}

	public void setTflowNo(String tflowNo) {
		this.tflowNo = tflowNo;
	}
	
	@ExcelField(title="交易类型", dictType="flow_type", align=2, sort=3, fieldType=WashMember.class,value="member.mobile")
	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="交易时间不能为空")
	@ExcelField(title="交易时间", align=2, sort=4)
	public Date getTdatetime() {
		return tdatetime;
	}

	public void setTdatetime(Date tdatetime) {
		this.tdatetime = tdatetime;
	}
	
	@ExcelField(title="描述", align=2, sort=5)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ExcelField(title="收入", align=2, sort=6)
	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}
	
	@ExcelField(title="支出", align=2, sort=7)
	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}
	
	@ExcelField(title="余额", align=2, sort=8)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}