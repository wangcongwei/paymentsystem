package com.newtouch.payment.service;

import com.newtouch.payment.model.TPayPlatformTransation;


/**
 * 调用快钱交易流水查询接口
 * @author xf
 *
 */
public interface TransFlowSearch {
	
	/**
	 * 调用快钱交易流水查询接口获取交易状态
	 * @param tppt（交易流水信息）
	 * @return 交易流水状态（FAILED-失败；SUCCESS-成功；CREATE-处理中）
	 */
	public String searchTransFlow(TPayPlatformTransation tppt);
}
