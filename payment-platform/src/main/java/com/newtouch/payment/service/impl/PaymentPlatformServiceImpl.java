package com.newtouch.payment.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.demo.service.impl.CurdServiceImpl;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.constant.ErrCode;
import com.newtouch.payment.im.PayType;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.model.AuthPro;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeRequestDTO;
import com.newtouch.payment.model.DTO.DynamicVerifyCodeResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianGateWayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianQuickPayResponseDTO;
import com.newtouch.payment.model.DTO.KuaiqianRequestDTO;
import com.newtouch.payment.repository.AuthProRepo;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.service.DynamicVerifyCodeService;
import com.newtouch.payment.service.KuaiqianGateWayService;
import com.newtouch.payment.service.KuaiqianQuickPayService;
import com.newtouch.payment.service.PaymentPlatformService;

@Service
public class PaymentPlatformServiceImpl implements PaymentPlatformService {
	
	private static Logger logger = LoggerFactory.getLogger(CurdServiceImpl.class);
	
	@Autowired
	private PaymentRepo paymentRepo;
	
	@Autowired
	private AuthProRepo authProRepo;
	
	@Autowired
	private KuaiqianGateWayService kuaiqianGateWayService;
	
	@Autowired
	private KuaiqianQuickPayService kuaiqianQuickPayService;
	
	@Autowired
	private DynamicVerifyCodeService dynamicVerifyCodeService;
	
	@Override
	public KuaiqianGateWayResponseDTO gatewayPay(KuaiqianRequestDTO requestDto) {
		StringBuffer errorMsg = new StringBuffer("");
		KuaiqianGateWayResponseDTO responseDto = new KuaiqianGateWayResponseDTO();

		// 校验商户参数
		errorMsg.append(checkRequest(requestDto.getPaymentNo(), "支付号"));
		if (!"".equals(errorMsg.toString())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage(errorMsg.toString());
			return responseDto;
		}
		QueryParams queryParams = new QueryParams();
		queryParams.put(CommonConst.PAYMENTNO, requestDto.getPaymentNo());
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		if (payment == null ) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("支付信息不存在!");
			return responseDto;
		}
		if(payment != null && PaymentStatus.PS_NOEFFITIVE.equals(payment.getPaymentStatus())){
			responseDto.setErrorCode(ErrCode.NOEFFECTIVE);
			responseDto.setErrorMessage("支付信息已失效!");
			return responseDto;
		}
		if(payment !=null && PaymentStatus.PS_SUCCESS.equals(payment.getPaymentStatus())){
			responseDto.setErrorCode(ErrCode.HADPAY);
			responseDto.setErrorMessage("支付信息已支付!");
			return responseDto;
		}
		if(payment !=null && payment.getPayEffEnd().before(new Date())){
			responseDto.setErrorCode(ErrCode.NOEFFECTIVE);
			responseDto.setErrorMessage("支付信息已失效!");
			return responseDto;
		}
		// TODO 调用支付方法,返回调用结果
		try{
			responseDto = kuaiqianGateWayService.gateWayPay(requestDto.getPaymentNo(), PayType.ONLINE, requestDto.getBankCode());
		} catch(Exception e){
			logger.error("调用网关支付失败",e);
			responseDto.setErrorCode(ErrCode.EXCEPTION);
			responseDto.setErrorMessage(e.getMessage());
		}
		return responseDto;
	}

	@Override
	public DynamicVerifyCodeResponseDTO getQuickPaySmsCode(DynamicVerifyCodeRequestDTO requestDto) {
		
		DynamicVerifyCodeResponseDTO responseDTO=dynamicVerifyCodeService.getDynamicVerifyCode(requestDto);
		return responseDTO;
	}

	@Override
	public KuaiqianQuickPayResponseDTO quickPay(KuaiqianRequestDTO requestDto) {
		KuaiqianQuickPayResponseDTO responseDto = new KuaiqianQuickPayResponseDTO();
		if (!checkAuth(requestDto)) {
			responseDto.setErrorCode(ErrCode.NOAUTH);
			responseDto.setErrorMessage("无支付权限,请开通支付权限!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getPaymentNo())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("支付号为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getCardNo())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("银行卡号为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getAccountName())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("账户名为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getIdType())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("证件类型为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getCardHolderId())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("证件号为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getTelePhone())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("手机号为空!");
			return responseDto;
		}
		if (StringUtils.isBlank(requestDto.getValidCode())) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("验证码为空!");
			return responseDto;
		}
		if (CommonConst.CREDIT.equals(requestDto.getCardType())) {// 信用卡需传
			if (StringUtils.isBlank(requestDto.getCvv2())) {
				responseDto.setErrorCode(ErrCode.NODATA);
				responseDto.setErrorMessage("卡校验码为空!");
				return responseDto;
			}
			if (StringUtils.isBlank(requestDto.getExpiredMonth()) || StringUtils.isBlank(requestDto.getExpiredYear())) {
				responseDto.setErrorCode(ErrCode.NODATA);
				responseDto.setErrorMessage("卡有效期为空!");
				return responseDto;
			}
		}
		QueryParams queryParams = new QueryParams();
		queryParams.put(CommonConst.PAYMENTNO, requestDto.getPaymentNo());
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		if (payment == null ) {
			responseDto.setErrorCode(ErrCode.NODATA);
			responseDto.setErrorMessage("支付信息不存在!");
			return responseDto;
		}
		if(payment != null && PaymentStatus.PS_NOEFFITIVE.equals(payment.getPaymentStatus())){
			responseDto.setErrorCode(ErrCode.NOEFFECTIVE);
			responseDto.setErrorMessage("支付信息已失效!");
			return responseDto;
		}
		if(payment !=null && PaymentStatus.PS_SUCCESS.equals(payment.getPaymentStatus())){
			responseDto.setErrorCode(ErrCode.HADPAY);
			responseDto.setErrorMessage("支付信息已支付!");
			return responseDto;
		}
		if(payment !=null && payment.getPayEffEnd().before(new Date())){
			responseDto.setErrorCode(ErrCode.NOEFFECTIVE);
			responseDto.setErrorMessage("支付信息已失效!");
			return responseDto;
		}
	 	try{
	 		responseDto = kuaiqianQuickPayService.quickPay(requestDto);
	 	}catch(Exception e){
	 		logger.error("调用快捷支付失败",e);
	 		responseDto.setErrorCode(ErrCode.EXCEPTION);
			responseDto.setErrorMessage(e.getMessage());
	 	}
		return responseDto;
	}

	/**
	 * 根据请求信息校验支付权限
	 * 
	 * @return
	 */
	private Boolean checkAuth(KuaiqianRequestDTO requestDTO) {
		Boolean flag = false;
		QueryParams queryParams = new QueryParams();
		queryParams.put(CommonConst.CONSUMERID, requestDTO.getConsumerID());
		queryParams.put(CommonConst.COMCODE, requestDTO.getComCode());
		List<AuthPro> authPro = authProRepo.findByParam(AuthPro.class, queryParams);
		if (authPro != null && authPro.size() > 0 ) {
			flag = true;
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
	
}
