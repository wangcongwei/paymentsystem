package com.newtouch.payment.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.demo.service.impl.CurdServiceImpl;
import com.newtouch.payment.im.OrderPayStatus;
import com.newtouch.payment.im.PayChannel;
import com.newtouch.payment.im.PayRequestStatus;
import com.newtouch.payment.model.BusinessNum;
import com.newtouch.payment.model.Order;
import com.newtouch.payment.model.OrderPayRequest;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayRequestDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.repository.BusinessNumRepo;
import com.newtouch.payment.repository.OrderPayRequestRepo;
import com.newtouch.payment.repository.OrderRepo;
import com.newtouch.payment.service.DynamicVerifyCodeService;
import com.newtouch.payment.service.KuaiqianGateWayService;
import com.newtouch.payment.service.KuaiqianQuickPayService;
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

@Service
public class OrderPayServiceImpl implements OrderPayService {
	
	private static Logger logger = LoggerFactory.getLogger(CurdServiceImpl.class);
	
	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderPayRequestRepo orderPayRequestRepo;

	@Autowired
	private BusinessNumRepo businessNumRepo;
	
	
	@Autowired
	private KuaiqianGateWayService kuaiqianGateWayService;
	
	@Autowired
	private KuaiqianQuickPayService kuaiqianQuickPayService;
	
	@Autowired
	private DynamicVerifyCodeService dynamicVerifyCodeService;
	

	/**
	 * 请求支付
	 */
	@Override
	public OrderPayRespVo doPay(OrderPayReqVo orderPayReqVo) {
		StringBuffer errorMsg = new StringBuffer("");

		if (checkBusNum(orderPayReqVo.getBusNum(), PayChannel.E_1)) {
			errorMsg.append("未开通该支付方式!");
		}
		// 校验商户参数
		errorMsg.append(checkRequest(orderPayReqVo.getAmount(), "订单金额"));
		errorMsg.append(checkRequest(orderPayReqVo.getBusNum(), "商户号"));
		OrderPayRespVo orderPayRespVo = new OrderPayRespVo();
		if (!"".equals(errorMsg.toString())) {
			orderPayRespVo.setErrorCode("E0001");
			orderPayRespVo.setErrorMessage(errorMsg.toString());
			orderPayRespVo.setStatus("0");
			return orderPayRespVo;
		}
		String orderNo = orderPayRespVo.getOrderNo();
		Order order = null;
		if (!StringUtils.isBlank(orderNo)) {
			order = orderRepo.findByOrderNo(orderNo);
		}
		if(order!=null&&OrderPayStatus.E_3.equals(order.getOrderStatus())){
			orderPayRespVo.setOrderNo(orderNo);
			orderPayRespVo.setErrorCode("");
			orderPayRespVo.setErrorMessage("订单支付中");
			orderPayRespVo.setStatus("1");
			return orderPayRespVo;
		}
		if(order!=null&&OrderPayStatus.E_4.equals(order.getOrderStatus())){
			orderPayRespVo.setOrderNo(orderNo);
			orderPayRespVo.setErrorCode("");
			orderPayRespVo.setErrorMessage("订单支付成功");
			orderPayRespVo.setStatus("1");
			return orderPayRespVo;
		}
		if(order!=null&&order.getOutTime().after(new Date())){
			orderPayRespVo.setOrderNo(orderNo);
			orderPayRespVo.setErrorCode("");
			orderPayRespVo.setErrorMessage("订单已经过期");
			orderPayRespVo.setStatus("1");
			return orderPayRespVo;
		}
		//保存订单
		order = saveOrder(order ,orderPayReqVo);
		//保存支付流水
		OrderPayRequest orderPayRequest = savePayRequest(order,null,null,null,null,null);
		// TODO 调用支付方法,返回调用结果
		try{
			KuaiqianGateWayResponseDTO kuaiqianGateWayResponseDTO=kuaiqianGateWayService.gateWayPay(orderPayRequest.getPaySeriNo(), "1");
			orderPayRespVo.setPayUrl(kuaiqianGateWayResponseDTO.getUrl());
			orderPayRespVo.setErrorCode("");
			orderPayRespVo.setErrorMessage("");
			orderPayRespVo.setStatus("1");
		}catch(Exception e){
			logger.error("调用网关支付失败",e);
			orderPayRespVo.setErrorCode("");
			orderPayRespVo.setErrorMessage("调用网关支付失败");
			orderPayRespVo.setStatus("1");
		}
		orderPayRespVo.setOrderNo(orderNo);
		return orderPayRespVo;
	}

