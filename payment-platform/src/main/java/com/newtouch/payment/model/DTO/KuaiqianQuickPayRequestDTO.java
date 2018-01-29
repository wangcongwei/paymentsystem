package com.newtouch.payment.model.DTO;

public class KuaiqianQuickPayRequestDTO {
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 卡有效期
	 */
	private String expiredDate;
	
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
	 * 支付流水号
	 */
	private String userTransactionNo;
	
	/**
	 * 手机验证码
	 */
	private String validCode;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getUserTransactionNo() {
		return userTransactionNo;
	}

	public void setUserTransactionNo(String userTransactionNo) {
		this.userTransactionNo = userTransactionNo;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	
	
}
