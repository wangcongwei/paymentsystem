package com.newtouch.payment.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface KuaiqianPayCallBackService {
	/**
	 * 处理快钱支付平台的支付通知
	 * 
	 * @param req
	 *            通知HttpServletRequest
	 * @param resp
	 *            通知HttpServletResponse
	 */
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
