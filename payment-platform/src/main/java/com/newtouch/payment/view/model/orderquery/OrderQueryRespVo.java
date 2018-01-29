package com.newtouch.payment.view.model.orderquery;

import java.math.BigDecimal;

import com.newtouch.payment.view.model.RespVo;

/**
 * 支付结果查询返回体
 * @author xiangzhe.zeng
 *
 */
public class OrderQueryRespVo extends RespVo {
	
	/**
	 * 支付状态
	 */
	private String orderStatus;
	/**
	 * 支付状态描述
	 */
	private String payDesc;
	/**
	 * 订单金额
	 */
	private BigDecimal amount;

	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPayDesc() {
		return payDesc;
	}
	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