	/**
	 * 支付结果查询
	 */
	@Override
	public OrderQueryRespVo doFindByOrderNo(OrderQueryReqVo orderQueryReqVo) {
		String orderNo = orderQueryReqVo.getOrderNo();// 支付订单号
		String busNum = orderQueryReqVo.getBusNum();// 商户号
		Order order = orderRepo.findByOrderNo(orderNo);
		OrderQueryRespVo orderQueryRespVo = new OrderQueryRespVo();
		if (busNum != null && busNum.equals(order.getBusNum())) {
			orderQueryRespVo.setOrderStatus(order.getOrderStatus());
			orderQueryRespVo.setErrorCode("");
			orderQueryRespVo.setErrorMessage("");
			orderQueryRespVo.setStatus("1");
			return orderQueryRespVo;
		} else {
			orderQueryRespVo.setErrorCode("E0002");
			orderQueryRespVo.setErrorMessage("该商户号不存在或该商户无此订单!");
			orderQueryRespVo.setStatus("0");
			return orderQueryRespVo;
		}
	}

	/**
	 * 创建快捷支付支付订单
	 */
	@Override
	public QuickOrderRespVo doQuickOrder(QuickOrderReqVo quickOrderReqVo) {
		StringBuffer errorMsg = new StringBuffer("");
		if (checkBusNum(quickOrderReqVo.getBusNum(), PayChannel.E_2)) {
			errorMsg.append("未开通该支付方式!");
		}
		// 校验商户参数
		errorMsg.append(checkRequest(quickOrderReqVo.getAmount(), "订单金额"));
		errorMsg.append(checkRequest(quickOrderReqVo.getBusNum(), "商户号"));
		QuickOrderRespVo quickOrderRespVo = new QuickOrderRespVo();
		if (!"".equals(errorMsg.toString())) {
			quickOrderRespVo.setErrorCode("E0003");
			quickOrderRespVo.setErrorMessage(errorMsg.toString());
			quickOrderRespVo.setStatus("0");
			return quickOrderRespVo;
		}
		//保存支付订单
		Order order =new Order();
		order = saveOrder(order,quickOrderReqVo);
		quickOrderRespVo.setOrderNo(order.getOrderNo());
		quickOrderRespVo.setQuickOrderPayurl("");
		quickOrderRespVo.setErrorCode("");
		quickOrderRespVo.setErrorMessage("");
		quickOrderRespVo.setStatus("");
		return quickOrderRespVo;
	}

	/**
	 * 快捷支付验证码短信获取
	 */
	@Override
	public QuickOrderSMSRespVo doQuickOrderSMS(QuickOrderSMSReqVo quickOrderSMSReqVo) {
		OrderPayRequest orderPayRequest = saveQuikcPaySMSRequest(quickOrderSMSReqVo);
		DynamicVerifyCodeRequestDTO dynamicVerifyCodeRequestDTO = new DynamicVerifyCodeRequestDTO();
		dynamicVerifyCodeRequestDTO.setAccountName(quickOrderSMSReqVo.getUserName());
		dynamicVerifyCodeRequestDTO.setAccountNo(quickOrderSMSReqVo.getUserAccount());
//		dynamicVerifyCodeRequestDTO.setBankId(bankId);
		dynamicVerifyCodeRequestDTO.setCvv2(quickOrderSMSReqVo.getCvv2());
		dynamicVerifyCodeRequestDTO.setExpiredDate(quickOrderSMSReqVo.getExpiredDate());
		dynamicVerifyCodeRequestDTO.setIdNo(quickOrderSMSReqVo.getUserCerNum());
		dynamicVerifyCodeRequestDTO.setIdType(quickOrderSMSReqVo.getUserCerType());
		dynamicVerifyCodeRequestDTO.setTelePhone(quickOrderSMSReqVo.getUserMobile());
		dynamicVerifyCodeRequestDTO.setUserTransactionNo(orderPayRequest.getPaySeriNo());
		DynamicVerifyCodeResponseDTO dynamicVerifyCodeResponseDTO=dynamicVerifyCodeService.getDynamicVerifyCode(dynamicVerifyCodeRequestDTO);
		QuickOrderSMSRespVo quickOrderBodyRespVo = new QuickOrderSMSRespVo();
		quickOrderBodyRespVo.setOrderNo(quickOrderSMSReqVo.getOrderNo());
		quickOrderBodyRespVo.setErrorCode(dynamicVerifyCodeResponseDTO.getErrorCode());
		quickOrderBodyRespVo.setErrorMessage(dynamicVerifyCodeResponseDTO.getErrorMessage());
		return quickOrderBodyRespVo;
	}

