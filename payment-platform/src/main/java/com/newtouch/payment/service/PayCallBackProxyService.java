package com.newtouch.payment.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/13
 */
public interface PayCallBackProxyService {
	
	/**
	 * 处理第三方支付平台的支付通知
	 * 
	 * @param req
	 *            通知的HttpServletRequest对象
	 * @param resp
	 *            通知的HttpServletResponse对象
	 * @throws IOException
	 */
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
