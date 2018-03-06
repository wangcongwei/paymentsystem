package com.newtouch.payment.service;

import com.newtouch.payment.model.PaymentTransaction;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface PaymentTransactionService {
	/**
	 * 获取明细
	 * @param id
	 * @return
	 */
	PaymentTransaction get(Long id);
	
	/**
	 * 删除PaymentTransaction
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存PaymentTransaction
	 */
	void create(PaymentTransaction paymentTransaction);
	
	/**
	 * 更新PaymentTransaction
	 */
	void patch(PaymentTransaction paymentTransaction);
}
