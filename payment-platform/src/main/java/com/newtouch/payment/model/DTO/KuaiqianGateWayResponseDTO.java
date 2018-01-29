package com.newtouch.payment.model.DTO;

import java.util.Map;

public class KuaiqianGateWayResponseDTO {

	private String url;
	
	private Map<String, String> params;

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
