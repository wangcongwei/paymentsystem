/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-22 上午11:12:27 MjunLee
 */
package com.newtouch.payment.service;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * Description:快钱网关支付通知处理服务
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: NewTouch
 * </p>
 * 
 * @author MjunLee
 * @version 1.0
 */
public interface KuaiqianGatewayCallBackProcessService {
	/**
	 * 
	 * @param req
	 * @param pas
	 * @return
	 * @throws CallBackBussinessException
	 */
	public void saveCallBackPayment(HttpServletRequest req) throws Exception;
	
	/**
	 * 
	 * @param req
	 * @param pas
	 * @return
	 * @throws CallBackBussinessException
	 */
	public void saveOnlineCallBackPayment(HttpServletRequest req) throws Exception;
}
