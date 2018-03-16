/**
 * Copyright (c)  2013, 大地保险CCIC
 * All rights reserved. 
 *
 * 2013-3-19 下午2:25:03 MjunLee
 */
package com.newtouch.payment.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.newtouch.common.model.QueryParams;
import com.newtouch.payment.constant.CommonConst;
import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.im.PaymentStatus;
import com.newtouch.payment.model.Payment;
import com.newtouch.payment.repository.PaymentRepo;
import com.newtouch.payment.service.KuaiqianPayCallBackService;
import com.newtouch.payment.service.KuaiqianQuickPayCallBackProcessService;
import com.newtouch.payment.service.kuaiqian.support.send.InitTrans;
import com.newtouch.payment.service.kuaiqian.support.util.ParseUtil;
import com.newtouch.payment.service.kuaiqian.support.util.SignUtil;


/**
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: NewTouch
 * </p>
 * 
 * @author MjunLee
 * @version 1.0
 */
@Service("kuaiqianQuickPayPayCallBackService")
public class KuaiqianQuickPayPayCallBackServiceImpl implements KuaiqianPayCallBackService {
	private final static Logger logger = LoggerFactory.getLogger(KuaiqianQuickPayPayCallBackServiceImpl.class);
	@Resource
	private KuaiqianQuickPayCallBackProcessService kuaiqianQuickPayCallBackProcessService;
	
	@Autowired
	private PaymentRepo paymentRepo;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String receiveXml = null;
		receiveXml = (String) req.getAttribute("_receiveXml_");
		logger.info("==========快钱无卡通知信息：" + receiveXml);
		Map<String, String> payInfo = new HashMap<String, String>();
		// 通知信息校验不通过
		try {
			payInfo = this.checkCallbackMsg(receiveXml);
			this.validCallbackValues(payInfo);
			kuaiqianQuickPayCallBackProcessService.saveCallBackPayment(payInfo);
			String contentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>1.0</version><TxnMsgContent><txnType>PUR</txnType><interactiveStatus>TR4</interactiveStatus>"
					+ "<merchantId>"
					+ payInfo.get("merchantId")
					+ "</merchantId><terminalId>"
					+ payInfo.get("terminalId")
					+ "</terminalId><refNumber>"
					+ payInfo.get("refNumber") + "</refNumber></TxnMsgContent></MasMessage>";
			resp.getWriter().write(contentXML);
		} catch (ServiceException e) {
			logger.info("=====无卡返回信息校验失败", e);
			String contentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>1.0</version><TxnMsgContent><txnType>PUR</txnType><interactiveStatus>TR4</interactiveStatus>"
					+ "<merchantId>"
					+ payInfo.get("merchantId")
					+ "</merchantId><terminalId>"
					+ payInfo.get("terminalId")
					+ "</terminalId><refNumber>"
					+ payInfo.get("refNumber") + "</refNumber></TxnMsgContent></MasMessage>";
			resp.getWriter().write(contentXML);
			return;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private Map<String, String> checkCallbackMsg(String receiveXml) throws ServiceException {
		boolean b = true;
		try {
			b = SignUtil.veriSignForXml(receiveXml, new ClassPathResource(CommonConst.NOCARD_RESOURCE.getString("publickey")).getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new ServiceException("快钱无卡通知，加密信息校验失败！", e);
		}
		if (!b) {
			throw new ServiceException("快钱无卡通知，信息已被串改！");
		}
		@SuppressWarnings("unchecked")
		Map<String, String> payInfo = ParseUtil.parseXML(receiveXml, new InitTrans("PUR").getTransInfo());
		if (!"00".equals(payInfo.get("responseCode"))) {
			logger.info("=========快钱无卡支付通知，通知状态：" + payInfo.get("responseCode") + "非成功状态！");
			throw new ServiceException("通知状态：" + payInfo.get("responseCode") + "非成功状态！");
		}
		return payInfo;
	}

	private void validCallbackValues(Map<String, String> values) throws ServiceException {
		String paymentNo = values.get("externalRefNumber");
		QueryParams queryParams = new QueryParams();
		queryParams.put("paymentNo", paymentNo);
		Payment payment = paymentRepo.findOneByParam(Payment.class, queryParams);
		// 获取支付信息
		if (payment == null) {
			logger.info("=========快钱网关支付通知，支付号：" + paymentNo + "无对应信息！");
			throw new ServiceException("支付号：" + paymentNo + "无对应信息！");
		}
		// 校验支付金额
		BigDecimal payAoumtBd = BigDecimal.valueOf(payment.getPaymentAmount());
		BigDecimal amountBd = new BigDecimal(values.get("amount").toString());
		if (payAoumtBd.compareTo(amountBd) != 0) {
			logger.info("=========快钱无卡支付通知，支付通知金额：" + values.get("amount") + "元与支付申请金额：" + payment.getPaymentAmount() + "元不一致！");
			throw new ServiceException("支付通知金额：" + values.get("amount") + "元与支付申请金额：" + payment.getPaymentAmount() + "元不一致！");
		}
		logger.info("=========快钱无卡支付通知，支付通知支付号：" + payment.getPaymentNo() + "对应支付状态:" + payment.getPaymentStatus());
		if (!PaymentStatus.PS_PAYING.equals(payment.getPaymentStatus()) && !PaymentStatus.PS_WAITPAY.equals(payment.getPaymentStatus())
				&& !PaymentStatus.PS_NOEFFITIVE.equals(payment.getPaymentStatus())) {
			logger.info("=========快钱无卡支付通知，支付通知支付号：" + payment.getPaymentNo() + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
			throw new ServiceException("支付通知支付号：" + payment.getPaymentNo() + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
		}
	}
}
