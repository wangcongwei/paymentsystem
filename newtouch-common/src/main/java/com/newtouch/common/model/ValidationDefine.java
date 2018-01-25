package com.newtouch.common.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationDefine {
	private List<ValidationFieldRule> fields = new ArrayList<ValidationFieldRule>();
	private String intPath;
	private String intMethod;
	private boolean clientEnable = true;
	private boolean serverEnable = true;

	public List<ValidationFieldRule> getFields() {
		return fields;
	}

	public void setFields(List<ValidationFieldRule> fields) {
		this.fields = fields;
	}

	public String getIntPath() {
		return intPath;
	}

	public void setIntPath(String intPath) {
		this.intPath = intPath;
	}

	public String getIntMethod() {
		return intMethod;
	}

	public void setIntMethod(String intMethod) {
		this.intMethod = intMethod;
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
		return "ReqRuleModel [fields=" + fields + ", intPath=" + intPath + ", intMethod=" + intMethod
				+ ", clientEnable=" + clientEnable + ", serverEnable=" + serverEnable + "]";
	}
}
