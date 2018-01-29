package com.newtouch.payment.view.model.quickorderpay;

import java.util.Date;

import com.newtouch.payment.view.model.RespVo;

/**
 * 快捷支付返回
 * @author xiangzhe.zeng
 *
 */
public class QuickOrderPayRespVo extends RespVo {

	/**
	 * 支付订单号
	 */
	private String orderNo;
	
	/**
	 * 支付订单创建时间
	 */
	private Date creatTime;
	/**
	 * 支付订单失效时间
	 */
	private Date outTime;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

}
