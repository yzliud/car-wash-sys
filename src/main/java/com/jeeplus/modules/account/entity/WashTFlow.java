/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.account.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.member.entity.WashMember;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 交易流水Entity
 * @author LD
 * @version 2017-05-19
 */
public class WashTFlow extends DataEntity<WashTFlow> {
	
	private static final long serialVersionUID = 1L;
	private String tsn;		// 流水号
	private Date tdatetime;		// 交易时间
	private String ttype;		// 交易状态00 正常            10 冻结            90 异常
	private WashMember member;		// 会员
	private String tppType;		// 类型 0-微信 1-支付宝
	private String comment;		// 备注
	private Double tamount;		// 交易金额
	private String tppSn;		// 第三方流水号
	private String tppResult;		// 第三方结果
	private Date beginTDatetime;		// 开始 交易时间
	private Date endTDatetime;		// 结束 交易时间
	
	public WashTFlow() {
		super();
	}

	public WashTFlow(String id){
		super(id);
	}

	@ExcelField(title="流水号", align=2, sort=1)
	public String getTsn() {
		return tsn;
	}

	public void setTsn(String tsn) {
		this.tsn = tsn;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="交易时间不能为空")
	@ExcelField(title="交易时间", align=2, sort=2)
	public Date getTdatetime() {
		return tdatetime;
	}

	public void setTdatetime(Date tdatetime) {
		this.tdatetime = tdatetime;
	}
	
	@ExcelField(title="交易状态", dictType="flow_type", align=2, sort=3)
	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	
	@ExcelField(title="会员", align=2, sort=4, fieldType=WashMember.class,value="member.mobile")
	public WashMember getMember() {
		return member;
	}

	public void setMember(WashMember member) {
		this.member = member;
	}
	
	@ExcelField(title="类型", align=2, sort=5)
	public String getTppType() {
		return tppType;
	}

	public void setTppType(String tppType) {
		this.tppType = tppType;
	}
	
	@ExcelField(title="备注", align=2, sort=6)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ExcelField(title="交易金额", align=2, sort=7)
	public Double getTamount() {
		return tamount;
	}

	public void setTamount(Double tamount) {
		this.tamount = tamount;
	}
	
	@ExcelField(title="第三方流水号", align=2, sort=8)
	public String getTppSn() {
		return tppSn;
	}

	public void setTppSn(String tppSn) {
		this.tppSn = tppSn;
	}
	
	@ExcelField(title="第三方结果", align=2, sort=9)
	public String getTppResult() {
		return tppResult;
	}

	public void setTppResult(String tppResult) {
		this.tppResult = tppResult;
	}
	
	public Date getBeginTDatetime() {
		return beginTDatetime;
	}

	public void setBeginTDatetime(Date beginTDatetime) {
		this.beginTDatetime = beginTDatetime;
	}
	
	public Date getEndTDatetime() {
		return endTDatetime;
	}

	public void setEndTDatetime(Date endTDatetime) {
		this.endTDatetime = endTDatetime;
	}
		
}