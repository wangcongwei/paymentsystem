package com.newtouch.payment.service;

import com.newtouch.payment.model.AuthPro;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/06
 */
public interface AuthProService {
	/**
	 * 获取明细
	 * @param id
	 * @return
	 */
	AuthPro get(Long id);
	
	/**
	 * 删除AuthPro
	 * 
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * 保存AuthPro
	 */
	void create(AuthPro authPro);
	
	/**
	 * 更新AuthPro
	 */
	void patch(AuthPro authPro);
}
