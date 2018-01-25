package com.newtouch.common.model;

public class ValidationFieldRule {
	private String name;
	private boolean required;
	private String type;
	private int minLen = 0;
	private int maxLen = 100;
	private String rangeStart;
	private String rangeEnd;
	private String pattern;
	private String errMsg;
	private String errMsgCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMinLen() {
		return minLen;
	}

	public void setMinLen(int minLen) {
		this.minLen = minLen;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public String getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(String rangeStart) {
		this.rangeStart = rangeStart;
	}

	public String getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(String rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getErrMsgCode() {
		return errMsgCode;
	}

	public void setErrMsgCode(String errMsgCode) {
		this.errMsgCode = errMsgCode;
	}

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", type='" + type + '\'' +
                ", minLen=" + minLen +
                ", maxLen=" + maxLen +
                ", rangeStart='" + rangeStart + '\'' +
                ", rangeEnd='" + rangeEnd + '\'' +
                ", pattern='" + pattern + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", errMsgCode='" + errMsgCode + '\'' +
                '}';
    }
}
