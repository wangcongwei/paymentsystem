package com.newtouch.payment.model.DTO;

public class DynamicVerifyCodeResponseDTO {
	
	/**
	 * 错误码
	 */
	private String errorCode;
	
	/**
	 * 错误信息
	 */
	private String errorMessage;
	
	/**
	 * 令牌信息
	 */
	private String token;
	
	/**
	 * 应答码
	 */
	private String responseCode;
	
	/**
	 * 应答信息
	 */
	private String responseMessage;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
}
