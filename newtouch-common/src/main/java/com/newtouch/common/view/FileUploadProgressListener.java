package com.newtouch.common.view;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * 上传进度条支持
 * 
 * @author c_zhangdongfeng
 */
public class FileUploadProgressListener implements ProgressListener {
	public static final String SESSION_KEY = "_FILE_UPLOAD_PROGRESS_LISTENER_UPLOAD_STATUS_";
	private HttpSession session;
	private long bytesRead = 0; // 已经上传的部分
	private long contentLength = -1; // 总长度
	private int itemIndex = 0; // 上传第几个文件
	private int uploadPercent = 0; // 上传百分比

	public FileUploadProgressListener(HttpSession session) {
		this.session = session;
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {

		this.bytesRead = pBytesRead;// 已经上传的部分
		this.contentLength = pContentLength;// 总长度
		this.itemIndex = pItems;// 上传第几个文件

		if (contentLength != -1) {
			uploadPercent = (int) Math.round(100.00 * bytesRead / contentLength);
		}
		session.setAttribute(SESSION_KEY, this);
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getUploadPercent() {
		return uploadPercent;
	}

	public void setUploadPercent(int uploadPercent) {
		this.uploadPercent = uploadPercent;
	}
}