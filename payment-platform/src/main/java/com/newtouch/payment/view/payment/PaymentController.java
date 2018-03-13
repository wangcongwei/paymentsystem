package com.newtouch.payment.view.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newtouch.common.model.QueryParams;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.demo.service.CurdService;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.service.OrderPayService;
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

/**
 * 一个增删改查的例子
 * @author xiangzhe.zeng
 * @version 1.0
 * @date 2015/6/1
 * http://localhost:8080/payment-platform/orderpay/netpay/index.html
 * 
 */

@RequestMapping(value = "/orderpay/netpay")
@Controller
public class PaymentController{
	private static Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	private CurdService curdService;
	
	@Autowired
	private OrderPayService orderPayService;
	
//	/**
//	 * ajax 列表查询 查询
//	 * 
//	 * @param reqModel
//	 *            请求模型
//	 * @return json数据
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/orderListQuery", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
//	public RespModel orderListQuery(@RequestBody ReqModel reqModel) {
//		Pageable pageable = reqModel.getPageable();
//		QueryParams queryParams = reqModel.getQueryParams();
//		logger.trace("pagePageNumber={}", pageable.getPageNumber());
//		logger.trace("queryParams={}", queryParams);
////		Page<Order> orderPage = orderPayService.page(queryParams, pageable);
//		return new RespModel(new Page<Payment>());
//	}
	
	/**
	 * 订单支付,返回订单支付号
	 * @param orderPayReqVo
	 * @param errors
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/orderPay",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel orderPay(@RequestBody ReqModel reqModel) {
		OrderPayReqVo orderPayReqVo = reqModel.toObject(OrderPayReqVo.class);
		logger.trace(orderPayReqVo.toString());
		OrderPayRespVo orderPayRespVo  = orderPayService.doPay(orderPayReqVo);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("OrderNo", orderPayRespVo.getOrderNo());
//		params.put("CreatTime", orderPayRespVo.getCreatTime().getTime()+"");
//		params.put("OutTime", orderPayRespVo.getOutTime().getTime()+"");
//		params.put("Status", orderPayRespVo.getStatus());
//		params.put("ErrorCode", orderPayRespVo.getErrorCode());
//		params.put("ErrorMessage", orderPayRespVo.getErrorMessage());
		return new RespModel(orderPayRespVo);
	}

	/**
	 * 订单支付结果查询
	 * @param orderQueryReqVo
	 * @param errors
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/orderQuery",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel orderQuery(@RequestBody ReqModel reqModel) {
		OrderQueryReqVo orderQueryReqVo= reqModel.toObject(OrderQueryReqVo.class);
		OrderQueryRespVo orderQueryRespVo = this.orderPayService.doFindByOrderNo(orderQueryReqVo);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("OrderStatus", orderQueryRespVo.getOrderStatus());
//		params.put("PayDesc", orderQueryRespVo.getPayDesc());
//		params.put("Status", orderQueryRespVo.getStatus());
//		params.put("ErrorCode", orderQueryRespVo.getErrorCode());
//		params.put("ErrorMessage", orderQueryRespVo.getErrorMessage());
		return new RespModel(orderQueryRespVo);
	}
	
	/**
	 * 请求快捷支付
	 * @param quickOrderPayReqVo
	 * @param errors
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/quickOrder",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel quickOrder(@RequestBody ReqModel reqModel) {
		QuickOrderReqVo quickOrderReqVo= reqModel.toObject(QuickOrderReqVo.class);
		QuickOrderRespVo quickOrderRespVo = this.orderPayService.doQuickOrder(quickOrderReqVo);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("OrderNo", quickOrderRespVo.getOrderNo());
//		params.put("CreatTime", quickOrderRespVo.getCreatTime().getTime()+"");
//		params.put("OutTime", quickOrderRespVo.getOutTime().getTime()+"");
//		params.put("QuickOrderPayurl", quickOrderRespVo.getQuickOrderPayurl());
//		params.put("Status", quickOrderRespVo.getStatus());
//		params.put("ErrorCode", quickOrderRespVo.getErrorCode());
//		params.put("ErrorMessage", quickOrderRespVo.getErrorMessage());
		return new RespModel(quickOrderRespVo);
	}
	
	/**
	 * 快捷支付短信验证
	 * @param quickOrderBodyReqVo
	 * @param errors
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/quickOrderSMS",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel quickOrderSMS(@RequestBody ReqModel reqModel) {
		QuickOrderSMSReqVo quickOrderBodyReqVo= reqModel.toObject(QuickOrderSMSReqVo.class);
		QuickOrderSMSRespVo quickOrderBodyRespVo = this.orderPayService.doQuickOrderSMS(quickOrderBodyReqVo);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("OrderNo", quickOrderBodyRespVo.getOrderNo());
//		params.put("Status", quickOrderBodyRespVo.getStatus());
//		params.put("ErrorCode", quickOrderBodyRespVo.getErrorCode());
//		params.put("ErrorMessage", quickOrderBodyRespVo.getErrorMessage());
		return new RespModel(quickOrderBodyRespVo);
	}
	
	/**
	 * 快捷支付
	 * @param quickOrderPayReqVo
	 * @param errors
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/quickOrderPay",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel quickOrderPay(@RequestBody ReqModel reqModel) {
		QuickOrderPayReqVo quickOrderPayReqVo = reqModel.toObject(QuickOrderPayReqVo.class);
		QuickOrderPayRespVo quickOrderPayRespVo = this.orderPayService.doQuickOrderPay(quickOrderPayReqVo);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("OrderNo", quickOrderPayRespVo.getOrderNo());
//		params.put("Status", quickOrderPayRespVo.getStatus());
//		params.put("ErrorCode", quickOrderPayRespVo.getErrorCode());
//		params.put("ErrorMessage", quickOrderPayRespVo.getErrorMessage());
		return new RespModel(quickOrderPayRespVo);
	}
	
}
