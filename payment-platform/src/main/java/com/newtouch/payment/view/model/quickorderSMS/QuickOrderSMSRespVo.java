package com.newtouch.payment.view.model.quickorderSMS;

import com.newtouch.payment.view.model.RespVo;
/**
 * 快捷支付短信验证返回
 * @author xiangzhe.zeng
 *
 */
public class QuickOrderSMSRespVo extends RespVo {
	
	/**
	 * 支付订单号
	 */
	private String orderNo;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
