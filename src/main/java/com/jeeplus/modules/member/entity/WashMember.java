/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 会员Entity
 * @author LD
 * @version 2017-05-19
 */
public class WashMember extends DataEntity<WashMember> {
	
	private static final long serialVersionUID = 1L;
	private String openId;		// 微信标识
	private String nickName;		// 昵称
	private String mobile;		// 手机号
	private String cardNum;		// 身份证
	private String name;		// 姓名
	private String status;		// 用户状态 0-正常 1-黑名单
	private String userType;		// 用户类型(0-车主 1-洗车工)
	private String carModel;		// 车辆型号
	private String carNumber;		// 车牌号
	private String sex;		// 性别
	private String country;		// 国家
	private String province;		// 省
	private String city;		// 市
	private String img;		// 头像
	private Date lastTime;		// 最后登陆时间
	
	public WashMember() {
		super();
	}

	public WashMember(String id){
		super(id);
	}

	@ExcelField(title="微信标识", align=2, sort=1)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@ExcelField(title="昵称", align=2, sort=2)
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@ExcelField(title="手机号", align=2, sort=3)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	//@ExcelField(title="身份证", align=2, sort=4)
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	//@ExcelField(title="姓名", align=2, sort=5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="用户状态 ", dictType="member_status", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="用户类型", dictType="user_type", align=2, sort=5)
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@ExcelField(title="车辆型号", align=2, sort=6)
	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	
	@ExcelField(title="车牌号", align=2, sort=7)
	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=8)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="国家", align=2, sort=9)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@ExcelField(title="省", align=2, sort=10)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="市", align=2, sort=11)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="头像", align=2, sort=12)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登陆时间", align=2, sort=13)
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	
}