package com.newtouch.payment.model.DTO;

public class DynamicVerifyCodeRequestDTO {
	
	/**
	 * 支付流水号
	 */
	private String userTransactionNo;
	
	/**
	 * 持卡人姓名
	 */
	private String accountName;
	
	/**
	 * 银行卡号
	 */
	private String accountNo;
	
	/**
	 * 有效期
	 */
	private String expiredDate;
	
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
	 * 银行代码
	 */
	private String bankId;
	
	/**
	 * 手机号码
	 */
	private String telePhone;
	
	public String getUserTransactionNo() {
		return userTransactionNo;
	}

	public void setUserTransactionNo(String userTransactionNo) {
		this.userTransactionNo = userTransactionNo;
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

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
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

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

}
