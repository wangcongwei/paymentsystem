package com.newtouch.common.view;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.newtouch.common.exception.ApplicationException;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/23
 */
public class Views {
	private static Logger logger = LoggerFactory.getLogger(Views.class);

	/**
	 * 操作成功提示
	 * 
	 * @return
	 */
	static public RespModel getSuccessModel() {
		RespModel model = new RespModel();
		model.setTimestamp(System.currentTimeMillis());
		model.setSuccess(true);
		return model;
	}

	/**
	 * 设置提示信息获取成功模型
	 * 
	 * @param message
	 * @return
	 */
	static public RespModel getSuccessModel(String message) {
		RespModel model = new RespModel();
		model.setTimestamp(System.currentTimeMillis());
		model.setErrMsg(message);
		model.setSuccess(true);
		return model;
	}

	/**
	 * 设置提示信息获取失败模型
	 * 
	 * @param errorCode
	 * @return
	 */
	static public RespModel getErrorModel(String errorCode) {
		RespModel model = new RespModel();
		model.setTimestamp(System.currentTimeMillis());
		model.setErrCode(errorCode);
		model.setErrMsg(getI18nMessage(ApplicationException.PREFIX + errorCode));
		model.setSuccess(false);
		return model;
	}

	/**
	 * 获取国际化资源
	 * 
	 * @param key
	 *            资源key
	 * @param args
	 *            资源参数
	 * @param defaultMessage
	 *            缺省值
	 * @return
	 */
	static public String getI18nMessage(String key, Object[] args, String defaultMessage) {
		String msg = defaultMessage;
		try {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = servletRequestAttributes.getRequest();
			ApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);
			ResourceBundleMessageSource messageSource = ac.getBean(ResourceBundleMessageSource.class);
			msg = messageSource.getMessage(key, args, defaultMessage, LocaleContextHolder.getLocale());
		} catch (Exception ex) {
			logger.info("not found error code:" + key + ", locale:" + LocaleContextHolder.getLocale().getDisplayName());
		}
		return msg;
	}

	/**
	 * 获取国际化资源
	 * 
	 * @param key
	 *            资源key
	 * @return
	 */
	static public String getI18nMessage(String key) {
		String msg = "";
		try {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = servletRequestAttributes.getRequest();
			ApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);
			ResourceBundleMessageSource messageSource = ac.getBean(ResourceBundleMessageSource.class);
			msg = messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
		} catch (Exception ex) {
			logger.info("not found error code:" + key + ", locale:" + LocaleContextHolder.getLocale().getDisplayName());
		}
		return msg;
	}

	/**
	 * 获取国际化资源
	 * 
	 * @param key
	 *            资源key
	 * @param args
	 *            资源参数
	 * @return
	 */
	static public String getI18nMessage(String key, Object[] args) {
		String msg = "";
		try {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = servletRequestAttributes.getRequest();
			ApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);
			ResourceBundleMessageSource messageSource = ac.getBean(ResourceBundleMessageSource.class);
			msg = messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
		} catch (Exception ex) {
			logger.info("not found error code:" + key + ", locale:" + LocaleContextHolder.getLocale().getDisplayName());
		}
		return msg;
	}

	static public void uploadFile(MultipartFile uploadFile, String uploadDir, String saveFileName, String[] extensionPermit) {
		String originalFileName = uploadFile.getOriginalFilename();
		if (originalFileName.length() > 0) {
			String fileExtension = FilenameUtils.getExtension(originalFileName);
			// 扩展名校验
			if (!ArrayUtils.contains(extensionPermit, fileExtension)) {
				throw new ApplicationException("Request.UnSupportExtension");
			}

			try {
				File saveToFile = new File(uploadDir + File.separator + saveFileName);
				uploadFile.transferTo(saveToFile);
			} catch (Exception ex) {
				logger.error("文件上传失败", ex);
				throw new ApplicationException("Request.FileUploadError");
			}
		}
	}
}
