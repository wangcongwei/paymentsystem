package com.newtouch.payment.model.DTO;

public class KuaiqianRequestDTO {
	
	/**
	 * 服务消费方
	 */
	private String consumerID;
	
	/**
	 * 渠道代码
	 */
	private String comCode;
	
	/**
	 * 支付号
	 */
	private String paymentNo;
	
	/**
	 * 卡类型
	 */
	private String cardType;
	
	/**
	 * 银行代码
	 */
	private String bankCode;
	
	/**
	 * 账户名
	 */
	private String accountName;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 卡有效期 年
	 */
	private String expiredYear;
	
	/**
	 * 卡有效期 月
	 */
	private String expiredMonth;
	
	/**
	 * 卡检验码
	 */
	private String cvv2;
	
	/**
	 * 持卡人身份证号
	 */
	private String cardHolderId;
	
	/**
	 * 证件类型
	 */
	private String idType;
	
	/**
	 * 手机号
	 */
	private String telePhone;
	
	/**
	 * 手机验证码
	 */
	private String validCode;
	
	public String getConsumerID() {
		return consumerID;
	}

	public void setConsumerID(String consumerID) {
		this.consumerID = consumerID;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getCardHolderId() {
		return cardHolderId;
	}

	public void setCardHolderId(String cardHolderId) {
		this.cardHolderId = cardHolderId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

}
