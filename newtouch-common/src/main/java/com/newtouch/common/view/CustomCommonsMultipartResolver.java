package com.newtouch.common.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.newtouch.common.exception.ApplicationException;

/**
 * 上传进度条支持
 * 
 * @author dongfeng.zhang
 *
 */
public class CustomCommonsMultipartResolver extends CommonsMultipartResolver {
	private boolean useProgress = false;

	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		if (useProgress) {
			FileUploadProgressListener progressListener = new FileUploadProgressListener(request.getSession());
			fileUpload.setProgressListener(progressListener);
		}
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new ApplicationException("Request.UploadSizeError",new Object[]{fileUpload.getSizeMax()});
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}

	public void setUseProgress(boolean useProgress) {
		this.useProgress = useProgress;
	}

}
