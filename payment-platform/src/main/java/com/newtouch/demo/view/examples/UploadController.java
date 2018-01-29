package com.newtouch.demo.view.examples;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.newtouch.common.exception.ApplicationException;
import com.newtouch.common.view.FileUploadProgressListener;
import com.newtouch.common.view.RespModel;
import com.newtouch.common.view.Views;

/**
 * 上传文件的例子
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/6/1
 */

@RequestMapping(value = "/examples/upload")
@Controller
public class UploadController {
	private static Logger logger = LoggerFactory.getLogger(UploadController.class);
	/** 上传目录名 */
	//@Value("#{'excel.upload.dir'}")
	private String uploadDir ="d:/";

	/** 允许上传的扩展名 */
	//@Value("#{'excel.upload.ext'}")
	private String[] extensionPermit=new String[]{"xls","xlsx"};

	/**
	 * 上传进度条支持
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upload/progress", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
	public RespModel progress(HttpSession session) {
		return new RespModel(session.getAttribute(FileUploadProgressListener.SESSION_KEY));// 返回上传进度
	}

	@ResponseBody
	@RequestMapping(value = "/upload", produces = "text/plain", method = RequestMethod.POST)
	public String upload(@RequestParam("orderId") String orderId, @RequestParam("file") MultipartFile uploadFile) {
		//上传文件附带参数
		logger.info("orderId:{}", orderId);
		RespModel respModel;
		try {
			String originalFileName = uploadFile.getOriginalFilename();
			if (originalFileName.length() > 0) {
				String saveToFileName = System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFileName);
				Views.uploadFile(uploadFile, uploadDir, saveToFileName, extensionPermit);
			}
			respModel=Views.getSuccessModel("Excel上传成功");
		} catch (ApplicationException ex) {
			respModel= Views.getErrorModel(ex.getCode());
		}
		
		return "true";
	}

}
