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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.ccic.eb.common.constant.Constant;
import com.ccic.eb.constant.payment.OrderStatus;
import com.ccic.eb.constant.payment.SponsorSystem;
import com.ccic.eb.constant.payment.TransType;
import com.ccic.eb.model.payment.Payment;
import com.ccic.eb.model.payment.PaymentApplication;
import com.ccic.eb.service.onlinepayment.PayNoService;
import com.ccic.eb.service.onlinepayment.kuaiqianpayment.TransErrorService;
import com.ccic.eb.service.onlinepayment.support.combill99mgw.send.InitTrans;
import com.ccic.eb.service.onlinepayment.support.combill99mgw.util.ParseUtil;
import com.ccic.eb.service.onlinepayment.support.combill99mgw.util.SignUtil;
import com.ccic.eb.service.paycallback.NotifyPayResultService;
import com.ccic.eb.service.paycallback.exception.CallBackBussinessException;
import com.ccic.eb.service.paycallback.kuaiqianpaycallback.KuaiqianNoCardCallBackProcessService;
import com.ccic.eb.service.paycallback.kuaiqianpaycallback.KuaiqianPayCallBackService;
import com.newtouch.lightframework.service.exception.ServiceException;

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
@Service("kuaiqianNoCardCallBackService")
public class KuaiqianQuickPayPayCallBackServiceImpl implements KuaiqianPayCallBackService {
	private final static Log log = LogFactory.getLog(KuaiqianOnlinePayCallBackServiceImpl.class);
	@Resource(name = "payNoService")
	private PayNoService payNoService;
	@Resource(name = "notifyPayResultService")
	private NotifyPayResultService notifyPayResultService;
	@Resource
	private TransErrorService transErrorService;
	@Resource
	private KuaiqianNoCardCallBackProcessService kuaiqianNoCardCallBackProcessService;

	@Override
	public void callBackPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String receiveXml = null;
		receiveXml = (String) req.getAttribute("_receiveXml_");
		log.info("==========快钱无卡通知信息：" + receiveXml);
		Map<String, String> payInfo = new HashMap<String, String>();
		PaymentApplication pa;
		// 通知信息校验不通过
		try {
			payInfo = this.checkCallbackMsg(receiveXml);
			pa = payNoService.findApplicationByPayNo(payInfo.get("externalRefNumber"));
			this.validCallbackValues(payInfo, pa);
			kuaiqianNoCardCallBackProcessService.saveCallBackPayment(payInfo, pa);
			String contentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>1.0</version><TxnMsgContent><txnType>PUR</txnType><interactiveStatus>TR4</interactiveStatus>"
					+ "<merchantId>"
					+ payInfo.get("merchantId")
					+ "</merchantId><terminalId>"
					+ payInfo.get("terminalId")
					+ "</terminalId><refNumber>"
					+ payInfo.get("refNumber") + "</refNumber></TxnMsgContent></MasMessage>";
			resp.getWriter().write(contentXML);
		} catch (CallBackBussinessException e) {
			log.info("=====无卡返回信息校验失败", e);
			transErrorService.createTransError("KUAIQIAN", payInfo.get("externalRefNumber"), "", TransType.NO_CARD_PAY, e.getMessage());
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
			transErrorService.createTransError("KUAIQIAN", payInfo.get("externalRefNumber"), "", TransType.NO_CARD_PAY, e.getMessage());
			throw new ServiceException(e);
		}
		try {
			// 通知收付费回调结果
			notifyPayResultService.sendPayResultToPayment(pa.getPayApplyNo());
		} catch (Exception e) {
			log.error("=====通知收付费支付成功信息失败", e);
		}
		try {
			// 通知合作网站支付成功结果
			if (SponsorSystem.WANGXIAO.equals(pa.getPayment().getSource())) {
				notifyPayResultService.sendPayResultToCoop(pa.getPayApplyNo());
			}
		} catch (Exception e) {
			log.error("=====通知合作网站支付成功信息失败", e);
		}
	}

	private Map<String, String> checkCallbackMsg(String receiveXml) throws CallBackBussinessException {
		boolean b = true;
		try {
			b = SignUtil.veriSignForXml(receiveXml, new ClassPathResource(Constant.NOCARD_RESOURCE.getString("publickey")).getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new ServiceException("快钱无卡通知，加密信息校验失败！", e);
		}
		if (!b) {
			throw new CallBackBussinessException("快钱无卡通知，信息已被串改！");
		}
		@SuppressWarnings("unchecked")
		Map<String, String> payInfo = ParseUtil.parseXML(receiveXml, new InitTrans("PUR").getTransInfo());
		if (!"00".equals(payInfo.get("responseCode"))) {
			log.info("=========快钱无卡支付通知，通知状态：" + payInfo.get("responseCode") + "非成功状态！");
			transErrorService.createTransError("KUAIQIAN", payInfo.get("externalRefNumber"), null, TransType.NO_CARD_PAY,
					"快钱无卡支付通知，通知状态：" + payInfo.get("externalRefNumber") + "非成功状态！");
			throw new CallBackBussinessException("通知状态：" + payInfo.get("responseCode") + "非成功状态！");
		}
		return payInfo;
	}

	private void validCallbackValues(Map<String, String> values, PaymentApplication pa) throws CallBackBussinessException {
		// 获取支付信息
		if (pa == null) {
			log.info("=========快钱网关支付通知，支付号：" + values.get("externalRefNumber") + "无对应信息！");
			transErrorService.createTransError("KUAIQIAN", values.get("externalRefNumber"), null, TransType.NO_CARD_PAY,
					"快钱无卡支付通知，支付号：" + values.get("externalRefNumber") + "无对应信息！");
			throw new CallBackBussinessException("支付号：" + values.get("externalRefNumber") + "无对应信息！");
		}
		Payment pm = pa.getPayment();
		// 校验支付金额
		BigDecimal payAoumtBd = BigDecimal.valueOf(pm.getPaymentAmount());
		BigDecimal amountBd = new BigDecimal(values.get("amount").toString());
		if (payAoumtBd.compareTo(amountBd) != 0) {
			log.info("=========快钱无卡支付通知，支付通知金额：" + values.get("amount") + "元与支付申请金额：" + pm.getPaymentAmount() + "元不一致！");
			throw new CallBackBussinessException("支付通知金额：" + values.get("amount") + "元与支付申请金额：" + pm.getPaymentAmount() + "元不一致！");
		}
		log.info("=========快钱无卡支付通知，支付通知支付号：" + pm.getPaymentNo() + "对应支付状态:" + pm.getPaymentStatus());
		if (!OrderStatus.PAYING.equals(pm.getPaymentStatus()) && !OrderStatus.CREATENOTPAY.equals(pm.getPaymentStatus())
				&& !OrderStatus.INVALID.equals(pm.getPaymentStatus())) {
			log.info("=========快钱无卡支付通知，支付通知支付号：" + pm.getPaymentNo() + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
			throw new CallBackBussinessException("支付通知支付号：" + pm.getPaymentNo() + "对应大地支付平台中不是支付中状态，拒绝接受此通知！");
		}
	}
}
