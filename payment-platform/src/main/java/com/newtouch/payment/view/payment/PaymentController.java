package com.newtouch.payment.view.payment;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.common.view.ReqModel;
import com.newtouch.common.view.RespModel;
import com.newtouch.common.view.Views;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianRequestDTO;
import com.newtouch.payment.service.PaymentPlatformService;

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
	private PaymentPlatformService paymentPlatformService;
	
	/**
	 * 快钱网关支付
	 * @param kuaiqianRequestDto
	 * @return
	 */
	@RequestMapping(value = "/gatewayPay",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel orderPay(@RequestBody ReqModel reqModel) {
		KuaiqianRequestDTO requestDto = reqModel.toObject(KuaiqianRequestDTO.class);
		logger.trace(requestDto.toString());
		KuaiqianGateWayResponseDTO responseDto  = paymentPlatformService.gatewayPay(requestDto);
		if (StringUtils.isNotBlank(responseDto.getErrorCode())) {
			return Views.getErrorModel(responseDto.getErrorCode());
		}
		JSONObject jo = new JSONObject();
		jo.put("url", responseDto.getUrl());
		jo.put("params", responseDto.getParams());
		return new RespModel(jo);
	}

	/**
	 * 短信验证码获取
	 * @param verifyCodeRequestDto
	 * @return
	 */
	@RequestMapping(value = "/sendVerifyCode",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel orderQuery(@RequestBody ReqModel reqModel) {
		DynamicVerifyCodeRequestDTO requestDto= reqModel.toObject(DynamicVerifyCodeRequestDTO.class);
		DynamicVerifyCodeResponseDTO responseDto = paymentPlatformService.getQuickPaySmsCode(requestDto);
		if (StringUtils.isNotBlank(responseDto.getErrorCode())) {
			return Views.getErrorModel(responseDto.getErrorCode());
		}
		return Views.getSuccessModel();
	}
	
	/**
	 * 快捷支付
	 * @param quickPayRequestDto
	 * @return
	 */
	@RequestMapping(value = "/quickPay",consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public RespModel quickOrderPay(@RequestBody ReqModel reqModel) {
		KuaiqianRequestDTO requestDto = reqModel.toObject(KuaiqianRequestDTO.class);
		KuaiqianQuickPayResponseDTO responseDto = paymentPlatformService.quickPay(requestDto);
		if (StringUtils.isNotBlank(responseDto.getErrorCode())) {
			return Views.getErrorModel(responseDto.getErrorCode());
		}
		return Views.getSuccessModel();
	}
	
}
