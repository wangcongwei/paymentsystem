package com.newtouch.payment.service;

import com.newtouch.payment.model.Payment;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface PaymentService {
	
	/**
	 * 获取支付信息
	 * @param id
	 * @return
	 */
	Payment get(Long id);
	
	/**
	 * 删除Payment
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存Payment
	 */
	void create(Payment payment);
	
	/**
	 * 更新Payment
	 */
	void patch(Payment payment);
}
