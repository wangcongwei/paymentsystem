package com.newtouch.payment.model.DTO;

import java.util.Map;

public class KuaiqianGateWayResponseDTO {
	
	/**
	 * 错误码
	 */
	private String errorCode;
	
	/**
	 * 错误信息
	 */
	private String errorMessage;

	private String url;
	
	private Map<String, String> params;
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}
