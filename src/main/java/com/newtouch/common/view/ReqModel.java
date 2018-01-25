package com.newtouch.common.view;

import org.springframework.data.domain.Pageable;

import com.newtouch.common.model.QueryParams;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/31
 */
public class ReqModel {
	private long timestamp;
	private Object content;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Object getContent() {
		return content;
	}

	public Pageable getPageable() {
		return ModelConvertUtils.getPageable(content);
	}

	public void setContent(Object content) {
		this.content = ModelConvertUtils.convertContent(content);
	}

	public QueryParams getQueryParams() {
		return ModelConvertUtils.getQueryParams(content);
	}

	// 转换请求模型到POJO对象
	public <T> T toObject(Class<T> clazz) {
		return ModelConvertUtils.toObject(content, clazz);
	}
}
