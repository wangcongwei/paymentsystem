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
	 * 快钱返回码
	 */
	private String responseCode;
	
	/**
	 * 支付号
	 */
	private String paymentNo;
	
	/**
	 * 支付号金额
	 */
	private String paymentAmount;
	
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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
