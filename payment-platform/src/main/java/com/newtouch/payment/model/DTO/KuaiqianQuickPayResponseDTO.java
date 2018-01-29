package com.newtouch.payment.model.DTO;

public class KuaiqianQuickPayResponseDTO {

	/**
	 * 错误代码
	 */
	private String errorCode;
	
	/**
	 * 错误消息
	 */
	private String errorMessage;
	
	/**
	 * 系统来源
	 */
	private String systemCode;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 订单金额
	 */
	private String orderAmount;
	
	/**
	 * 外部跟踪编号
	 */
	private String externalRefNumber;
	
	/**
	 * 交易传输时间
	 */
	private String transTime;
	
	/**
	 * 系统参考号
	 */
	private String refNumber;
	
	/**
	 * 应答码
	 */
	private String responseCode;
	
	/**
	 * 缩略卡号
	 */
	private String storableCardNo;
	
	/**
	 * 授权码
	 */
	private String authorizationCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getExternalRefNumber() {
		return externalRefNumber;
	}

	public void setExternalRefNumber(String externalRefNumber) {
		this.externalRefNumber = externalRefNumber;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getStorableCardNo() {
		return storableCardNo;
	}

	public void setStorableCardNo(String storableCardNo) {
		this.storableCardNo = storableCardNo;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
}
