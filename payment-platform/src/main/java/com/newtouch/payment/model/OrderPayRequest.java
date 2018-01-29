package com.newtouch.payment.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPayRequest  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 414787744450651891L;

	/**
	 * 
	 */
	private Long id;

	/**
	 * 订单金额
	 */
	private BigDecimal amount;

	/**
	 * 支付订单号
	 */
	private String orderNo;

	/**
	 * 请求支付时间
	 */
	private Date creatTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 流水状态
	 */
	private String payRequestStatus;

	/**
	 * 商户号
	 */
	private String busNum;
	/**
	 * 支付流水号
	 */
	private String paySeriNo;

	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户证件类型
	 */
	private String userCerType;
	/**
	 * 用户证件号码
	 */
	private String userCerNum;
	/**
	 * 用户银行帐号
	 */
	private String userAccount;
	/**
	 * 用户手机
	 */
	private String userMobile;
	
	/**
	 * 短信验证码
	 */
	private String smsCode;
	/**
	 * 扩展字段
	 */
	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPayRequestStatus() {
		return payRequestStatus;
	}

	public void setPayRequestStatus(String payRequestStatus) {
		this.payRequestStatus = payRequestStatus;
	}

	public String getBusNum() {
		return busNum;
	}

	public void setBusNum(String busNum) {
		this.busNum = busNum;
	}

	public String getPaySeriNo() {
		return paySeriNo;
	}

	public void setPaySeriNo(String paySeriNo) {
		this.paySeriNo = paySeriNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCerType() {
		return userCerType;
	}

	public void setUserCerType(String userCerType) {
		this.userCerType = userCerType;
	}

	public String getUserCerNum() {
		return userCerNum;
	}

	public void setUserCerNum(String userCerNum) {
		this.userCerNum = userCerNum;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	
}