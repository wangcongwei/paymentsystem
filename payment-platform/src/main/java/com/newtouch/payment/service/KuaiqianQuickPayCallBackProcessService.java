
package com.newtouch.payment.service;

import java.util.Map;



public interface KuaiqianQuickPayCallBackProcessService {
	/**
	 * 保存快钱无卡支付通知信息
	 * 
	 * @param payInfo
	 * @throws CallBackBussinessException 
	 */
	public void saveCallBackPayment(Map<String, String> payInfo) throws Exception;
}
