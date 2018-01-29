package com.newtouch.payment.service;

import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;

public interface KuaiqianGateWayService {
	
	/**
	 * 根据用户请求流水请求第三方支付
	 * @param userTransactionNo
	 * @return
	 */
	public KuaiqianGateWayResponseDTO gateWayPay(String userTransactionNo,String bankId);

}
