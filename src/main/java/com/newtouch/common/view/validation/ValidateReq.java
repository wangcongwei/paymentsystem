package com.newtouch.common.view.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidateReq {
	private List<ValidateRule> fields = new ArrayList<ValidateRule>();
	private String url;
	private String[] method;
	private boolean clientEnable = true;
	private boolean serverEnable = true;

	public List<ValidateRule> getFields() {
		return fields;
	}

	public void setFields(List<ValidateRule> fields) {
		this.fields = fields;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getMethod() {
		return method;
	}

	public void setMethod(String[] method) {
		this.method = method;
	}

	public boolean isClientEnable() {
		return clientEnable;
	}

	public void setClientEnable(boolean clientEnable) {
		this.clientEnable = clientEnable;
	}

	public boolean isServerEnable() {
		return serverEnable;
	}

	public void setServerEnable(boolean serverEnable) {
		this.serverEnable = serverEnable;
	}

	@Override
	public String toString() {
		return "ValidateReq{" + "fields=" + fields + ", url='" + url + '\'' + ", clientEnable=" + clientEnable
				+ ", serverEnable=" + serverEnable + '}';
	}
}
