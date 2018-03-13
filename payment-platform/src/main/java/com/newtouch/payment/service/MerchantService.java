package com.newtouch.payment.service;

import com.newtouch.payment.model.Merchant;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface MerchantService {
	/**
	 * 获取商户明细
	 * @param id
	 * @return
	 */
	Merchant get(Long id);
	
	/**
	 * 删除Merchant
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存Merchant
	 */
	void create(Merchant merchant);
	
	/**
	 * 更新Merchant
	 */
	void patch(Merchant merchant);
}
