package com.newtouch.payment.service;

import com.newtouch.payment.model.ReconciliationDetail;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface ReconciliationDetailService {
	/**
	 * 获取明细
	 * @param id
	 * @return
	 */
	ReconciliationDetail get(Long id);
	
	/**
	 * 删除ReconciliationDetail
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存ReconciliationDetail
	 */
	void create(ReconciliationDetail reconciliationDetail);
	
	/**
	 * 更新ReconciliationDetail
	 */
	void patch(ReconciliationDetail reconciliationDetail);
}
