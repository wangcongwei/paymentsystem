package com.newtouch.payment.service;

import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianRequestDTO;


public interface PaymentPlatformService {

	/**
	 * 快钱网关支付
	 * @param requestDto
	 * @return
	 */
	public KuaiqianGateWayResponseDTO  gatewayPay(KuaiqianRequestDTO requestDto);
	
	/**
	 * 获取快捷支付验证码
	 * @param requestDto
	 * @return
	 */
	public DynamicVerifyCodeResponseDTO getQuickPaySmsCode(DynamicVerifyCodeRequestDTO requestDto);
	
	/**
	 * 进行快捷支付
	 * @param orderPayReqVo
	 * @return
	 */
	public KuaiqianQuickPayResponseDTO quickPay(KuaiqianRequestDTO requestDto);
	
}