	/**
	 * 快捷支付
	 */
	@Override
	public QuickOrderPayRespVo doQuickOrderPay(QuickOrderPayReqVo quickOrderPayReqVo) {
		StringBuffer errorMsg = new StringBuffer("");
		if (checkBusNum(quickOrderPayReqVo.getBusNum(), "1")) {
			errorMsg.append("未开通该支付方式!");
		}
		// 校验商户参数
		errorMsg.append(checkRequest(quickOrderPayReqVo.getAmount(), "订单金额"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getBusNum(), "商户号"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getOrderNo(), "支付订单号"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getUserMobile(), "手机号"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getUserAccount(),"银行帐号"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getUserCerNum(),"身份证证件号码"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getUserName(), "账户姓名"));
		errorMsg.append(checkRequest(quickOrderPayReqVo.getCardType(), "卡类型"));
		if ("2".equals(quickOrderPayReqVo.getCardType())) {// 信用卡需传
			errorMsg.append(checkRequest(quickOrderPayReqVo.getCvv2(), "CVV码"));
			errorMsg.append(checkRequest(quickOrderPayReqVo.getExpiredDate(),"卡片有效期"));
		}
		QuickOrderPayRespVo quickOrderPayRespVo = new QuickOrderPayRespVo();
		if (!"".equals(errorMsg.toString())) {
			quickOrderPayRespVo.setErrorCode("E0005");
			quickOrderPayRespVo.setErrorMessage(errorMsg.toString());
			quickOrderPayRespVo.setStatus("0");
			return quickOrderPayRespVo;
		}
		QueryParams queryParam = new QueryParams();
		queryParam.put("orderNo", quickOrderPayReqVo.getOrderNo());
		List<Order> orderList = orderRepo.findByParam(Order.class, queryParam);
		if(CollectionUtils.isEmpty(orderList)){
			quickOrderPayRespVo.setErrorCode("E0005");
			quickOrderPayRespVo.setErrorMessage("该支付订单不存在");
			quickOrderPayRespVo.setStatus("0");
			return quickOrderPayRespVo;
		}
		Order order = orderList.get(0);
		//校验快捷支付请求信息
		errorMsg.append(checkQuickOrderPayReqVo(order,quickOrderPayReqVo));
		if (!"".equals(errorMsg.toString())) {
			quickOrderPayRespVo.setErrorCode("E0005");
			quickOrderPayRespVo.setErrorMessage(errorMsg.toString());
			quickOrderPayRespVo.setStatus("0");
			return quickOrderPayRespVo;
		}
		if(OrderPayStatus.E_3.equals(order.getOrderStatus())){
			quickOrderPayRespVo.setErrorCode("");
			quickOrderPayRespVo.setErrorMessage("订单支付中");
			quickOrderPayRespVo.setStatus("3");
			return quickOrderPayRespVo;
		}
		if(OrderPayStatus.E_4.equals(order.getOrderStatus())){
			quickOrderPayRespVo.setErrorCode("");
			quickOrderPayRespVo.setErrorMessage("订单已支付成功");
			quickOrderPayRespVo.setStatus("4");
			return quickOrderPayRespVo;
		}
		
		//保存快捷支付流水
		OrderPayRequest orderPayRequest =saveQuikcPayRequest(quickOrderPayReqVo);
		KuaiqianQuickPayRequestDTO kuaiqianQuickPayRequestDTO = new KuaiqianQuickPayRequestDTO();
		kuaiqianQuickPayRequestDTO.setCardHolderId(quickOrderPayReqVo.getUserCerNum());
		kuaiqianQuickPayRequestDTO.setCardNo(quickOrderPayReqVo.getUserAccount());
	 	kuaiqianQuickPayRequestDTO.setCvv2(quickOrderPayReqVo.getCvv2());
	 	kuaiqianQuickPayRequestDTO.setExpiredDate(quickOrderPayReqVo.getExpiredDate());
	 	kuaiqianQuickPayRequestDTO.setUserTransactionNo(orderPayRequest.getPaySeriNo());
	 	kuaiqianQuickPayRequestDTO.setIdType(quickOrderPayReqVo.getUserCerType());
	 	kuaiqianQuickPayRequestDTO.setValidCode(quickOrderPayReqVo.getSmsCode());
	 	kuaiqianQuickPayRequestDTO.setValidCode(quickOrderPayReqVo.getSmsCode());
	 	try{
		 	KuaiqianQuickPayResponseDTO kuaiqianQuickPayResponseDTO =kuaiqianQuickPayService.quickPay(kuaiqianQuickPayRequestDTO);
			quickOrderPayRespVo.setOrderNo(kuaiqianQuickPayResponseDTO.getOrderNo());
		 	quickOrderPayRespVo.setErrorCode(kuaiqianQuickPayResponseDTO.getErrorCode());
			quickOrderPayRespVo.setErrorMessage(kuaiqianQuickPayResponseDTO.getErrorMessage());
			quickOrderPayRespVo.setStatus("1");
	 	}catch(Exception e){
	 		logger.error("调用快捷支付失败",e);
	 		quickOrderPayRespVo.setOrderNo(orderPayRequest.getOrderNo());
		 	quickOrderPayRespVo.setErrorCode("");
			quickOrderPayRespVo.setErrorMessage("调用快捷支付失败");
			quickOrderPayRespVo.setStatus("3");
	 	}
		return quickOrderPayRespVo;
	}

