package com.newtouch.payment.view.model.orderpay;

import java.util.Date;

import com.newtouch.payment.view.model.RespVo;

/**
 * 订单支付返回体
 * @author xiangzhe.zeng
 *
 */
public class OrderPayRespVo extends RespVo {
	
	/**
	 * 支付订单号
	 */
	private String orderNo;
	/**
	 * 网关支付url
	 */
	private String payUrl;
	
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

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
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
