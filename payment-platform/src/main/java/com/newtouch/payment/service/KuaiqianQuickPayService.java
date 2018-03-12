package com.newtouch.payment.service;

import com.newtouch.payment.model.PaymentTransaction;
import com.newtouch.payment.model.TPayPlatformTransation;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayRequestDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;

public interface KuaiqianQuickPayService {
	
	/**
	 * 根据请求信息请求第三方支付并获得响应
	 * @param request
	 * @return
	 */
	public KuaiqianQuickPayResponseDTO quickPay(KuaiqianQuickPayRequestDTO request);
	
	/**
	 * 根据支付交易流水表请求第三方支付
	 * @param tppt-支付交易流水表
	 * @return 响应
	 */
	public KuaiqianQuickPayResponseDTO quickPay(PaymentTransaction pt);
}
