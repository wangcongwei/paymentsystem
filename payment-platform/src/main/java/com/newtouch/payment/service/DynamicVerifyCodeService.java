package com.newtouch.payment.service;

import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;

public interface DynamicVerifyCodeService {
	
	/**
	 * 根据请求信息获得手机验证码
	 * @param request
	 * @return
	 */
	public DynamicVerifyCodeResponseDTO getDynamicVerifyCode(DynamicVerifyCodeRequestDTO request);

}
