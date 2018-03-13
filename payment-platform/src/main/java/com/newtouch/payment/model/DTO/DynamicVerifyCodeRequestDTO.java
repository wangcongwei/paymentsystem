package com.newtouch.payment.model.DTO;

import java.io.Serializable;

public class DynamicVerifyCodeRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3493919456387989555L;

	/**
	 * 支付号
	 */
	private String paymentNo;
	
	/**
	 * 平台
	 */
	private String platform;
	
	/**
	 * 银行代码
	 */
	private String bankCode;
	
	/**
	 * 持卡人姓名
	 */
	private String accountName;
	
	/**
	 * 银行卡号
	 */
	private String accountNo;
	
	/**
	 * 信用卡失效年份
	 */
	private String expiredYear;
	
	/**
	 * 信用卡失效月份
	 */
	private String expiredMonth;
	
	/**
	 * 卡校验码
	 */
	private String cvv2;
	
	/**
	 * 证件类型
	 */
	private String idType;
	
	/**
	 * 证件号码
	 */
	private String idNo;
	
	/**
	 * 手机号码
	 */
	private String telePhone;

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getExpiredYear() {
		return expiredYear;
	}

	public void setExpiredYear(String expiredYear) {
		this.expiredYear = expiredYear;
	}

	public String getExpiredMonth() {
		return expiredMonth;
	}

	public void setExpiredMonth(String expiredMonth) {
		this.expiredMonth = expiredMonth;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	
}
