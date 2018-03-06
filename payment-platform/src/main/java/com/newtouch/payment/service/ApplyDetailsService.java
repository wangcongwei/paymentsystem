package com.newtouch.payment.service;

import com.newtouch.payment.model.ApplyDetails;

/**
 * 申请明细服务
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/3/6
 */
public interface ApplyDetailsService {
	
	/**
	 * 获取明细
	 * @param id
	 * @return
	 */
	ApplyDetails get(Long id);
	
	/**
	 * 删除ApplyDetails
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存ApplyDetails
	 */
	void create(ApplyDetails applyDetails);
	
	/**
	 * 更新ApplyDetails
	 */
	void patch(ApplyDetails applyDetails);
	
}
