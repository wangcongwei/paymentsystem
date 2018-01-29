package com.newtouch.payment.service;

import com.newtouch.payment.model.CompensationRecord;
import com.newtouch.payment.model.OrderPayRequest;
import com.newtouch.payment.model.TPayPlatformTransation;


/**
 * 交易流水状态查询
 * @author xf
 *
 */
public interface TransStatusSearch {
	
	/**
	 * 根据交易流水号查询交易流水状态
	 * @return 交易流水状态(0-失败；1-成功；2-处理中)
	 */
	public String SearchStatusByTransNo(String transNo);
	
	/**
	 * 更新支付订单信息
	 * @param orderPayRequest
	 */
	public void idoOrderPayRequest(OrderPayRequest orderPayRequest);
	
	/**
	 * 更新交易流水信息
	 * @param tppt
	 */
	public void idoTPayPlatformTransation(TPayPlatformTransation tppt);
	
	/**
	 * 保存交易查询补偿记录
	 * @param cr
	 */
	public void idoCompensationRecord(CompensationRecord cr);
	
}
