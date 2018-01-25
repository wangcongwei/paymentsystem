package com.newtouch.common.view;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/4/1
 */

public class ReqItem {
	private String name;
	private String value;
	private String[] values;
	private String type;
	private String format;
	private boolean array;

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public ReqItem() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean isArray) {
		this.array = isArray;
	}
}
