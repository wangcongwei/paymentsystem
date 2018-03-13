package com.newtouch.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.view.model.orderpay.OrderPayReqVo;
import com.newtouch.payment.view.model.orderpay.OrderPayRespVo;
import com.newtouch.payment.view.model.orderquery.OrderQueryReqVo;
import com.newtouch.payment.view.model.orderquery.OrderQueryRespVo;
import com.newtouch.payment.view.model.quickorder.QuickOrderReqVo;
import com.newtouch.payment.view.model.quickorder.QuickOrderRespVo;
import com.newtouch.payment.view.model.quickorderSMS.QuickOrderSMSReqVo;
import com.newtouch.payment.view.model.quickorderSMS.QuickOrderSMSRespVo;
import com.newtouch.payment.view.model.quickorderpay.QuickOrderPayReqVo;
import com.newtouch.payment.view.model.quickorderpay.QuickOrderPayRespVo;


public interface OrderPayService {

	public OrderQueryRespVo doFindByOrderNo(OrderQueryReqVo orderQueryReqVo);
	/**
	 * 支付
	 * @param orderPayReqVo
	 * @return
	 */
	public OrderPayRespVo  doPay(OrderPayReqVo orderPayReqVo);
	
	

	/**
	 * 创建快捷支付支付订单
	 * @param orderPayReqVo
	 * @return
	 */
	public QuickOrderRespVo doQuickOrder(QuickOrderReqVo quickOrderReqVo);
	
	/**
	 * 获取快捷支付验证码
	 * @param orderPayReqVo
	 * @return
	 */
	public QuickOrderSMSRespVo doQuickOrderSMS(QuickOrderSMSReqVo quickOrderSMSReqVo);
	
	
	/**
	 * 进行快捷支付
	 * @param orderPayReqVo
	 * @return
	 */
	public QuickOrderPayRespVo doQuickOrderPay(QuickOrderPayReqVo quickOrderPayReqVo);
	
	/**
	 * 获得支付订单号
	 * @return
	 */
	public String getOrderNo();
	
	
	
}