	@Override
	public String getOrderNo() {
		return UUID.randomUUID().toString().replace("-", "");

	}

	private String getPaySeriNo() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 校验商户号及支付渠道 busNum 商户号 payChannel 支付渠道枚举值(快钱,支付宝,财付通)
	 * 
	 * @return
	 */
	private Boolean checkBusNum(String busNum, String payChannel) {
		Boolean flag = true;
		QueryParams queryParams = new QueryParams();
		queryParams.put("busNum", busNum);
		queryParams.put("payChannel", payChannel);
		List<BusinessNum> businessNumList = businessNumRepo.findByBusNum(
				BusinessNum.class, queryParams);
		for (BusinessNum businessNum : businessNumList) {
			if (payChannel.equals(businessNum.getPayChannel())) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 校验请求参数
	 * 
	 * @return
	 */
	private String checkRequest(Object o, String typeName) {
		if (o == null) {
			return typeName + "不能为空!";
		} else if (o instanceof String && "".equals(o.toString().trim())) {
			return typeName + "不能为空!";
		}
		return "";
	}
	
	@Override
	public Page<Order> page(QueryParams queryParams, Pageable pageable) {
		return orderRepo.page(Order.class, queryParams, pageable);
	}
	
	
	/**
	 * 
	 * 保存请求支付流水
	 * @param order
	 * @param userAccount
	 * @param userCerNum
	 * @param userCerType
	 * @param userName
	 * @param userMobile
	 * @return
	 */
	private OrderPayRequest savePayRequest(Order order,String userAccount,String userCerNum,String userCerType,String userName,String userMobile){
		String paySeriNo = getPaySeriNo();// 支付流水号
		OrderPayRequest orderPayRequest = new OrderPayRequest();
		orderPayRequest.setAmount(order.getAmount());
		orderPayRequest.setBusNum(order.getBusNum());
		orderPayRequest.setPayRequestStatus(PayRequestStatus.E_2);
		orderPayRequest.setCreatTime(new Date());
		orderPayRequest.setOrderNo(order.getOrderNo());
		orderPayRequest.setPaySeriNo(paySeriNo);
		orderPayRequest.setUpdateTime(new Date());
		orderPayRequest.setExt1(order.getExt1());
		orderPayRequest.setExt2(order.getExt2());
		orderPayRequest.setExt3(order.getExt3());
		orderPayRequest.setExt4(order.getExt4());
		orderPayRequest.setUserAccount(userAccount);
		orderPayRequest.setUserCerNum(userCerNum);
		orderPayRequest.setUserCerType(userCerType);
		orderPayRequest.setUserName(userName);
		orderPayRequest.setUserMobile(userMobile);
		// 保存商户支付请求
		orderPayRequestRepo.save(orderPayRequest);
		return orderPayRequest;
	}
	
	/**
	 * 保存快捷支付流水
	 * @param quickOrderPayReqVo
	 * @return
	 */
	private OrderPayRequest saveQuikcPayRequest(QuickOrderPayReqVo quickOrderPayReqVo){
		// 生成支付流水号
		String paySeriNo = getPaySeriNo();
		OrderPayRequest orderPayRequest = new OrderPayRequest();
		orderPayRequest.setPaySeriNo(paySeriNo);
		orderPayRequest.setAmount(quickOrderPayReqVo.getAmount());
		orderPayRequest.setBusNum(quickOrderPayReqVo.getBusNum());
		orderPayRequest.setCreatTime(new Date());
		orderPayRequest.setOrderNo(quickOrderPayReqVo.getOrderNo());
		orderPayRequest.setUpdateTime(new Date());
		orderPayRequest.setUserAccount(quickOrderPayReqVo.getUserAccount());
		orderPayRequest.setUserCerNum(quickOrderPayReqVo.getUserCerNum());
		orderPayRequest.setUserCerType(quickOrderPayReqVo.getUserCerType());
		orderPayRequest.setUserName(quickOrderPayReqVo.getUserName());
		orderPayRequest.setUserMobile(quickOrderPayReqVo.getUserMobile());
		// 保存商户支付请求
		orderPayRequestRepo.save(orderPayRequest);
		return orderPayRequest;
	}
	
	/**
	 * 保存快捷短信生成支付流水
	 * @param quickOrderPayReqVo
	 * @return
	 */
	private OrderPayRequest saveQuikcPaySMSRequest(QuickOrderSMSReqVo quickOrderSMSReqVo){
		// 生成支付流水号
		String paySeriNo = getPaySeriNo();
		OrderPayRequest orderPayRequest = new OrderPayRequest();
		orderPayRequest.setPaySeriNo(paySeriNo);
		orderPayRequest.setAmount(quickOrderSMSReqVo.getAmount());
		orderPayRequest.setBusNum(quickOrderSMSReqVo.getBusNum());
		orderPayRequest.setCreatTime(new Date());
		orderPayRequest.setOrderNo(quickOrderSMSReqVo.getOrderNo());
		orderPayRequest.setUpdateTime(new Date());
		orderPayRequest.setUserAccount(quickOrderSMSReqVo.getUserAccount());
		orderPayRequest.setUserCerNum(quickOrderSMSReqVo.getUserCerNum());
		orderPayRequest.setUserCerType(quickOrderSMSReqVo.getUserCerType());
		orderPayRequest.setUserName(quickOrderSMSReqVo.getUserName());
		orderPayRequest.setUserMobile(quickOrderSMSReqVo.getUserMobile());
		// 保存商户支付请求
		orderPayRequestRepo.save(orderPayRequest);
		return orderPayRequest;
	}
	

	/**
	 * 保存网关支付订单
	 * @param order
	 * @param orderPayReqVo
	 * @return
	 */
	private Order saveOrder(Order order,OrderPayReqVo orderPayReqVo){
		if (order == null) {
			order = new Order();
			Date today = new Date();
			order.setCreatTime(today);
			order.setUpdateTime(today);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(today); 
			calendar.add(Calendar.DATE, 1);
			order.setOutTime(calendar.getTime());
			String orderNo = getOrderNo();
			order.setOrderStatus(OrderPayStatus.E_2);
			order.setOrderNo(orderNo);
		}
		order.setUpdateTime(new Date());
		order.setBusNum(orderPayReqVo.getBusNum());
		order.setBusDetail(orderPayReqVo.getOrderDetail());
		order.setAmount(orderPayReqVo.getAmount());
		order.setBusOrderNo(orderPayReqVo.getBusOrderNo());
		order.setExt1(orderPayReqVo.getExt1());
		order.setExt2(orderPayReqVo.getExt2());
		order.setExt3(orderPayReqVo.getExt3());
		order.setExt4(orderPayReqVo.getExt4());
		if(order.getId()!=null){
			orderRepo.update(order);
		}else{
			orderRepo.save(order);
		}
		return order;
	}
	
	
	/**
	 * 保存快捷支付订单
	 * @param order
	 * @param orderPayReqVo
	 * @return
	 */
	private Order saveOrder(Order order,QuickOrderReqVo quickOrderReqVo){
		order.setUpdateTime(new Date());
		order.setBusNum(quickOrderReqVo.getBusNum());
		order.setAmount(quickOrderReqVo.getAmount());
		order.setBusOrderNo(quickOrderReqVo.getBusOrderNo());
		order.setExt1(quickOrderReqVo.getExt1());
		order.setExt2(quickOrderReqVo.getExt2());
		order.setExt3(quickOrderReqVo.getExt3());
		order.setExt4(quickOrderReqVo.getExt4());
		Date today = new Date();
		order.setCreatTime(today);
		order.setUpdateTime(today);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(today); 
		calendar.add(Calendar.DATE, 1);
		order.setOutTime(calendar.getTime());
		order.setOrderStatus(OrderPayStatus.E_2);
		String orderNo = getOrderNo();
		order.setOrderNo(orderNo);
		orderRepo.save(order);
		return order;
	}
	/**
	 * 校验快捷支付及订单信息
	 * @param order
	 * @param quickOrderPayReqVo
	 * @return
	 */
	private String checkQuickOrderPayReqVo(Order order, QuickOrderPayReqVo quickOrderPayReqVo) {
		if(!quickOrderPayReqVo.getBusNum().equals(order.getBusNum())){
			return "商户号不正确";
		}
		if(!quickOrderPayReqVo.getBusOrderNo().equals(order.getBusOrderNo())){
			return "商户订单号不正确";
		}
		return "";
	}
}
